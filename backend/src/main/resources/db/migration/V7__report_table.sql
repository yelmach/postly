-- Reports table
CREATE TABLE reports (
    id BIGSERIAL PRIMARY KEY,
    reporter_id BIGINT NOT NULL,
    reported_user_id BIGINT,
    reported_post_id BIGINT,
    reason VARCHAR(50) NOT NULL CHECK (
        reason IN (
            'SPAM',
            'HARASSMENT',
            'INAPPROPRIATE_CONTENT',
            'HATE_SPEECH',
            'MISINFORMATION',
            'OTHER'
        )
    ),
    description TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'RESOLVED', 'DISMISSED')),
    reviewed_by BIGINT,
    admin_notes TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    reviewed_at TIMESTAMP,
    CONSTRAINT fk_report_reporter FOREIGN KEY (reporter_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_report_reported_user FOREIGN KEY (reported_user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_report_reported_post FOREIGN KEY (reported_post_id) REFERENCES posts(id) ON DELETE CASCADE,
    CONSTRAINT fk_report_reviewer FOREIGN KEY (reviewed_by) REFERENCES users(id) ON DELETE
    SET
        NULL,
        CONSTRAINT check_report_target CHECK (
            (
                reported_user_id IS NOT NULL
                AND reported_post_id IS NULL
            )
            OR (
                reported_user_id IS NULL
                AND reported_post_id IS NOT NULL
            )
        )
);

-- Indexes for performance
CREATE INDEX idx_reports_reporter ON reports(reporter_id);

CREATE INDEX idx_reports_reported_user ON reports(reported_user_id);

CREATE INDEX idx_reports_reported_post ON reports(reported_post_id);

CREATE INDEX idx_reports_status ON reports(status, created_at DESC);

CREATE INDEX idx_reports_created_at ON reports(created_at DESC);

-- Index to prevent duplicate reports
CREATE UNIQUE INDEX idx_reports_unique_user ON reports(reporter_id, reported_user_id)
WHERE
    reported_user_id IS NOT NULL
    AND status = 'PENDING';

CREATE UNIQUE INDEX idx_reports_unique_post ON reports(reporter_id, reported_post_id)
WHERE
    reported_post_id IS NOT NULL
    AND status = 'PENDING';