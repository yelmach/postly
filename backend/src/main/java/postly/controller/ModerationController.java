package postly.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import postly.dto.request.BanUserRequest;
import postly.dto.request.HidePostRequest;
import postly.dto.response.UserResponse;
import postly.service.ModerationService;

@RestController
@RequestMapping("/api/moderation")
public class ModerationController {

    @Autowired
    private ModerationService moderationService;

    @PostMapping("/users/{userId}/ban")
    public ResponseEntity<UserResponse> banUser(@PathVariable Long userId,
            @Valid @RequestBody BanUserRequest request) {
        UserResponse user = moderationService.banUser(userId, request);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/users/{userId}/ban")
    public ResponseEntity<UserResponse> unbanUser(@PathVariable Long userId) {
        UserResponse user = moderationService.unbanUser(userId);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/posts/{postId}/hide")
    public ResponseEntity<Void> hidePost(@PathVariable Long postId, @Valid @RequestBody HidePostRequest request) {
        moderationService.hidePost(postId, request.reason());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/posts/{postId}/restore")
    public ResponseEntity<Void> restorePost(@PathVariable Long postId) {
        moderationService.restorePost(postId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        moderationService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }
}
