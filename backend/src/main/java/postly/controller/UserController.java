package postly.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import postly.dto.request.UpdateProfileRequest;
import postly.dto.response.UserResponse;
import postly.service.SubscriptionService;
import postly.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    SubscriptionService subscriptionService;

    @GetMapping("/{username}")
    public ResponseEntity<UserResponse> getUserProfile(@PathVariable String username) {
        UserResponse user = userService.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }

    @PutMapping()
    public ResponseEntity<UserResponse> updateProfile(@Valid @RequestBody UpdateProfileRequest request) {
        UserResponse updatedUser = userService.updateUser(request);
        return ResponseEntity.ok(updatedUser);
    }

    @PostMapping("/{userId}/subscriptions")
    public ResponseEntity<Void> subscribe(@PathVariable Long userId) {
        subscriptionService.subscribe(userId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{userId}/subscriptions")
    public ResponseEntity<Void> unsubscribe(@PathVariable Long userId) {
        subscriptionService.unsubscribe(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}/subscribers")
    public ResponseEntity<List<UserResponse>> getSubscribers(@PathVariable Long userId) {
        List<UserResponse> subscribers = subscriptionService.getSubscribers(userId);
        return ResponseEntity.ok(subscribers);
    }

    @GetMapping("/{userId}/subscriptions")
    public ResponseEntity<List<UserResponse>> getSubscriptions(@PathVariable Long userId) {
        List<UserResponse> subscriptions = subscriptionService.getSubscriptions(userId);
        return ResponseEntity.ok(subscriptions);
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserResponse>> searchUsers(@RequestParam String query) {
        List<UserResponse> users = userService.searchUsers(query);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/suggestions")
    public ResponseEntity<List<UserResponse>> getSuggestedUsers(
            @RequestParam(defaultValue = "5") int limit) {
        List<UserResponse> users = userService.getSuggestedUsers(limit);
        return ResponseEntity.ok(users);
    }
}