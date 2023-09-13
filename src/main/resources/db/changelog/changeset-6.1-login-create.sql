CREATE TABLE IF NOT EXISTS login_entity
(
    id         BIGINT NOT NULL UNIQUE AUTO_INCREMENT PRIMARY KEY,
    username   varchar(50),
    password   varchar(100),
    role       varchar(10),
    client_id  BIGINT,
    created_at datetime,
    updated_at datetime,
    FOREIGN KEY (client_id) REFERENCES client (id)
            ON DELETE CASCADE
            ON UPDATE CASCADE
    );
