package postly.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "subscritions", uniqueConstraints = {
        @UniqueConstraint(name = "unique_subscription", columnNames = { "subscriber_id", "subscribed_to_id" })
})
public class SubscriptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "subscriber_id", nullable = false)
    private UserEntity subscriber;

    @ManyToOne
    @JoinColumn(name = "subscribed_to_id", nullable = false)
    private UserEntity subscribedTo;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public SubscriptionEntity() {
    }

    public SubscriptionEntity(UserEntity subscriber, UserEntity subscribedTo) {
        this.subscriber = subscriber;
        this.subscribedTo = subscribedTo;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntity getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(UserEntity subscriber) {
        this.subscriber = subscriber;
    }

    public UserEntity getSubscribedTo() {
        return subscribedTo;
    }

    public void setSubscribedTo(UserEntity subscribedTo) {
        this.subscribedTo = subscribedTo;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
