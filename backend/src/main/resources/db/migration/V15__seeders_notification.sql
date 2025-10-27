-- Seed notifications for testing
-- Types: NEW_SUBSCRIBER, NEW_POST

-- NEW_SUBSCRIBER notifications (when someone subscribes to you)
-- Sarah gets notified about her new subscribers
INSERT INTO notifications (reciever_id, sender_id, post_id, type, message, is_read, created_at) VALUES
(2, 3, NULL, 'NEW_SUBSCRIBER', 'mike_chen started following you', TRUE, CURRENT_TIMESTAMP - INTERVAL '130 days'),
(2, 4, NULL, 'NEW_SUBSCRIBER', 'emma_codes started following you', TRUE, CURRENT_TIMESTAMP - INTERVAL '125 days'),
(2, 5, NULL, 'NEW_SUBSCRIBER', 'james_w started following you', TRUE, CURRENT_TIMESTAMP - INTERVAL '115 days'),
(2, 6, NULL, 'NEW_SUBSCRIBER', 'olivia_m started following you', TRUE, CURRENT_TIMESTAMP - INTERVAL '105 days'),
(2, 8, NULL, 'NEW_SUBSCRIBER', 'sophia_dev started following you', TRUE, CURRENT_TIMESTAMP - INTERVAL '90 days'),
(2, 11, NULL, 'NEW_SUBSCRIBER', 'lucas_r started following you', TRUE, CURRENT_TIMESTAMP - INTERVAL '75 days'),
(2, 12, NULL, 'NEW_SUBSCRIBER', 'mia_codes started following you', FALSE, CURRENT_TIMESTAMP - INTERVAL '70 days'),
(2, 15, NULL, 'NEW_SUBSCRIBER', 'alex_w started following you', FALSE, CURRENT_TIMESTAMP - INTERVAL '55 days'),
(2, 18, NULL, 'NEW_SUBSCRIBER', 'eve_w started following you', FALSE, CURRENT_TIMESTAMP - INTERVAL '40 days'),
(2, 22, NULL, 'NEW_SUBSCRIBER', 'abby_w started following you', FALSE, CURRENT_TIMESTAMP - INTERVAL '25 days'),
(2, 25, NULL, 'NEW_SUBSCRIBER', 'jack_y started following you', FALSE, CURRENT_TIMESTAMP - INTERVAL '18 days'),
(2, 28, NULL, 'NEW_SUBSCRIBER', 'ella_l started following you', FALSE, CURRENT_TIMESTAMP - INTERVAL '10 days');

-- Mike gets notified
INSERT INTO notifications (reciever_id, sender_id, post_id, type, message, is_read, created_at) VALUES
(3, 2, NULL, 'NEW_SUBSCRIBER', 'sarah_j started following you', TRUE, CURRENT_TIMESTAMP - INTERVAL '128 days'),
(3, 4, NULL, 'NEW_SUBSCRIBER', 'emma_codes started following you', TRUE, CURRENT_TIMESTAMP - INTERVAL '122 days'),
(3, 7, NULL, 'NEW_SUBSCRIBER', 'will_a started following you', TRUE, CURRENT_TIMESTAMP - INTERVAL '95 days'),
(3, 9, NULL, 'NEW_SUBSCRIBER', 'ben_t started following you', TRUE, CURRENT_TIMESTAMP - INTERVAL '85 days'),
(3, 11, NULL, 'NEW_SUBSCRIBER', 'lucas_r started following you', FALSE, CURRENT_TIMESTAMP - INTERVAL '76 days'),
(3, 13, NULL, 'NEW_SUBSCRIBER', 'henry_l started following you', FALSE, CURRENT_TIMESTAMP - INTERVAL '65 days'),
(3, 17, NULL, 'NEW_SUBSCRIBER', 'dan_lee started following you', FALSE, CURRENT_TIMESTAMP - INTERVAL '45 days'),
(3, 21, NULL, 'NEW_SUBSCRIBER', 'ethan_l started following you', FALSE, CURRENT_TIMESTAMP - INTERVAL '28 days'),
(3, 26, NULL, 'NEW_SUBSCRIBER', 'avery_k started following you', FALSE, CURRENT_TIMESTAMP - INTERVAL '16 days'),
(3, 30, NULL, 'NEW_SUBSCRIBER', 'sofia_s started following you', FALSE, CURRENT_TIMESTAMP - INTERVAL '6 days');

-- Emma gets notified
INSERT INTO notifications (reciever_id, sender_id, post_id, type, message, is_read, created_at) VALUES
(4, 2, NULL, 'NEW_SUBSCRIBER', 'sarah_j started following you', TRUE, CURRENT_TIMESTAMP - INTERVAL '126 days'),
(4, 3, NULL, 'NEW_SUBSCRIBER', 'mike_chen started following you', TRUE, CURRENT_TIMESTAMP - INTERVAL '124 days'),
(4, 7, NULL, 'NEW_SUBSCRIBER', 'will_a started following you', TRUE, CURRENT_TIMESTAMP - INTERVAL '92 days'),
(4, 10, NULL, 'NEW_SUBSCRIBER', 'bella_g started following you', FALSE, CURRENT_TIMESTAMP - INTERVAL '82 days'),
(4, 12, NULL, 'NEW_SUBSCRIBER', 'mia_codes started following you', FALSE, CURRENT_TIMESTAMP - INTERVAL '68 days'),
(4, 14, NULL, 'NEW_SUBSCRIBER', 'char_g started following you', FALSE, CURRENT_TIMESTAMP - INTERVAL '60 days'),
(4, 24, NULL, 'NEW_SUBSCRIBER', 'emily_a started following you', FALSE, CURRENT_TIMESTAMP - INTERVAL '20 days'),
(4, 26, NULL, 'NEW_SUBSCRIBER', 'avery_k started following you', FALSE, CURRENT_TIMESTAMP - INTERVAL '15 days'),
(4, 28, NULL, 'NEW_SUBSCRIBER', 'ella_l started following you', FALSE, CURRENT_TIMESTAMP - INTERVAL '9 days');

-- Lucas gets notified (popular backend dev)
INSERT INTO notifications (reciever_id, sender_id, post_id, type, message, is_read, created_at) VALUES
(11, 5, NULL, 'NEW_SUBSCRIBER', 'james_w started following you', TRUE, CURRENT_TIMESTAMP - INTERVAL '112 days'),
(11, 8, NULL, 'NEW_SUBSCRIBER', 'sophia_dev started following you', TRUE, CURRENT_TIMESTAMP - INTERVAL '88 days'),
(11, 12, NULL, 'NEW_SUBSCRIBER', 'mia_codes started following you', FALSE, CURRENT_TIMESTAMP - INTERVAL '72 days'),
(11, 15, NULL, 'NEW_SUBSCRIBER', 'alex_w started following you', FALSE, CURRENT_TIMESTAMP - INTERVAL '54 days'),
(11, 19, NULL, 'NEW_SUBSCRIBER', 'matt_h started following you', FALSE, CURRENT_TIMESTAMP - INTERVAL '37 days'),
(11, 21, NULL, 'NEW_SUBSCRIBER', 'ethan_l started following you', FALSE, CURRENT_TIMESTAMP - INTERVAL '30 days'),
(11, 25, NULL, 'NEW_SUBSCRIBER', 'jack_y started following you', FALSE, CURRENT_TIMESTAMP - INTERVAL '19 days'),
(11, 26, NULL, 'NEW_SUBSCRIBER', 'avery_k started following you', FALSE, CURRENT_TIMESTAMP - INTERVAL '15 days'),
(11, 28, NULL, 'NEW_SUBSCRIBER', 'ella_l started following you', FALSE, CURRENT_TIMESTAMP - INTERVAL '5 days'),
(11, 30, NULL, 'NEW_SUBSCRIBER', 'sofia_s started following you', FALSE, CURRENT_TIMESTAMP - INTERVAL '2 days');

-- Other users get subscriber notifications
INSERT INTO notifications (reciever_id, sender_id, post_id, type, message, is_read, created_at) VALUES
(5, 11, NULL, 'NEW_SUBSCRIBER', 'lucas_r started following you', TRUE, CURRENT_TIMESTAMP - INTERVAL '110 days'),
(5, 21, NULL, 'NEW_SUBSCRIBER', 'ethan_l started following you', FALSE, CURRENT_TIMESTAMP - INTERVAL '29 days'),
(5, 27, NULL, 'NEW_SUBSCRIBER', 'dave_w started following you', FALSE, CURRENT_TIMESTAMP - INTERVAL '14 days'),
(6, 15, NULL, 'NEW_SUBSCRIBER', 'alex_w started following you', TRUE, CURRENT_TIMESTAMP - INTERVAL '100 days'),
(6, 26, NULL, 'NEW_SUBSCRIBER', 'avery_k started following you', FALSE, CURRENT_TIMESTAMP - INTERVAL '17 days'),
(8, 11, NULL, 'NEW_SUBSCRIBER', 'lucas_r started following you', TRUE, CURRENT_TIMESTAMP - INTERVAL '87 days'),
(8, 12, NULL, 'NEW_SUBSCRIBER', 'mia_codes started following you', FALSE, CURRENT_TIMESTAMP - INTERVAL '71 days'),
(8, 16, NULL, 'NEW_SUBSCRIBER', 'amelia_m started following you', FALSE, CURRENT_TIMESTAMP - INTERVAL '52 days'),
(8, 20, NULL, 'NEW_SUBSCRIBER', 'harper_c started following you', FALSE, CURRENT_TIMESTAMP - INTERVAL '32 days'),
(8, 22, NULL, 'NEW_SUBSCRIBER', 'abby_w started following you', FALSE, CURRENT_TIMESTAMP - INTERVAL '26 days'),
(8, 25, NULL, 'NEW_SUBSCRIBER', 'jack_y started following you', FALSE, CURRENT_TIMESTAMP - INTERVAL '18 days'),
(8, 28, NULL, 'NEW_SUBSCRIBER', 'ella_l started following you', FALSE, CURRENT_TIMESTAMP - INTERVAL '6 days'),
(8, 30, NULL, 'NEW_SUBSCRIBER', 'sofia_s started following you', FALSE, CURRENT_TIMESTAMP - INTERVAL '3 days');

-- NEW_POST notifications (when someone you follow posts)
-- Note: Assuming posts table has sequential IDs starting from 1

-- Sarah's followers get notified about her posts
INSERT INTO notifications (reciever_id, sender_id, post_id, type, message, is_read, created_at) VALUES
(3, 2, 33, 'NEW_POST', 'sarah_j published a new post: Just deployed my first Spring Boot app!', TRUE, CURRENT_TIMESTAMP - INTERVAL '145 days'),
(4, 2, 33, 'NEW_POST', 'sarah_j published a new post: Just deployed my first Spring Boot app!', TRUE, CURRENT_TIMESTAMP - INTERVAL '145 days'),
(5, 2, 33, 'NEW_POST', 'sarah_j published a new post: Just deployed my first Spring Boot app!', TRUE, CURRENT_TIMESTAMP - INTERVAL '145 days'),

(3, 2, 34, 'NEW_POST', 'sarah_j published a new post: Understanding Spring Security', TRUE, CURRENT_TIMESTAMP - INTERVAL '100 days'),
(4, 2, 34, 'NEW_POST', 'sarah_j published a new post: Understanding Spring Security', TRUE, CURRENT_TIMESTAMP - INTERVAL '100 days'),
(6, 2, 34, 'NEW_POST', 'sarah_j published a new post: Understanding Spring Security', TRUE, CURRENT_TIMESTAMP - INTERVAL '100 days'),
(8, 2, 34, 'NEW_POST', 'sarah_j published a new post: Understanding Spring Security', TRUE, CURRENT_TIMESTAMP - INTERVAL '100 days'),
(11, 2, 34, 'NEW_POST', 'sarah_j published a new post: Understanding Spring Security', TRUE, CURRENT_TIMESTAMP - INTERVAL '100 days'),

(3, 2, 35, 'NEW_POST', 'sarah_j published a new post: Coffee and Code ☕', FALSE, CURRENT_TIMESTAMP - INTERVAL '15 days'),
(4, 2, 35, 'NEW_POST', 'sarah_j published a new post: Coffee and Code ☕', FALSE, CURRENT_TIMESTAMP - INTERVAL '15 days'),
(6, 2, 35, 'NEW_POST', 'sarah_j published a new post: Coffee and Code ☕', FALSE, CURRENT_TIMESTAMP - INTERVAL '15 days'),
(8, 2, 35, 'NEW_POST', 'sarah_j published a new post: Coffee and Code ☕', FALSE, CURRENT_TIMESTAMP - INTERVAL '15 days'),
(11, 2, 35, 'NEW_POST', 'sarah_j published a new post: Coffee and Code ☕', FALSE, CURRENT_TIMESTAMP - INTERVAL '15 days'),
(12, 2, 35, 'NEW_POST', 'sarah_j published a new post: Coffee and Code ☕', FALSE, CURRENT_TIMESTAMP - INTERVAL '15 days'),
(15, 2, 35, 'NEW_POST', 'sarah_j published a new post: Coffee and Code ☕', FALSE, CURRENT_TIMESTAMP - INTERVAL '15 days'),
(18, 2, 35, 'NEW_POST', 'sarah_j published a new post: Coffee and Code ☕', FALSE, CURRENT_TIMESTAMP - INTERVAL '15 days');

-- Mike's followers get notified
INSERT INTO notifications (reciever_id, sender_id, post_id, type, message, is_read, created_at) VALUES
(2, 3, 36, 'NEW_POST', 'mike_chen published a new post: Learning REST API Best Practices', TRUE, CURRENT_TIMESTAMP - INTERVAL '135 days'),
(4, 3, 36, 'NEW_POST', 'mike_chen published a new post: Learning REST API Best Practices', TRUE, CURRENT_TIMESTAMP - INTERVAL '135 days'),

(2, 3, 37, 'NEW_POST', 'mike_chen published a new post: Database Optimization Journey', TRUE, CURRENT_TIMESTAMP - INTERVAL '80 days'),
(4, 3, 37, 'NEW_POST', 'mike_chen published a new post: Database Optimization Journey', TRUE, CURRENT_TIMESTAMP - INTERVAL '80 days'),
(7, 3, 37, 'NEW_POST', 'mike_chen published a new post: Database Optimization Journey', TRUE, CURRENT_TIMESTAMP - INTERVAL '80 days'),
(9, 3, 37, 'NEW_POST', 'mike_chen published a new post: Database Optimization Journey', TRUE, CURRENT_TIMESTAMP - INTERVAL '80 days'),

(2, 3, 38, 'NEW_POST', 'mike_chen published a new post: Started a new side project', FALSE, CURRENT_TIMESTAMP - INTERVAL '20 days'),
(4, 3, 38, 'NEW_POST', 'mike_chen published a new post: Started a new side project', FALSE, CURRENT_TIMESTAMP - INTERVAL '20 days'),
(7, 3, 38, 'NEW_POST', 'mike_chen published a new post: Started a new side project', FALSE, CURRENT_TIMESTAMP - INTERVAL '20 days'),
(9, 3, 38, 'NEW_POST', 'mike_chen published a new post: Started a new side project', FALSE, CURRENT_TIMESTAMP - INTERVAL '20 days'),
(11, 3, 38, 'NEW_POST', 'mike_chen published a new post: Started a new side project', FALSE, CURRENT_TIMESTAMP - INTERVAL '20 days'),
(13, 3, 38, 'NEW_POST', 'mike_chen published a new post: Started a new side project', FALSE, CURRENT_TIMESTAMP - INTERVAL '20 days');

-- Emma's followers get notified
INSERT INTO notifications (reciever_id, sender_id, post_id, type, message, is_read, created_at) VALUES
(2, 4, 39, 'NEW_POST', 'emma_codes published a new post: Angular Reactive Forms are Amazing', TRUE, CURRENT_TIMESTAMP - INTERVAL '125 days'),
(3, 4, 39, 'NEW_POST', 'emma_codes published a new post: Angular Reactive Forms are Amazing', TRUE, CURRENT_TIMESTAMP - INTERVAL '125 days'),

(2, 4, 40, 'NEW_POST', 'emma_codes published a new post: CSS Grid vs Flexbox', TRUE, CURRENT_TIMESTAMP - INTERVAL '70 days'),
(3, 4, 40, 'NEW_POST', 'emma_codes published a new post: CSS Grid vs Flexbox', TRUE, CURRENT_TIMESTAMP - INTERVAL '70 days'),
(7, 4, 40, 'NEW_POST', 'emma_codes published a new post: CSS Grid vs Flexbox', TRUE, CURRENT_TIMESTAMP - INTERVAL '70 days'),
(10, 4, 40, 'NEW_POST', 'emma_codes published a new post: CSS Grid vs Flexbox', TRUE, CURRENT_TIMESTAMP - INTERVAL '70 days'),
(12, 4, 40, 'NEW_POST', 'emma_codes published a new post: CSS Grid vs Flexbox', FALSE, CURRENT_TIMESTAMP - INTERVAL '70 days'),

(2, 4, 41, 'NEW_POST', 'emma_codes published a new post: Finally understanding RxJS', FALSE, CURRENT_TIMESTAMP - INTERVAL '12 days'),
(3, 4, 41, 'NEW_POST', 'emma_codes published a new post: Finally understanding RxJS', FALSE, CURRENT_TIMESTAMP - INTERVAL '12 days'),
(7, 4, 41, 'NEW_POST', 'emma_codes published a new post: Finally understanding RxJS', FALSE, CURRENT_TIMESTAMP - INTERVAL '12 days'),
(10, 4, 41, 'NEW_POST', 'emma_codes published a new post: Finally understanding RxJS', FALSE, CURRENT_TIMESTAMP - INTERVAL '12 days'),
(12, 4, 41, 'NEW_POST', 'emma_codes published a new post: Finally understanding RxJS', FALSE, CURRENT_TIMESTAMP - INTERVAL '12 days'),
(14, 4, 41, 'NEW_POST', 'emma_codes published a new post: Finally understanding RxJS', FALSE, CURRENT_TIMESTAMP - INTERVAL '12 days');

-- Lucas's followers get notified (popular backend architect)
INSERT INTO notifications (reciever_id, sender_id, post_id, type, message, is_read, created_at) VALUES
(5, 11, 55, 'NEW_POST', 'lucas_r published a new post: Microservices Architecture', TRUE, CURRENT_TIMESTAMP - INTERVAL '78 days'),
(8, 11, 55, 'NEW_POST', 'lucas_r published a new post: Microservices Architecture', TRUE, CURRENT_TIMESTAMP - INTERVAL '78 days'),
(12, 11, 55, 'NEW_POST', 'lucas_r published a new post: Microservices Architecture', FALSE, CURRENT_TIMESTAMP - INTERVAL '78 days'),

(5, 11, 56, 'NEW_POST', 'lucas_r published a new post: Event-Driven Architecture', FALSE, CURRENT_TIMESTAMP - INTERVAL '18 days'),
(8, 11, 56, 'NEW_POST', 'lucas_r published a new post: Event-Driven Architecture', FALSE, CURRENT_TIMESTAMP - INTERVAL '18 days'),
(12, 11, 56, 'NEW_POST', 'lucas_r published a new post: Event-Driven Architecture', FALSE, CURRENT_TIMESTAMP - INTERVAL '18 days'),
(15, 11, 56, 'NEW_POST', 'lucas_r published a new post: Event-Driven Architecture', FALSE, CURRENT_TIMESTAMP - INTERVAL '18 days'),
(19, 11, 56, 'NEW_POST', 'lucas_r published a new post: Event-Driven Architecture', FALSE, CURRENT_TIMESTAMP - INTERVAL '18 days'),
(21, 11, 56, 'NEW_POST', 'lucas_r published a new post: Event-Driven Architecture', FALSE, CURRENT_TIMESTAMP - INTERVAL '18 days');

-- Sophia's recent post notifications
INSERT INTO notifications (reciever_id, sender_id, post_id, type, message, is_read, created_at) VALUES
(2, 8, 50, 'NEW_POST', 'sophia_dev published a new post: Debugging Tips', FALSE, CURRENT_TIMESTAMP - INTERVAL '5 days'),
(11, 8, 50, 'NEW_POST', 'sophia_dev published a new post: Debugging Tips', FALSE, CURRENT_TIMESTAMP - INTERVAL '5 days'),
(12, 8, 50, 'NEW_POST', 'sophia_dev published a new post: Debugging Tips', FALSE, CURRENT_TIMESTAMP - INTERVAL '5 days'),
(16, 8, 50, 'NEW_POST', 'sophia_dev published a new post: Debugging Tips', FALSE, CURRENT_TIMESTAMP - INTERVAL '5 days'),
(20, 8, 50, 'NEW_POST', 'sophia_dev published a new post: Debugging Tips', FALSE, CURRENT_TIMESTAMP - INTERVAL '5 days'),
(22, 8, 50, 'NEW_POST', 'sophia_dev published a new post: Debugging Tips', FALSE, CURRENT_TIMESTAMP - INTERVAL '5 days'),
(25, 8, 50, 'NEW_POST', 'sophia_dev published a new post: Debugging Tips', FALSE, CURRENT_TIMESTAMP - INTERVAL '5 days'),
(26, 8, 50, 'NEW_POST', 'sophia_dev published a new post: Debugging Tips', FALSE, CURRENT_TIMESTAMP - INTERVAL '5 days');

-- Recent notifications from newer users
INSERT INTO notifications (reciever_id, sender_id, post_id, type, message, is_read, created_at) VALUES
(2, 28, 71, 'NEW_POST', 'ella_l published a new post: Bootcamp Graduate', FALSE, CURRENT_TIMESTAMP - INTERVAL '4 days'),
(3, 28, 71, 'NEW_POST', 'ella_l published a new post: Bootcamp Graduate', FALSE, CURRENT_TIMESTAMP - INTERVAL '4 days'),
(4, 28, 71, 'NEW_POST', 'ella_l published a new post: Bootcamp Graduate', FALSE, CURRENT_TIMESTAMP - INTERVAL '4 days'),
(12, 28, 71, 'NEW_POST', 'ella_l published a new post: Bootcamp Graduate', FALSE, CURRENT_TIMESTAMP - INTERVAL '4 days');

INSERT INTO notifications (reciever_id, sender_id, post_id, type, message, is_read, created_at) VALUES
(2, 30, 72, 'NEW_POST', 'sofia_s published a new post: Working Remotely from Bali', FALSE, CURRENT_TIMESTAMP - INTERVAL '2 days'),
(3, 30, 72, 'NEW_POST', 'sofia_s published a new post: Working Remotely from Bali', FALSE, CURRENT_TIMESTAMP - INTERVAL '2 days'),
(4, 30, 72, 'NEW_POST', 'sofia_s published a new post: Working Remotely from Bali', FALSE, CURRENT_TIMESTAMP - INTERVAL '2 days'),
(8, 30, 72, 'NEW_POST', 'sofia_s published a new post: Working Remotely from Bali', FALSE, CURRENT_TIMESTAMP - INTERVAL '2 days'),
(11, 30, 72, 'NEW_POST', 'sofia_s published a new post: Working Remotely from Bali', FALSE, CURRENT_TIMESTAMP - INTERVAL '2 days'),
(12, 30, 72, 'NEW_POST', 'sofia_s published a new post: Working Remotely from Bali', FALSE, CURRENT_TIMESTAMP - INTERVAL '2 days'),
(28, 30, 72, 'NEW_POST', 'sofia_s published a new post: Working Remotely from Bali', FALSE, CURRENT_TIMESTAMP - INTERVAL '2 days');