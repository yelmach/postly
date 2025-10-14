package postly.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import postly.dto.request.BanUserRequest;
import postly.dto.response.UserResponse;
import postly.entity.PostEntity;
import postly.entity.UserEntity;
import postly.exception.ApiException;
import postly.repository.PostRepository;
import postly.repository.UserRepository;

@Service
public class ModerationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserService userService;

    @Transactional
    public UserResponse banUser(Long userId, BanUserRequest request) {
        UserEntity currentUser = userService.getCurrentUserEntity();

        UserEntity userToBan = userRepository.findById(userId)
                .orElseThrow(() -> ApiException.notFound("User not found"));

        if (userToBan.isAdmin()) {
            throw ApiException.forbidden("Cannot ban admin users");
        }

        // Prevent banning yourself
        if (userToBan.getId().equals(currentUser.getId())) {
            throw ApiException.badRequest("You cannot ban yourself");
        }

        userToBan.setIsBanned(true);
        userToBan.setBanReason(request.reason());

        if (Boolean.TRUE.equals(request.permanent())) {
            userToBan.setBannedUntil(null);
        } else if (request.durationDays() != null && request.durationDays() > 0) {
            userToBan.setBannedUntil(LocalDateTime.now().plusDays(request.durationDays()));
        } else {
            userToBan.setBannedUntil(null);
        }

        userToBan = userRepository.save(userToBan);
        return UserResponse.fromUser(userToBan).build();
    }

    @Transactional
    public UserResponse unbanUser(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> ApiException.notFound("User not found"));

        if (!user.getIsBanned()) {
            throw ApiException.badRequest("User is not banned");
        }

        user.setIsBanned(false);
        user.setBannedUntil(null);
        user.setBanReason(null);

        user = userRepository.save(user);
        return UserResponse.fromUser(user).build();
    }

    @Transactional
    public void hidePost(Long postId, String reason) {
        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> ApiException.notFound("Post not found"));

        if (post.getIsHidden()) {
            throw ApiException.badRequest("Post is already hidden");
        }

        post.setIsHidden(true);
        postRepository.save(post);
    }

    @Transactional
    public void restorePost(Long postId) {
        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> ApiException.notFound("Post not found"));

        if (!post.getIsHidden()) {
            throw ApiException.badRequest("Post is not hidden");
        }

        post.setIsHidden(false);
        postRepository.save(post);
    }

    @Transactional
    public void deletePost(Long postId) {
        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> ApiException.notFound("Post not found"));

        postRepository.delete(post);
    }
}
