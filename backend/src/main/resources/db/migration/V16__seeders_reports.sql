-- Seed reports for testing
-- 5 user reports and 5 post reports

-- USER REPORTS
-- Report 1: User 10 reports User 9 for spam
INSERT INTO reports (reporter_id, reported_user_id, reported_post_id, reason, description, status, reviewed_by, admin_notes, created_at, reviewed_at) VALUES
(10, 9, NULL, 'SPAM', 'This user is constantly spamming in comments and posts with promotional links.', 'RESOLVED', 1, 'Reviewed and warned the user. Removed spam content.', CURRENT_TIMESTAMP - INTERVAL '15 days', CURRENT_TIMESTAMP - INTERVAL '14 days');

-- Report 2: User 14 reports User 23 for inappropriate content
INSERT INTO reports (reporter_id, reported_user_id, reported_post_id, reason, description, status, reviewed_by, admin_notes, created_at, reviewed_at) VALUES
(14, 23, NULL, 'INAPPROPRIATE_CONTENT', 'Profile bio contains offensive language and inappropriate images.', 'RESOLVED', 1, 'User profile cleaned up. Warning issued.', CURRENT_TIMESTAMP - INTERVAL '10 days', CURRENT_TIMESTAMP - INTERVAL '9 days');

-- Report 3: User 18 reports User 13 for harassment
INSERT INTO reports (reporter_id, reported_user_id, reported_post_id, reason, description, status, reviewed_by, admin_notes, created_at, reviewed_at) VALUES
(18, 13, NULL, 'HARASSMENT', 'This user has been sending harassing comments and direct messages.', 'DISMISSED', 1, 'Investigated - no evidence of harassment found. Appears to be a misunderstanding.', CURRENT_TIMESTAMP - INTERVAL '7 days', CURRENT_TIMESTAMP - INTERVAL '6 days');

-- Report 4: User 22 reports User 19 for hate speech (PENDING)
INSERT INTO reports (reporter_id, reported_user_id, reported_post_id, reason, description, status, created_at) VALUES
(22, 19, NULL, 'HATE_SPEECH', 'User posted comments containing discriminatory language and hate speech.', 'PENDING', CURRENT_TIMESTAMP - INTERVAL '3 days');

-- Report 5: User 25 reports User 16 for misinformation (PENDING)
INSERT INTO reports (reporter_id, reported_user_id, reported_post_id, reason, description, status, created_at) VALUES
(25, 16, NULL, 'MISINFORMATION', 'Spreading false information about security vulnerabilities that could harm others.', 'PENDING', CURRENT_TIMESTAMP - INTERVAL '1 day');

-- POST REPORTS
-- Report 6: User 8 reports Post 46 (will_a's React Native Tips) for spam
INSERT INTO reports (reporter_id, reported_user_id, reported_post_id, reason, description, status, reviewed_by, admin_notes, created_at, reviewed_at) VALUES
(8, NULL, 46, 'SPAM', 'This post is just advertising a paid course with no real content.', 'RESOLVED', 1, 'Post reviewed - contains legitimate tips. No action taken.', CURRENT_TIMESTAMP - INTERVAL '12 days', CURRENT_TIMESTAMP - INTERVAL '11 days');

-- Report 7: User 17 reports Post 51 for inappropriate content
INSERT INTO reports (reporter_id, reported_user_id, reported_post_id, reason, description, status, reviewed_by, admin_notes, created_at, reviewed_at) VALUES
(17, NULL, 51, 'INAPPROPRIATE_CONTENT', 'Post contains inappropriate images and offensive language.', 'RESOLVED', 1, 'Post removed. User warned about community guidelines.', CURRENT_TIMESTAMP - INTERVAL '8 days', CURRENT_TIMESTAMP - INTERVAL '7 days');

-- Report 8: User 20 reports Post 58 for misinformation
INSERT INTO reports (reporter_id, reported_user_id, reported_post_id, reason, description, status, reviewed_by, admin_notes, created_at, reviewed_at) VALUES
(20, NULL, 58, 'MISINFORMATION', 'Post contains false technical information that could mislead developers.', 'DISMISSED', 1, 'Technical content reviewed by senior dev. Information is accurate.', CURRENT_TIMESTAMP - INTERVAL '5 days', CURRENT_TIMESTAMP - INTERVAL '4 days');

-- Report 9: User 24 reports Post 64 for spam (PENDING)
INSERT INTO reports (reporter_id, reported_user_id, reported_post_id, reason, description, status, created_at) VALUES
(24, NULL, 64, 'SPAM', 'Post is full of promotional links and advertisements, not real content.', 'PENDING', CURRENT_TIMESTAMP - INTERVAL '2 days');

-- Report 10: User 29 reports Post 68 for other reason (PENDING)
INSERT INTO reports (reporter_id, reported_user_id, reported_post_id, reason, description, status, created_at) VALUES
(29, NULL, 68, 'OTHER', 'This post seems to be plagiarized from another developer blog without attribution.', 'PENDING', CURRENT_TIMESTAMP - INTERVAL '12 hours');
