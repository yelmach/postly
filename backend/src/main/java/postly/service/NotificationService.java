package postly.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import postly.dto.response.NotificationResponse;
import postly.entity.NotificationEntity;
import postly.entity.NotificationType;
import postly.entity.PostEntity;
import postly.entity.UserEntity;
import postly.exception.ApiException;
import postly.repository.NotificationRepository;
import postly.repository.PostRepository;
import postly.repository.UserRepository;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
    private static final long SSE_TIMEOUT = 30 * 60 * 1000; // 30 minutes

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    private final Map<Long, List<SseEmitter>> userEmitters = new ConcurrentHashMap<>();

    public Page<NotificationResponse> getNotifications(Pageable pageable) {
        UserEntity currentUser = getCurrentUserEntity();
        Page<NotificationEntity> notifications = notificationRepository
                .findByRecieverIdOrderByCreatedAtDesc(currentUser.getId(), pageable);

        return notifications.map(NotificationResponse::fromNotification);
    }

    public long getUnreadCount() {
        UserEntity currentUser = getCurrentUserEntity();
        return notificationRepository.countByRecieverIdAndIsReadFalse(currentUser.getId());
    }

    public void markAsRead(Long notificationId) {
        UserEntity currentUser = getCurrentUserEntity();
        int updated = notificationRepository.markAsReadById(notificationId, currentUser.getId());

        if (updated == 0) {
            throw ApiException.notFound("Notification not found or you don't have permission");
        }
    }

    public void markAllAsRead() {
        UserEntity currentUser = getCurrentUserEntity();
        notificationRepository.markAllAsReadByUserId(currentUser.getId());
    }

    @Transactional
    public void createNotification(Long receiverId, Long senderId, Long postId, NotificationType type, String message) {
        UserEntity receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> ApiException.notFound("Receiver not found"));

        UserEntity sender = userRepository.findById(senderId)
                .orElseThrow(() -> ApiException.notFound("Sender not found"));

        NotificationEntity notification = new NotificationEntity(receiver, sender, type, message);

        if (postId != null) {
            PostEntity post = postRepository.findById(postId)
                    .orElseThrow(() -> ApiException.notFound("Post not found"));
            notification.setPost(post);
        }

        notification = notificationRepository.save(notification);

        NotificationResponse response = NotificationResponse.fromNotification(notification);
        sendToUser(receiverId, response);

        logger.info("Notification created and sent to user {}: {}", receiverId, message);
    }

    public void createSubscriberNotification(Long subscribedToUserId, Long subscriberId) {
        UserEntity subscriber = userRepository.findById(subscriberId)
                .orElseThrow(() -> ApiException.notFound("Subscriber not found"));

        String message = subscriber.getUsername() + " started following you";
        createNotification(subscribedToUserId, subscriberId, null, NotificationType.NEW_SUBSCRIBER, message);
    }

    @Async
    public void createPostNotification(Long postId, Long authorId, List<Long> subscriberIds) {
        PostEntity post = postRepository.findById(postId)
                .orElseThrow(() -> ApiException.notFound("Post not found"));

        UserEntity author = userRepository.findById(authorId)
                .orElseThrow(() -> ApiException.notFound("Author not found"));

        String message = author.getUsername() + " published a new post: " + post.getTitle();

        for (Long subscriberId : subscriberIds) {
            try {
                createNotification(subscriberId, authorId, postId, NotificationType.NEW_POST, message);
            } catch (Exception e) {
                logger.error("Failed to create notification for subscriber {}: {}", subscriberId, e.getMessage());
            }
        }
    }

    public void addEmitter(Long userId, SseEmitter emitter) {
        userEmitters.computeIfAbsent(userId, k -> new CopyOnWriteArrayList<>()).add(emitter);
        logger.info("SSE emitter added for user {}. Total emitters: {}", userId, userEmitters.get(userId).size());
    }

    public void removeEmitter(Long userId, SseEmitter emitter) {
        List<SseEmitter> emitters = userEmitters.get(userId);
        if (emitters != null) {
            emitters.remove(emitter);
            if (emitters.isEmpty()) {
                userEmitters.remove(userId);
            }
            logger.info("SSE emitter removed for user {}. Remaining emitters: {}", userId,
                    emitters.isEmpty() ? 0 : emitters.size());
        }
    }

    private void sendToUser(Long userId, NotificationResponse notification) {
        List<SseEmitter> emitters = userEmitters.get(userId);
        if (emitters == null || emitters.isEmpty()) {
            logger.debug("No active SSE connections for user {}", userId);
            return;
        }

        List<SseEmitter> deadEmitters = new CopyOnWriteArrayList<>();
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event().name("notification").data(notification));
                logger.debug("Notification sent via SSE to user {}", userId);
            } catch (IOException e) {
                logger.error("Failed to send notification via SSE to user {}: {}", userId, e.getMessage());
                deadEmitters.add(emitter);
            }
        }

        deadEmitters.forEach(emitter -> removeEmitter(userId, emitter));
    }

    public long getSseTimeout() {
        return SSE_TIMEOUT;
    }

    private UserEntity getCurrentUserEntity() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (UserEntity) auth.getPrincipal();
    }
}
