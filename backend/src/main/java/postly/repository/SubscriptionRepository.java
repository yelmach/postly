package postly.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import postly.entity.SubscriptionEntity;

@Repository
public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, Long> {

    long countBySubscribedToId(Long userId);

    long countBySubscriberId(Long userId);

    boolean existsBySubscriberIdAndSubscribedToId(Long subscriberId, Long subscribedToId);

    void deleteBySubscriberIdAndSubscribedToId(Long subscriberId, Long subscribedToId);
}
