package postly.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import postly.dto.response.UserResponse;
import postly.entity.SubscriptionEntity;
import postly.entity.UserEntity;
import postly.exception.ApiException;
import postly.repository.SubscriptionRepository;
import postly.repository.UserRepository;

@Service
public class SubscriptionService {

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    NotificationService notificationService;

    @Transactional
    public void subscribe(Long targetUserId) {
        UserEntity currentUser = getCurrentUserEntity();

        if (currentUser.getId().equals(targetUserId)) {
            throw ApiException.badRequest("You cannot subscribe yourself");
        }

        UserEntity targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> ApiException.notFound("User not found"));

        if (subscriptionRepository.existsBySubscriberIdAndSubscribedToId(currentUser.getId(), targetUserId)) {
            throw ApiException.conflict("You are already subscribed to this user");
        }

        SubscriptionEntity subscription = new SubscriptionEntity(currentUser, targetUser);
        subscriptionRepository.save(subscription);

        // Create notification for the subscribed user
        notificationService.createSubscriberNotification(targetUserId, currentUser.getId());
    }

    @Transactional
    public void unsubscribe(Long targetUserId) {
        UserEntity currentUser = getCurrentUserEntity();

        if (currentUser.getId().equals(targetUserId)) {
            throw ApiException.badRequest("You cannot unsubscribe from yourself");
        }

        if (!subscriptionRepository.existsBySubscriberIdAndSubscribedToId(currentUser.getId(), targetUserId)) {
            throw ApiException.notFound("You are not subscribed to this user");
        }

        subscriptionRepository.deleteBySubscriberIdAndSubscribedToId(currentUser.getId(), targetUserId);
    }

    public boolean isSubscribed(Long subscriberId, Long targetUserId) {
        if (subscriberId == null || targetUserId == null) {
            return false;
        }
        return subscriptionRepository.existsBySubscriberIdAndSubscribedToId(subscriberId, targetUserId);
    }

    public List<UserResponse> getSubscribers(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> ApiException.notFound("User not found"));

        List<SubscriptionEntity> subscriptions = subscriptionRepository.findBySubscribedToId(userId);

        UserEntity currentUser = getCurrentUserEntity();
        final Long currentUserId = currentUser.getId();

        return subscriptions.stream()
                .map(subscription -> {
                    UserEntity subscriber = subscription.getSubscriber();
                    Boolean isSubscribed = isSubscribed(currentUserId, subscriber.getId());

                    return UserResponse.fromUser(subscriber)
                            .isSubscribed(isSubscribed)
                            .build();
                })
                .collect(Collectors.toList());
    }

    public List<UserResponse> getSubscriptions(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> ApiException.notFound("User not found"));

        List<SubscriptionEntity> subscriptions = subscriptionRepository.findBySubscriberId(userId);

        UserEntity currentUser = getCurrentUserEntity();
        final Long currentUserId = currentUser.getId();

        return subscriptions.stream()
                .map(subscription -> {
                    UserEntity subscribedTo = subscription.getSubscribedTo();
                    Boolean isSubscribed = isSubscribed(currentUserId, subscribedTo.getId());

                    return UserResponse.fromUser(subscribedTo)
                            .isSubscribed(isSubscribed)
                            .build();
                })
                .collect(Collectors.toList());
    }

    private UserEntity getCurrentUserEntity() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (UserEntity) auth.getPrincipal();
    }
}
