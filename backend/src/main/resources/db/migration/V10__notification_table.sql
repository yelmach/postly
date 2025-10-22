-- Notifications table
CREATE TABLE notifications (
    id BIGSERIAL PRIMARY KEY,
    reciever_id BIGINT NOT NULL,
    sender_id BIGINT NOT NULL,
    post_id BIGINT,
    type VARCHAR(50) NOT NULL CHECK (type IN ('NEW_SUBSCRIBER', 'NEW_POST')),
    message VARCHAR(255) NOT NULL,
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_notification_reciever FOREIGN KEY (reciever_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_notification_sender FOREIGN KEY (sender_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_notification_post FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE
);

-- Indexes for better query performance
CREATE INDEX idx_notifications_reciever_id ON notifications(reciever_id);

CREATE INDEX idx_notifications_reciever_created ON notifications(reciever_id, created_at DESC);

CREATE INDEX idx_notifications_reciever_is_read ON notifications(reciever_id, is_read);

CREATE INDEX idx_notifications_created_at ON notifications(created_at DESC);