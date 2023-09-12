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