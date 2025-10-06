package postly.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public void subscribe(Long targetUserId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserEntity currentUser = (UserEntity) auth.getPrincipal();

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
    }

    @Transactional
    public void unsubscribe(Long targetUserId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserEntity currentUser = (UserEntity) auth.getPrincipal();

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
}
