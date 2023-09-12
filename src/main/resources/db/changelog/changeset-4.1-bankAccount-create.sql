CREATE TABLE IF NOT EXISTS bank_account
(
    id             BIGINT NOT NULL UNIQUE AUTO_INCREMENT PRIMARY KEY,
    client_id      BIGINT NOT NULL,
    product_id     BIGINT NOT NULL,
    account_name   varchar(100),
    account_number varchar(26),
    account_type   varchar(50),
    status         varchar(50),
    balance        numeric(15, 2),
    currency_code  varchar(3),
    created_at     datetime,
    updated_at     datetime,
    FOREIGN KEY (client_id) REFERENCES client (id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
    );