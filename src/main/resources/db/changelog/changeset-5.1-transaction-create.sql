CREATE TABLE IF NOT EXISTS transaction
(
    id                  BIGINT NOT NULL UNIQUE AUTO_INCREMENT PRIMARY KEY,
    account_id          BIGINT,
    sender              varchar(100),
    source_account      varchar(26),
    beneficiary         varchar(100),
    destination_account varchar(26),
    transaction_amount  numeric(25, 2),
    description         varchar(150),
    interest_rate       numeric(6, 4),
    transaction_type    varchar(70),
    transaction_status  varchar(70),
    transaction_code    varchar(70),
    created_at          datetime,
    updated_at          datetime,
    transaction_date    datetime,
    effective_date      datetime,
    FOREIGN KEY (account_id) REFERENCES bank_account (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);