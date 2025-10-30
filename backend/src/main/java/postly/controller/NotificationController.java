package postly.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import postly.dto.response.NotificationResponse;
import postly.entity.UserEntity;
import postly.service.NotificationService;
import postly.service.UserService;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);

    @Autowired
    private NotificationService notificationService;

    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity<Page<NotificationResponse>> getNotifications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<NotificationResponse> notifications = notificationService.getNotifications(pageable);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/unread/count")
    public ResponseEntity<Map<String, Long>> getUnreadCount() {
        long count = notificationService.getUnreadCount();
        Map<String, Long> response = new HashMap<>();
        response.put("count", count);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/read-all")
    public ResponseEntity<Void> markAllAsRead() {
        notificationService.markAllAsRead();
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamNotifications() {
        UserEntity currentUser = userService.getCurrentUserEntity();
        Long userId = currentUser.getId();

        SseEmitter emitter = new SseEmitter(notificationService.getSseTimeout());

        emitter.onCompletion(() -> {
            logger.info("SSE connection completed for user {}", userId);
            notificationService.removeEmitter(userId, emitter);
        });

        emitter.onTimeout(() -> {
            logger.info("SSE connection timeout for user {}", userId);
            notificationService.removeEmitter(userId, emitter);
        });

        emitter.onError(e -> {
            logger.error("SSE connection error for user {}: {}", userId, e.getMessage());
            notificationService.removeEmitter(userId, emitter);
        });

        notificationService.addEmitter(userId, emitter);

        try {
            Map<String, String> connectMessage = new HashMap<>();
            connectMessage.put("status", "connected");
            connectMessage.put("message", "SSE connection established");
            emitter.send(SseEmitter.event()
                    .name("connected")
                    .data(connectMessage));
            logger.info("SSE connection established for user {}", userId);
        } catch (IOException e) {
            logger.error("Failed to send initial SSE message to user {}: {}", userId, e.getMessage());
            emitter.completeWithError(e);
        }

        return emitter;
    }
}
