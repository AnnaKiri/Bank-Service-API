DROP TABLE IF EXISTS bank_account;
DROP TABLE IF EXISTS transfer;
DROP TABLE IF EXISTS users;

CREATE TABLE users
(
    id         INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    username   VARCHAR NOT NULL UNIQUE,
    password   VARCHAR NOT NULL,
    phone      VARCHAR UNIQUE,
    email      VARCHAR UNIQUE,
    birth_date DATE    NOT NULL,
    full_name  VARCHAR NOT NULL
);

CREATE TABLE bank_account
(
    id                        INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    balance                   DOUBLE PRECISION NOT NULL CHECK (balance >= 0),
    max_balance_with_interest DOUBLE PRECISION NOT NULL CHECK (max_balance_with_interest >= 0),
    user_id                   INTEGER          NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE transfer
(
    id          INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    sender_id   INTEGER                             NOT NULL,
    receiver_id INTEGER                             NOT NULL,
    amount      DOUBLE PRECISION                    NOT NULL,
    timestamp   TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    status      VARCHAR(20)                         NOT NULL,
    FOREIGN KEY (sender_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (receiver_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE INDEX idx_sender_id ON transfer (sender_id);
CREATE INDEX idx_receiver_id ON transfer (receiver_id);