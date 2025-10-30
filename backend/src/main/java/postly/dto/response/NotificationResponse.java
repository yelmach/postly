package postly.dto.response;

import postly.entity.NotificationEntity;
import postly.entity.NotificationType;

import java.time.LocalDateTime;

public record NotificationResponse(
        Long id,
        NotificationType type,
        String message,
        Boolean isRead,
        LocalDateTime createdAt,
        String senderUsername,
        Long postId) {
    public static NotificationResponse fromNotification(NotificationEntity notification) {
        return new NotificationResponse(
                notification.getId(),
                notification.getType(),
                notification.getMessage(),
                notification.getIsRead(),
                notification.getCreatedAt(),
                notification.getSender().getUsername(),
                notification.getPost() != null ? notification.getPost().getId() : null);
    }
}
