-- Subscription table
CREATE TABLE subscriptions (
    id BIGSERIAL PRIMARY KEY,
    subscriber_id BIGINT NOT NULL,
    subscribed_to_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_subscriber FOREIGN KEY (subscriber_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_subscribed_to FOREIGN KEY (subscribed_to_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT unique_subscription UNIQUE (subscriber_id, subscribed_to_id),
    CONSTRAINT check_no_self_subscription CHECK (subscriber_id != subscribed_to_id)
);

CREATE INDEX idx_subscriptions_subscriber ON subscriptions(subscriber_id);

CREATE INDEX idx_subscriptions_subscribed_to ON subscriptions(subscribed_to_id);

CREATE INDEX idx_subscriptions_created_at ON subscriptions(created_at);