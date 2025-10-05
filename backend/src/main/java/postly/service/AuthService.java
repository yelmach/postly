package postly.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import postly.dto.request.LoginRequest;
import postly.dto.request.RegisterRequest;
import postly.entity.Role;
import postly.entity.UserEntity;
import postly.exception.ApiException;
import postly.repository.PostRepository;
import postly.repository.SubscriptionRepository;
import postly.repository.UserRepository;

@Service
public class AuthService {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserEntity register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw ApiException.badRequest("Username is already taken");
        }

        if (userRepository.existsByEmail(request.email())) {
            throw ApiException.badRequest("Email is already registered");
        }

        UserEntity user = new UserEntity();
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(Role.USER);
        user.setBio(request.bio());

        UserEntity savedUser = userRepository.save(user);

        return savedUser;
    }

    public UserEntity login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.credentials(),
                            request.password()));

            return (UserEntity) authentication.getPrincipal();

        } catch (Exception e) {
            throw ApiException.unauthorized("Invalid credentials");
        }
    }
}