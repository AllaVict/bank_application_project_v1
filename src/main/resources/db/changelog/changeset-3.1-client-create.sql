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
