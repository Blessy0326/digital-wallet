CREATE TABLE users (
    id         BIGSERIAL PRIMARY KEY,
    full_name  VARCHAR(100) NOT NULL,
    email      VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE accounts (
    id             BIGSERIAL PRIMARY KEY,
    account_number VARCHAR(40) NOT NULL UNIQUE,
    user_id        BIGINT NOT NULL REFERENCES users (id),
    balance        NUMERIC(19, 2) NOT NULL,
    currency       VARCHAR(3) NOT NULL,
    version        BIGINT,
    created_at     TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE transactions (
    id                           BIGSERIAL PRIMARY KEY,
    account_id                   BIGINT NOT NULL REFERENCES accounts (id),
    type                         VARCHAR(20) NOT NULL,
    amount                       NUMERIC(19, 2) NOT NULL,
    balance_after                NUMERIC(19, 2) NOT NULL,
    description                  VARCHAR(255),
    counterparty_account_number  VARCHAR(40),
    created_at                   TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX idx_accounts_user_id ON accounts (user_id);
CREATE INDEX idx_transactions_account_id_created_at ON transactions (account_id, created_at DESC);