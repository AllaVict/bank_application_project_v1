CREATE SCHEMA bank_project_database;

--# Table Client ( Bank's Clients table )
CREATE TABLE client(
    id BIGINT NOT NULL UNIQUE AUTO_INCREMENT PRIMARY KEY,
    manager_id BIGINT NOT NULL,
    status varchar(10),
    tax_code varchar(10),
    first_name varchar(50),
    last_name varchar(50),
    email varchar(60),
    address varchar(80),
    phone varchar(20),
    created_at datetime,
    updated_at datetime,
        FOREIGN KEY (manager_id) REFERENCES manager (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
 );


INSERT INTO client
(id, manager_id, status, tax_code, first_name, last_name, email, address, phone, created_at,updated_at)
VALUES
    (1,   2,  2, '5555555551', 'Alina', 'Smith', 'alina@gmail.com','Warszawa' ,'+48777333111', '2023-07-01','2023-07-01'),
    (2,   2,  1, '1111111113', 'Tom', 'Jakson', 'tom@gmail.com', 'Krakow', '+48999555666','2023-07-11', '2023-07-11'),
    (3,   2,  1, '2222222224', 'Jaklin', 'Ford', 'jaklin@gmail.com', 'Gdansk', '+48888333777','2023-07-11', '2023-07-11'),
    (4,   3,  1, '3333333335', 'Vens', 'Disel', 'vaens@gmail.com', 'Wroclaw', '+48444777222','2023-07-11', '2023-07-11'),
    (5,   3,  1, '8888888886', 'Kerry', 'Truemen', 'kerry@gmail.com', 'Poznan', '+485557333111','2023-07-11', '2023-07-11'),
    (6,   3,  1, '4444444447', 'Sara', 'Grinber', 'sarag@gmail.com', 'Wroclaw','+48999444222', '2023-07-01', '2023-07-11'),
    (7,   4,  1, '7777777778', 'Tina', 'Kanson', 'tinak@gmail.com', 'Poznan','+48333555666', '2023-07-11', '2023-07-11'),
    (8,   4,  1, '6666666669', 'Gretta', 'Pirson', 'grettap@gmail.com', 'Warszawa','+48111333777', '2023-07-11', '2023-07-11'),
    (9,   4,  2, '9999999990', 'Bary', 'Wilson', 'baryw@gmail.com', 'Krakow','+48999777222', '2023-07-11', '2023-07-11'),
    (10,   4,  1, '5555555552', 'Robby', 'Wolf', 'roobyw@gmail.com', 'Katowice','+48666444222', '2023-07-11', '2023-07-11'),
    (11,   5,  1, '7777777778', 'Tina', 'Kanson', 'tinak@gmail.com', 'Warszawa', '+48111555666', '2023-07-11', '2023-07-11'),
    (12,   5,  1, '6666666669', 'Gretta', 'Pirson', 'grettap@gmail.com', 'Lublin', '+4888444222','2023-07-11', '2023-07-11'),
    (13,   5,  2, '9999999990', 'Bary', 'Wilson', 'baryw@gmail.com', 'Warszawa', '+48222333777','2023-07-11', '2023-07-11'),
    (14,   5,  1, '5555555552', 'Robby', 'Wolf', 'roobyw@gmail.com', 'Bydgoszcz', '+48999777222','2023-07-11', '2023-07-11');


# --Table Account (Bank's accounts table)
CREATE TABLE bank_account(
        id BIGINT NOT NULL UNIQUE AUTO_INCREMENT PRIMARY KEY,
        client_id BIGINT NOT NULL,
        account_name varchar(100),
        account_number varchar(26),
        account_type varchar(50),
        status varchar(50),
        balance numeric(15,2),
        currency_code varchar(3),
        created_at datetime,
        updated_at datetime,
        FOREIGN KEY (client_id) REFERENCES client (id)
            ON DELETE CASCADE
            ON UPDATE CASCADE
);

# -- TABLE product
# -- TABLE transaction

INSERT INTO bank_account
(id, client_id, account_name, account_type, account_number, status, balance, currency_code, created_at)
VALUES
 (1,1,'current_per_PLN','PERSONAL_PLN',11000022220000333300002222,'ACTIVATED', 750,'PLN','2023-07-11'),
 (2,1,'current_per_EUR','PERSONAL_EUR',55000044440000333300004444,'ACTIVATED', 1000,'EUR','2023-07-11'),
 (3,1,'current_bus_PLN','BUSINESS_PLN',55000044440000333300004444,'ACTIVATED', 1000,'PLN','2023-07-11'),
(4,1,'credit_USD' ,'CREDIT_USD',44000055550000333300005555, 'ACTIVATED', 30000,'USD','2023-07-11'),
 (5,1,'deposit_EUR','DEPOSIT_EUR',66000088880000333300008888,'ACTIVATED', 1100,  'EUR','2023-07-11'),

 (6,2,'current_per_PLN','PERSONAL_PLN',77000077770000333300007777,'ACTIVATED',  65,   'PLN','2023-07-11'),
(7,2,'current_per_PLN','PERSONAL_PLN',88000099990000333300009999,'ACTIVATED', 7000,  'PLN','2023-07-11'),
(8,3,'current_bus_PLN','BUSINESS_PLN',22000011110000333300001111,'ACTIVATED', 25005, 'PLN','2023-07-11'),
(9,4,'current_bus_PLN','BUSINESS_PLN',99000033330000333300003333,'ACTIVATED',8900,   'PLN','2023-07-11'),

(10,5,'current_bus_PLN','BUSINESS_PLN',11000066660000666600006666, 'ACTIVATED',5500, 'PLN' ,'2023-07-11'),
(11,5,'current_bus_USD','BUSINESS_USD',33000022220000333300002222,'ACTIVATED', 7700,  'USD','2023-07-11'),
(12,5,'current_bus_EUR','BUSINESS_EUR',33000022220000333300002222,'CHECKING', 7700,  'EUR','2023-07-11'),
(13,5,'deposit_EUR','DEPOSIT_EUR',33000022220000333300002222,'BLOCKED', 7700,  'EUR','2023-07-11'),
(14,5,'credit_PLN','CREDIT_PLN',33000022220000333300002222,'CLOSED', 7700,  'PLN','2023-07-11');

#-- Table Agreement (Bank's - Client's Agreement table)
CREATE TABLE transaction(
    id BIGINT NOT NULL UNIQUE AUTO_INCREMENT PRIMARY KEY,
    account_id BIGINT,
    product_id BIGINT,
    sender varchar(100),
    source_account BIGINT,
    beneficiary varchar(100),
    destination_account BIGINT,
    transaction_amount numeric(25,2),
    description varchar(150),
    interest_rate numeric(6,4),
    transaction_type varchar(70),
    transaction_status varchar(70),
    transaction_code varchar(70),
    transaction_date datetime,
    effective_date datetime

);

INSERT INTO transaction
(id, account_id, product_id, sender, source_account,
 beneficiary, destination_account,
 transaction_amount, description,
 interest_rate, transaction_type,transaction_status, transaction_date)
VALUES
(1,1,1,  'Garry Bred',11000022220000333300002222,
 'Kerry Truemen',11000066660000666600006666,
 50.00, 'payment for rent',
 0 , 'INTERNAL',  'DRAFT_INVALID', '2023-07-11' ),
    (2,1,1,  'Garry Bred',11000022220000333300002222,
     'Kerry Truemen',11000066660000666600006666,
     50.00, 'payment for rent',
     0 , 'INTERNAL',  'DRAFT_INVALID', '2023-07-12' ),
(3,1,1,  'Garry Bred',11000022220000333300002222,
    'Kerry Truemen',11000066660000666600006666,
    50.00, 'payment for rent',
    0 , 'INTERNAL',  'DRAFT_INVALID', '2023-07-15' );



# -- Table Manager (Bank's managers )
CREATE TABLE manager(
        id BIGINT NOT NULL UNIQUE AUTO_INCREMENT PRIMARY KEY,
        first_name varchar(50),
        last_name varchar(50),
        manager_status varchar(50),
        description varchar(255),
        created_at datetime
);


# --Table Product ( Sets of Bank's available Products)
CREATE TABLE product(
    id BIGINT NOT NULL UNIQUE AUTO_INCREMENT PRIMARY KEY,
    manager_id BIGINT,
    product_name varchar(70),
    product_status varchar(70),
    currency_code varchar(3),
    interest_rate numeric(6,4),
    credit_limit numeric(15,2),
    created_at datetime,
    updated_at datetime,
    FOREIGN KEY (manager_id) REFERENCES manager (id)
    ON DELETE CASCADE
    ON UPDATE CASCADE

);

INSERT INTO product
(id, manager_id, product_name, product_status, currency_code, interest_rate, credit_limit, created_at)
VALUES
    (1, 1, 'current_personal_PLN', 'ACTIVE', 'PLN', 0.0000, 0.00 ,'2023-07-11'),
    (2, 1, 'current_personal_USD', 'ACTIVE', 'USD', 0.0000, 0.00 ,'2023-07-11'),
    (3, 1, 'current_personal_EUR', 'ACTIVE', 'EUR', 0.0000, 0.00 ,'2023-07-11'),

    (4, 1, 'deposit_personal_PLN', 'ACTIVE', 'PLN', 3.5000, 0.00,'2023-07-11'),
    (5, 1, 'deposit_personal_USD', 'ACTIVE', 'USD', 1.1000, 0.00 ,'2023-07-11'),
    (6, 1, 'deposit_personal_EUR', 'ACTIVE', 'EUR', 0.8000, 0.00,'2023-07-11'),

    (7, 1, 'credit_personal_PLN', 'ACTIVE', 'PLN', 5.0000, 1000000.00,'2023-07-11'),
    (8, 1, 'credit_personal_USD', 'ACTIVE', 'USD', 2.0000, 1000000.00 ,'2023-07-11'),
    (9, 1, 'credit_personal_EUR', 'ACTIVE', 'EUR', 1.8000, 1000000.00,'2023-07-11'),

    (10, 1, 'current_business_PLN', 'ACTIVE', 'PLN', 0.0000, 0.00,'2023-07-11'),
    (11, 1, 'current_business_USD', 'ACTIVE', 'USD', 0.0000, 0.00 ,'2023-07-11'),
    (12, 1, 'current_business_EUR', 'ACTIVE', 'EUR', 0.0000, 0.00,'2023-07-11'),

    (13, 1, 'deposit_business_PLN', 'ACTIVE', 'PLN', 4.5000, 0.00,'2023-07-11'),
    (14, 1, 'deposit_business_USD', 'ACTIVE', 'USD', 1.9000, 0.00 ,'2023-07-11'),
    (15, 1, 'deposit_business_EUR', 'ACTIVE', 'EUR', 1.7000, 0.00,'2023-07-11'),

    (16, 1, 'credit_business_PLN', 'ACTIVE', 'PLN', 5.50, 1000000.00,'2023-07-11'),
    (17, 1, 'credit_business_USD', 'ACTIVE', 'USD', 2.20, 1000000.00 ,'2023-07-11'),
    (18, 1, 'credit_business_EUR', 'ACTIVE', 'EUR', 2.00, 1000000.00,'2023-07-11');


-- TABLE manager status   1 -ADMIN  2 -USER
INSERT INTO manager
(id, first_name, last_name, manager_status, description, created_at)
VALUES
    (1,  'Nik',     'Bred',   'ADMIN', 'ADMIN', '2023-07-11'),
    (2,  'Barbara', 'Nikson', 'ACTIVE', 'USER', '2022-07-11'),
    (3,  'Jessika', 'Parker', 'ACTIVE', 'USER', '2023-07-09'),
    (4,  'Sandra',  'Grade',  'ACTIVE', 'USER', '2023-07-08'),
    (5,  'Harry',   'Frend', 'ACTIVE', 'USER', '2023-07-11');


ALTER TABLE manager MODIFY id INT AUTO_INCREMENT;
ALTER TABLE transaction MODIFY id INT AUTO_INCREMENT;
ALTER TABLE product MODIFY id INT AUTO_INCREMENT;
ALTER TABLE client MODIFY id INT AUTO_INCREMENT;
ALTER TABLE bank_account MODIFY id INT AUTO_INCREMENT;

ALTER TABLE manager MODIFY id BIGINT AUTO_INCREMENT;
ALTER TABLE transaction MODIFY id BIGINT AUTO_INCREMENT;
ALTER TABLE product MODIFY id BIGINT AUTO_INCREMENT;
ALTER TABLE client MODIFY id BIGINT AUTO_INCREMENT;
ALTER TABLE bank_account MODIFY id BIGINT AUTO_INCREMENT;


ALTER TABLE product MODIFY manager_id BIGINT;
ALTER TABLE product
    ADD CONSTRAINT FOREIGN KEY (manager_id)
        REFERENCES manager (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
;


ALTER TABLE client MODIFY manager_id BIGINT  NOT NULL;
ALTER TABLE client
    ADD CONSTRAINT FOREIGN KEY (manager_id)
        REFERENCES manager (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
;

ALTER TABLE bank_account MODIFY client_id BIGINT  NOT NULL;
ALTER TABLE bank_account
    ADD CONSTRAINT FOREIGN KEY (client_id)
        REFERENCES client (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
;

