package postly.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    FileStorageService fileStorageService;

    @Autowired
    PasswordEncoder passwordEncoder;

    public UserResponse getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserEntity currentUser = (UserEntity) auth.getPrincipal();

        long postsCount = postRepository.countByUserId(currentUser.getId());
        long followersCount = subscriptionRepository.countBySubscribedToId(currentUser.getId());
        long followingCount = subscriptionRepository.countBySubscriberId(currentUser.getId());

        return UserResponse.fromUser(currentUser)
                .postsCount(postsCount)
                .followersCount(followersCount)
                .followingCount(followingCount)
                .build();
    }

    public UserResponse getUserByUsername(String username) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> ApiException.notFound("User not exist with username: " + username));

        long postsCount = postRepository.countByUserId(user.getId());
        long followersCount = subscriptionRepository.countBySubscribedToId(user.getId());
        long followingCount = subscriptionRepository.countBySubscriberId(user.getId());

        return UserResponse.fromUser(user)
                .postsCount(postsCount)
                .followersCount(followersCount)
                .followingCount(followingCount)
                .build();
    }

    public UserResponse updateUser(UpdateProfileRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserEntity currentUser = (UserEntity) auth.getPrincipal();

        UserEntity user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> ApiException.notFound("User not found"));

        if (request.firstName() != null && !request.firstName().isBlank()) {
            user.setFirstName(request.firstName());
        }

        if (request.lastName() != null && !request.lastName().isBlank()) {
            user.setLastName(request.lastName());
        }

        if (request.email() != null && !request.email().isBlank()) {
            if (!request.email().equals(user.getEmail()) && userRepository.existsByEmail(request.email())) {
                throw ApiException.conflict("Email is already taken");
            }
            user.setEmail(request.email());
        }

        if (request.password() != null && !request.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.password()));
        }

        if (request.bio() != null) {
            user.setBio(request.bio());
        }

        UserEntity updatedUser = userRepository.save(user);

        long postsCount = postRepository.countByUserId(updatedUser.getId());
        long followersCount = subscriptionRepository.countBySubscribedToId(updatedUser.getId());
        long followingCount = subscriptionRepository.countBySubscriberId(updatedUser.getId());

        return UserResponse.fromUser(updatedUser)
                .postsCount(postsCount)
                .followersCount(followersCount)
                .followingCount(followingCount)
                .build();
    }

    public UserResponse updateProfilePicture(MultipartFile file) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserEntity currentUser = (UserEntity) auth.getPrincipal();

        UserEntity user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> ApiException.notFound("User not found"));

        if (user.getProfileUrl() != null && !user.getProfileUrl().isEmpty()) {
            fileStorageService.deleteFile(user.getProfileUrl());
        }

        String profileUrl = fileStorageService.storeProfilePicture(file, user.getId());
        user.setProfileUrl(profileUrl);

        UserEntity updatedUser = userRepository.save(user);

        long postsCount = postRepository.countByUserId(updatedUser.getId());
        long followersCount = subscriptionRepository.countBySubscribedToId(updatedUser.getId());
        long followingCount = subscriptionRepository.countBySubscriberId(updatedUser.getId());

        return UserResponse.fromUser(updatedUser)
                .postsCount(postsCount)
                .followersCount(followersCount)
                .followingCount(followingCount)
                .build();
    }
}