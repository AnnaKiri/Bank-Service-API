INSERT INTO users (username, password, phone, email, birth_date, full_name)
VALUES
    ('user1', '{noop}password1', '+1234567890', 'user1@example.com', '1990-01-01', 'User One'),
    ('user2', '{noop}password2', '+1234567891', 'user2@example.com', '1991-01-01', 'User Two'),
    ('user3', '{noop}password3', '+1234567892', 'user3@example.com', '1992-01-01', 'User Three');

INSERT INTO bank_accounts (initial_balance, balance, user_id)
VALUES
    (1000.0, 1000.0, (SELECT id FROM users WHERE username = 'user1')),
    (2000.0, 2000.0, (SELECT id FROM users WHERE username = 'user2')),
    (3000.0, 3000.0, (SELECT id FROM users WHERE username = 'user3'));
