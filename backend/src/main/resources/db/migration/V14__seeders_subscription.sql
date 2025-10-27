INSERT INTO
    subscriptions (subscriber_id, subscribed_to_id, created_at)
VALUES
    (1, 2, CURRENT_TIMESTAMP - INTERVAL '140 days'),
    (1, 3, CURRENT_TIMESTAMP - INTERVAL '135 days'),
    (1, 4, CURRENT_TIMESTAMP - INTERVAL '120 days'),
    (1, 8, CURRENT_TIMESTAMP - INTERVAL '85 days'),
    (1, 15, CURRENT_TIMESTAMP - INTERVAL '50 days');

-- Popular users (many followers)
-- Sarah (id: 2) - 12 followers
INSERT INTO
    subscriptions (subscriber_id, subscribed_to_id, created_at)
VALUES
    (3, 2, CURRENT_TIMESTAMP - INTERVAL '130 days'),
    (4, 2, CURRENT_TIMESTAMP - INTERVAL '125 days'),
    (5, 2, CURRENT_TIMESTAMP - INTERVAL '115 days'),
    (6, 2, CURRENT_TIMESTAMP - INTERVAL '105 days'),
    (8, 2, CURRENT_TIMESTAMP - INTERVAL '90 days'),
    (11, 2, CURRENT_TIMESTAMP - INTERVAL '75 days'),
    (12, 2, CURRENT_TIMESTAMP - INTERVAL '70 days'),
    (15, 2, CURRENT_TIMESTAMP - INTERVAL '55 days'),
    (18, 2, CURRENT_TIMESTAMP - INTERVAL '40 days'),
    (22, 2, CURRENT_TIMESTAMP - INTERVAL '25 days'),
    (25, 2, CURRENT_TIMESTAMP - INTERVAL '18 days'),
    (28, 2, CURRENT_TIMESTAMP - INTERVAL '10 days');

-- Mike (id: 3) - 10 followers
INSERT INTO
    subscriptions (subscriber_id, subscribed_to_id, created_at)
VALUES
    (2, 3, CURRENT_TIMESTAMP - INTERVAL '128 days'),
    (4, 3, CURRENT_TIMESTAMP - INTERVAL '122 days'),
    (7, 3, CURRENT_TIMESTAMP - INTERVAL '95 days'),
    (9, 3, CURRENT_TIMESTAMP - INTERVAL '85 days'),
    (11, 3, CURRENT_TIMESTAMP - INTERVAL '76 days'),
    (13, 3, CURRENT_TIMESTAMP - INTERVAL '65 days'),
    (17, 3, CURRENT_TIMESTAMP - INTERVAL '45 days'),
    (21, 3, CURRENT_TIMESTAMP - INTERVAL '28 days'),
    (26, 3, CURRENT_TIMESTAMP - INTERVAL '16 days'),
    (30, 3, CURRENT_TIMESTAMP - INTERVAL '6 days');

-- Emma (id: 4) - 9 followers
INSERT INTO
    subscriptions (subscriber_id, subscribed_to_id, created_at)
VALUES
    (2, 4, CURRENT_TIMESTAMP - INTERVAL '126 days'),
    (3, 4, CURRENT_TIMESTAMP - INTERVAL '124 days'),
    (7, 4, CURRENT_TIMESTAMP - INTERVAL '92 days'),
    (10, 4, CURRENT_TIMESTAMP - INTERVAL '82 days'),
    (12, 4, CURRENT_TIMESTAMP - INTERVAL '68 days'),
    (14, 4, CURRENT_TIMESTAMP - INTERVAL '60 days'),
    (24, 4, CURRENT_TIMESTAMP - INTERVAL '20 days'),
    (26, 4, CURRENT_TIMESTAMP - INTERVAL '15 days'),
    (28, 4, CURRENT_TIMESTAMP - INTERVAL '9 days');

-- Active community members with mutual follows
-- Frontend/Backend developers cluster
INSERT INTO
    subscriptions (subscriber_id, subscribed_to_id, created_at)
VALUES
    (5, 11, CURRENT_TIMESTAMP - INTERVAL '112 days'),
    (11, 5, CURRENT_TIMESTAMP - INTERVAL '110 days'),
    (8, 11, CURRENT_TIMESTAMP - INTERVAL '88 days'),
    (11, 8, CURRENT_TIMESTAMP - INTERVAL '87 days'),
    (12, 8, CURRENT_TIMESTAMP - INTERVAL '72 days'),
    (8, 12, CURRENT_TIMESTAMP - INTERVAL '71 days');

-- Data/AI enthusiasts cluster
INSERT INTO
    subscriptions (subscriber_id, subscribed_to_id, created_at)
VALUES
    (6, 15, CURRENT_TIMESTAMP - INTERVAL '100 days'),
    (15, 6, CURRENT_TIMESTAMP - INTERVAL '58 days'),
    (26, 6, CURRENT_TIMESTAMP - INTERVAL '17 days'),
    (26, 15, CURRENT_TIMESTAMP - INTERVAL '17 days'),
    (6, 26, CURRENT_TIMESTAMP - INTERVAL '16 days');

-- DevOps/Cloud engineers cluster
INSERT INTO
    subscriptions (subscriber_id, subscribed_to_id, created_at)
VALUES
    (5, 21, CURRENT_TIMESTAMP - INTERVAL '108 days'),
    (21, 5, CURRENT_TIMESTAMP - INTERVAL '29 days'),
    (27, 5, CURRENT_TIMESTAMP - INTERVAL '14 days'),
    (27, 21, CURRENT_TIMESTAMP - INTERVAL '14 days'),
    (5, 27, CURRENT_TIMESTAMP - INTERVAL '13 days'),
    (21, 27, CURRENT_TIMESTAMP - INTERVAL '27 days');

-- Students following seniors
INSERT INTO
    subscriptions (subscriber_id, subscribed_to_id, created_at)
VALUES
    (12, 3, CURRENT_TIMESTAMP - INTERVAL '73 days'),
    (12, 11, CURRENT_TIMESTAMP - INTERVAL '72 days'),
    (26, 8, CURRENT_TIMESTAMP - INTERVAL '16 days'),
    (26, 11, CURRENT_TIMESTAMP - INTERVAL '15 days'),
    (28, 3, CURRENT_TIMESTAMP - INTERVAL '11 days'),
    (28, 12, CURRENT_TIMESTAMP - INTERVAL '9 days');

-- Content creators and tech writers
INSERT INTO
    subscriptions (subscriber_id, subscribed_to_id, created_at)
VALUES
    (14, 2, CURRENT_TIMESTAMP - INTERVAL '62 days'),
    (14, 3, CURRENT_TIMESTAMP - INTERVAL '61 days'),
    (24, 2, CURRENT_TIMESTAMP - INTERVAL '21 days'),
    (24, 3, CURRENT_TIMESTAMP - INTERVAL '20 days'),
    (24, 14, CURRENT_TIMESTAMP - INTERVAL '19 days'),
    (14, 24, CURRENT_TIMESTAMP - INTERVAL '59 days');

-- Random diverse connections
INSERT INTO
    subscriptions (subscriber_id, subscribed_to_id, created_at)
VALUES
    (9, 17, CURRENT_TIMESTAMP - INTERVAL '84 days'),
    (17, 9, CURRENT_TIMESTAMP - INTERVAL '44 days'),
    (10, 14, CURRENT_TIMESTAMP - INTERVAL '80 days'),
    (13, 7, CURRENT_TIMESTAMP - INTERVAL '66 days'),
    (16, 8, CURRENT_TIMESTAMP - INTERVAL '52 days'),
    (16, 12, CURRENT_TIMESTAMP - INTERVAL '51 days'),
    (18, 5, CURRENT_TIMESTAMP - INTERVAL '42 days'),
    (18, 27, CURRENT_TIMESTAMP - INTERVAL '41 days'),
    (19, 2, CURRENT_TIMESTAMP - INTERVAL '38 days'),
    (19, 11, CURRENT_TIMESTAMP - INTERVAL '37 days'),
    (20, 11, CURRENT_TIMESTAMP - INTERVAL '33 days'),
    (20, 8, CURRENT_TIMESTAMP - INTERVAL '32 days'),
    (22, 8, CURRENT_TIMESTAMP - INTERVAL '26 days'),
    (23, 13, CURRENT_TIMESTAMP - INTERVAL '23 days'),
    (23, 7, CURRENT_TIMESTAMP - INTERVAL '22 days'),
    (25, 11, CURRENT_TIMESTAMP - INTERVAL '19 days'),
    (25, 8, CURRENT_TIMESTAMP - INTERVAL '18 days'),
    (29, 5, CURRENT_TIMESTAMP - INTERVAL '9 days'),
    (29, 18, CURRENT_TIMESTAMP - INTERVAL '8 days'),
    (30, 2, CURRENT_TIMESTAMP - INTERVAL '6 days'),
    (30, 28, CURRENT_TIMESTAMP - INTERVAL '5 days');

-- More realistic one-way follows
INSERT INTO
    subscriptions (subscriber_id, subscribed_to_id, created_at)
VALUES
    (15, 11, CURRENT_TIMESTAMP - INTERVAL '54 days'),
    (17, 2, CURRENT_TIMESTAMP - INTERVAL '46 days'),
    (19, 3, CURRENT_TIMESTAMP - INTERVAL '36 days'),
    (20, 2, CURRENT_TIMESTAMP - INTERVAL '34 days'),
    (21, 11, CURRENT_TIMESTAMP - INTERVAL '30 days'),
    (22, 3, CURRENT_TIMESTAMP - INTERVAL '24 days'),
    (23, 4, CURRENT_TIMESTAMP - INTERVAL '21 days'),
    (25, 3, CURRENT_TIMESTAMP - INTERVAL '17 days'),
    (27, 11, CURRENT_TIMESTAMP - INTERVAL '13 days'),
    (29, 21, CURRENT_TIMESTAMP - INTERVAL '8 days'),
    (30, 4, CURRENT_TIMESTAMP - INTERVAL '5 days');

-- Recent follows (last week)
INSERT INTO
    subscriptions (subscriber_id, subscribed_to_id, created_at)
VALUES
    (28, 8, CURRENT_TIMESTAMP - INTERVAL '6 days'),
    (28, 11, CURRENT_TIMESTAMP - INTERVAL '5 days'),
    (29, 3, CURRENT_TIMESTAMP - INTERVAL '4 days'),
    (30, 8, CURRENT_TIMESTAMP - INTERVAL '3 days'),
    (30, 11, CURRENT_TIMESTAMP - INTERVAL '2 days'),
    (30, 12, CURRENT_TIMESTAMP - INTERVAL '1 day');