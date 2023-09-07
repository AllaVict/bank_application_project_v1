CREATE TABLE IF NOT EXISTS manager
(
    id             BIGINT NOT NULL UNIQUE AUTO_INCREMENT PRIMARY KEY,
    first_name     varchar(50),
    last_name      varchar(50),
    manager_status varchar(50),
    description    varchar(255),
    created_at     datetime
);

CREATE TABLE IF NOT EXISTS product
(
    id             BIGINT NOT NULL UNIQUE AUTO_INCREMENT PRIMARY KEY,
    manager_id     BIGINT,
    product_name   varchar(70),
    product_status varchar(70),
    currency_code  varchar(3),
    interest_rate  numeric(6, 4),
    credit_limit   numeric(15, 2),
    created_at     datetime,
    updated_at     datetime,
    FOREIGN KEY (manager_id) REFERENCES manager (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE

);

CREATE TABLE IF NOT EXISTS client
(
    id         BIGINT NOT NULL UNIQUE AUTO_INCREMENT PRIMARY KEY,
    manager_id BIGINT NOT NULL,
    status     varchar(10),
    tax_code   varchar(10),
    first_name varchar(50),
    last_name  varchar(50),
    email      varchar(60),
    address    varchar(80),
    phone      varchar(20),
    created_at datetime,
    updated_at datetime,
    FOREIGN KEY (manager_id) REFERENCES manager (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);



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

CREATE TABLE IF NOT EXISTS login_entity
(
    id         BIGINT NOT NULL UNIQUE AUTO_INCREMENT PRIMARY KEY,
    username   varchar(50),
    password   varchar(100),
    role       varchar(10),
    client_id  BIGINT,
    created_at datetime,
    updated_at datetime
);
