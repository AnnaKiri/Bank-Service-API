DROP TABLE IF EXISTS bank_accounts;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS transfers;

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

CREATE TABLE bank_accounts
(
    id              INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    initial_balance DOUBLE PRECISION NOT NULL,
    balance         DOUBLE PRECISION NOT NULL,
    user_id         BIGINT           NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE transfers
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

CREATE INDEX idx_sender_id ON transfers (sender_id);
CREATE INDEX idx_receiver_id ON transfers (receiver_id);