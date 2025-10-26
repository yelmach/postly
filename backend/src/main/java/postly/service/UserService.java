package postly.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import postly.dto.request.UpdateProfileRequest;
import postly.dto.response.UserResponse;
import postly.entity.UserEntity;
import postly.exception.ApiException;
import postly.repository.PostRepository;
import postly.repository.SubscriptionRepository;
import postly.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    SubscriptionService subscriptionService;

    @Autowired
    PasswordEncoder passwordEncoder;

    public UserEntity getCurrentUserEntity() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (UserEntity) auth.getPrincipal();
    }

    public UserResponse getCurrentUser() {
        UserEntity currentUser = getCurrentUserEntity();

        long postsCount = postRepository.countByUserId(currentUser.getId());
        long subscribersCount = subscriptionRepository.countBySubscribedToId(currentUser.getId());
        long subscribedCount = subscriptionRepository.countBySubscriberId(currentUser.getId());

        return UserResponse.fromUser(currentUser)
                .postsCount(postsCount)
                .subscribersCount(subscribersCount)
                .subscribedCount(subscribedCount)
                .build();
    }

    public UserResponse getUserByUsername(String username) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> ApiException.notFound("User not exist with username: " + username));

        long postsCount = postRepository.countByUserId(user.getId());
        long subscribersCount = subscriptionRepository.countBySubscribedToId(user.getId());
        long subscribedCount = subscriptionRepository.countBySubscriberId(user.getId());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Boolean isSubscribed = null;
        if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof UserEntity) {
            UserEntity currentUser = (UserEntity) auth.getPrincipal();
            isSubscribed = subscriptionService.isSubscribed(currentUser.getId(), user.getId());
        }

        return UserResponse.fromUser(user)
                .postsCount(postsCount)
                .subscribersCount(subscribersCount)
                .subscribedCount(subscribedCount)
                .isSubscribed(isSubscribed)
                .build();
    }

    public UserResponse updateUser(UpdateProfileRequest request) {
        UserEntity user = getCurrentUserEntity();

        if (request.firstName() != null && !request.firstName().isBlank()) {
            user.setFirstName(request.firstName().trim());
        }

        if (request.lastName() != null && !request.lastName().isBlank()) {
            user.setLastName(request.lastName().trim());
        }

        if (request.email() != null && !request.email().isBlank()) {
            String trimmedEmail = request.email().trim();
            if (!trimmedEmail.equals(user.getEmail()) && userRepository.existsByEmail(trimmedEmail)) {
                throw ApiException.conflict("Email is already taken");
            }
            user.setEmail(trimmedEmail);
        }

        if (request.password() != null && !request.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.password()));
        }

        if (request.bio() != null) {
            user.setBio(request.bio().trim());
        }

        UserEntity updatedUser = userRepository.save(user);

        long postsCount = postRepository.countByUserId(updatedUser.getId());
        long subscribersCount = subscriptionRepository.countBySubscribedToId(updatedUser.getId());
        long subscribedCount = subscriptionRepository.countBySubscriberId(updatedUser.getId());

        return UserResponse.fromUser(updatedUser)
                .postsCount(postsCount)
                .subscribersCount(subscribersCount)
                .subscribedCount(subscribedCount)
                .build();
    }

    public List<UserResponse> searchUsers(String query) {
        List<UserEntity> users = userRepository.searchUsers(query);

        return users.stream()
                .map(user -> {
                    return UserResponse.fromUser(user).build();
                })
                .collect(Collectors.toList());
    }

    public List<UserResponse> getSuggestedUsers(int limit) {
        UserEntity currentUser = getCurrentUserEntity();

        List<UserEntity> users = userRepository.findSuggestedUsers(currentUser.getId());

        return users.stream().limit(limit).map(user -> {
            return UserResponse.fromUser(user).build();
        }).collect(Collectors.toList());
    }
}