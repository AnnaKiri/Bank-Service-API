INSERT INTO users (username, password, phone, email, birth_date, full_name)
VALUES ('user1', '{noop}password1', '+1234567890', 'user1@example.com', '1990-01-01', 'User One'),
       ('user2', '{noop}password2', '+1234567891', 'user2@example.com', '1991-01-01', 'User Two'),
       ('user3', '{noop}password3', '+1234567892', 'user3@example.com', '1992-01-01', 'User Three');

INSERT INTO transfers (sender_id, receiver_id, amount, status)
VALUES ((SELECT id FROM users WHERE username = 'user1'), (SELECT id FROM users WHERE username = 'user2'), 100.0,
        'SUCCESS'),
       ((SELECT id FROM users WHERE username = 'user3'), (SELECT id FROM users WHERE username = 'user1'), 200.0,
        'SUCCESS'),
       ((SELECT id FROM users WHERE username = 'user2'), (SELECT id FROM users WHERE username = 'user3'), 300.0,
        'SUCCESS'),
       ((SELECT id FROM users WHERE username = 'user2'), (SELECT id FROM users WHERE username = 'user2'), 100.0,
        'FAIL'),
       ((SELECT id FROM users WHERE username = 'user1'), (SELECT id FROM users WHERE username = 'user2'), 100000.0,
        'FAIL'),
       ((SELECT id FROM users WHERE username = 'user3'), (SELECT id FROM users WHERE username = 'user1'), -100.0,
        'FAIL');

INSERT INTO bank_accounts (balance, max_balance_with_interest, user_id)
VALUES (1100.0, 2070.0, (SELECT id FROM users WHERE username = 'user1')),
       (1800.0, 4140.0, (SELECT id FROM users WHERE username = 'user2')),
       (3100.0, 6210.0, (SELECT id FROM users WHERE username = 'user3'));
