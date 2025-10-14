-- Add ban-related fields to users table
ALTER TABLE users ADD COLUMN is_banned BOOLEAN NOT NULL DEFAULT FALSE;
ALTER TABLE users ADD COLUMN banned_until TIMESTAMP;
ALTER TABLE users ADD COLUMN ban_reason TEXT;

-- Index for querying banned users
CREATE INDEX idx_users_banned ON users(is_banned, banned_until);
