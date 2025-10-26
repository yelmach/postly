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
import postly.repository.UserRepository;

@Service
public class AuthService {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserEntity register(RegisterRequest request) {
        String firstName = request.firstName() != null ? request.firstName().trim() : null;
        String lastName = request.lastName() != null ? request.lastName().trim() : null;
        String username = request.username() != null ? request.username().trim() : null;
        String email = request.email() != null ? request.email().trim() : null;
        String bio = request.bio() != null ? request.bio().trim() : null;

        if (userRepository.existsByUsername(username)) {
            throw ApiException.badRequest("Username is already taken");
        }

        if (userRepository.existsByEmail(email)) {
            throw ApiException.badRequest("Email is already registered");
        }

        UserEntity user = new UserEntity();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(Role.USER);
        user.setBio(bio);

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