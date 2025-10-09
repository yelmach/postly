-- Likes table
CREATE TABLE likes (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    post_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_like_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_like_post FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE,
    CONSTRAINT unique_user_post_like UNIQUE (user_id, post_id)
);

-- Indexes for performance
CREATE INDEX idx_likes_post_id ON likes(post_id);

CREATE INDEX idx_likes_user_id ON likes(user_id);

CREATE INDEX idx_likes_created_at ON likes(created_at DESC);