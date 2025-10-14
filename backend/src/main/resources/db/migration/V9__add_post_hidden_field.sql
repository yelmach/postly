-- Add hidden field to posts table
ALTER TABLE posts ADD COLUMN is_hidden BOOLEAN NOT NULL DEFAULT FALSE;

-- Index for filtering hidden posts
CREATE INDEX idx_posts_hidden ON posts(is_hidden);
