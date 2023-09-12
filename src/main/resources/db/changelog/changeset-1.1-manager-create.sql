CREATE TABLE IF NOT EXISTS manager
(
    id             BIGINT NOT NULL UNIQUE AUTO_INCREMENT PRIMARY KEY,
    first_name     varchar(50),
    last_name      varchar(50),
    manager_status varchar(50),
    description    varchar(255),
    created_at     datetime
);
