-- Post Media table for storing multiple media files per post
CREATE TABLE post_media (
    id BIGSERIAL PRIMARY KEY,
    post_id BIGINT,
    media_url VARCHAR(512) NOT NULL,
    media_type VARCHAR(20) NOT NULL CHECK (media_type IN ('IMAGE', 'VIDEO')),
    is_temporary BOOLEAN NOT NULL DEFAULT TRUE,
    expires_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_post_media_post FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE
);

-- Indexes for performance
CREATE INDEX idx_post_media_post_id ON post_media(post_id);

CREATE INDEX idx_post_media_url ON post_media(media_url);

CREATE INDEX idx_post_media_temporary_expires ON post_media(is_temporary, expires_at)
WHERE
    is_temporary = TRUE;