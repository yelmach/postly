-- Users table
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT null,
    role VARCHAR(20) NOT NULL DEFAULT 'USER' CHECK (role IN ('USER', 'ADMIN')),
    bio TEXT,
    is_active BOOLEAN NOT NULL DEFAULT true,
    profile_url VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Subscriptions table
CREATE TABLE subscriptions (
    id BIGSERIAL PRIMARY KEY,
    subscriber_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    subscribed_to_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(subscriber_id, subscribed_to_id),
    CHECK (subscriber_id != subscribed_to_id)
);

-- Indexes for better performance
CREATE INDEX idx_subscriptions_subscriber ON subscriptions(subscriber_id);

CREATE INDEX idx_subscriptions_subscribed_to ON subscriptions(subscribed_to_id);