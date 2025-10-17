package postly.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import postly.dto.request.BanUserRequest;
import postly.dto.request.HidePostRequest;
import postly.dto.request.ResolveReportRequest;
import postly.dto.response.ReportResponse;
import postly.dto.response.UserResponse;
import postly.service.ModerationService;
import postly.service.ReportService;

@RestController
@RequestMapping("/api/moderation")
@PreAuthorize("hasRole('ADMIN')")
public class ModerationController {

    @Autowired
    private ModerationService moderationService;

    @Autowired
    private ReportService reportService;

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

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        moderationService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/users/{userId}/role")
    public ResponseEntity<UserResponse> changeUserRole(@PathVariable Long userId) {
        UserResponse user = moderationService.changeUserRole(userId);
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

    @PatchMapping("/{id}")
    public ResponseEntity<ReportResponse> resolveReport(@PathVariable Long id,
            @Valid @RequestBody ResolveReportRequest request) {
        ReportResponse report = reportService.resolveReport(id, request);
        return ResponseEntity.ok(report);
    }
}
