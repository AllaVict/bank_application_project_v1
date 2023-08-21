CREATE SCHEMA bank_project_database;

ALTER TABLE manager MODIFY id INT AUTO_INCREMENT;
ALTER TABLE transaction MODIFY id INT AUTO_INCREMENT;
ALTER TABLE product MODIFY id INT AUTO_INCREMENT;
ALTER TABLE client MODIFY id INT AUTO_INCREMENT;
ALTER TABLE bank_account MODIFY id INT AUTO_INCREMENT;

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


ALTER TABLE manager DROP COLUMN product_status;

ALTER TABLE bank_account MODIFY account_number varchar(26);

# -- Table Manager (Bank's managers )
CREATE TABLE manager(
    id BIGINT NOT NULL UNIQUE AUTO_INCREMENT PRIMARY KEY, #--id key of row - unique, not null, primary key
    first_name varchar(50), # -- manager's name
    last_name varchar(50), # -- manager's surname
    manager_status varchar(50), # -- manager's status
    description varchar(255), # --description of transaction
    created_at datetime # --timestamp of row creation
);


-- TABLE manager status   1 -ADMIN  2 -USER
INSERT INTO manager
(id, first_name, last_name, manager_status, description, created_at)
VALUES
    (1,  'Nik',     'Bred',   'ADMIN', 'ADMIN', '2023-07-11'),
    (2,  'Barbara', 'Nikson', 'ACTIVE', 'USER', '2022-07-11'),
    (3,  'Jessika', 'Parker', 'ACTIVE', 'USER', '2023-07-09'),
    (4,  'Sandra',  'Grade',  'ACTIVE', 'USER', '2023-07-08'),
    (5,  'Harry',   'Frend', 'ACTIVE', 'USER', '2023-07-11');

# --Table Product ( Sets of Bank's available Products)
CREATE TABLE product(
    id BIGINT NOT NULL UNIQUE AUTO_INCREMENT PRIMARY KEY,  # --id key of row - unique, not null, primary key
    manager_id BIGINT,#  --manager id manager_id BIGINT REFERENCES manager (id)
    product_name varchar(70),#  -- product's name
    product_status varchar(70), # --product's status
    currency_code varchar(3), # --currency of product
    interest_rate numeric(6,4), # --interest rate of product
    credit_limit numeric(15,2), # --limit of credit a product ( 0 - no limit, 0 < - limit which can be used) |
    created_at datetime, # --timestamp of row creation
    updated_at datetime, # --timestamp of last update
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

    (16, 1, 'credit_business_PLN', 'ACTIVE', 'PLN', 5.50, 7000000.00,'2023-07-11'),
    (17, 1, 'credit_business_USD', 'ACTIVE', 'USD', 2.20, 7000000.00 ,'2023-07-11'),
    (18, 1, 'credit_business_EUR', 'ACTIVE', 'EUR', 2.00, 7000000.00,'2023-07-11');


--# Table Client ( Bank's Clients table )
CREATE TABLE client(
    id BIGINT NOT NULL UNIQUE AUTO_INCREMENT PRIMARY KEY,
    manager_id BIGINT NOT NULL,
    status varchar(10), #  client's status
    tax_code varchar(10),# -- client's TAX code (external ID)
    first_name varchar(50), # -- client's name
    last_name varchar(50),#  --client's surname
    email varchar(60), # -- client's e-mail
    address varchar(80), # --client's address
    phone varchar(20),# --  client's phone
    created_at datetime, # -- timestamp of row creation
    updated_at datetime, #  --timestamp of last update
        FOREIGN KEY (manager_id) REFERENCES manager (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
 );
-- --???  manager_id binary(16), -- ???? type  binary
-- -- TABLE client status- 1 -valid, 2- closed
# -- TABLE manager status- description   1 -ADMIN  2 -USER

INSERT INTO client
(id, manager_id, status, tax_code, first_name, last_name, email, address, phone, created_at,updated_at)
VALUES
    (1,   2,  'ACTIVATED', '5555555551', 'Alina', 'Smith', 'alina@gmail.com','Warszawa' ,'+48777333111', '2023-07-01','2023-07-01'),
    (2,   2, 'ACTIVATED', '1111111113', 'Tom', 'Jakson', 'tom@gmail.com', 'Krakow', '+48999555666','2023-07-11', '2023-07-11'),
    (3,   2,  'ACTIVATED', '2222222224', 'Jaklin', 'Ford', 'jaklin@gmail.com', 'Gdansk', '+48888333777','2023-07-11', '2023-07-11'),
    (4,   3,  'ACTIVATED', '3333333335', 'Vens', 'Disel', 'vaens@gmail.com', 'Wroclaw', '+48444777222','2023-07-11', '2023-07-11'),
    (5,   3,  'ACTIVATED', '8888888886', 'Kerry', 'Truemen', 'kerry@gmail.com', 'Poznan', '+485557333111','2023-07-11', '2023-07-11'),
    (6,   3,  'ACTIVATED', '4444444447', 'Sara', 'Grinber', 'sarag@gmail.com', 'Wroclaw','+48999444222', '2023-07-01', '2023-07-11'),
    (7,   4,  'ACTIVATED', '7777777778', 'Tina', 'Kanson', 'tinak@gmail.com', 'Poznan','+48333555666', '2023-07-11', '2023-07-11'),
    (8,   4, 'ACTIVATED', '6666666669', 'Gretta', 'Pirson', 'grettap@gmail.com', 'Warszawa','+48111333777', '2023-07-11', '2023-07-11'),
    (9,   4,  'CLOSED', '9999999990', 'Bary', 'Wilson', 'baryw@gmail.com', 'Krakow','+48999777222', '2023-07-11', '2023-07-11'),
    (10,   4,  'ACTIVATED', '5555555552', 'Robby', 'Wolf', 'roobyw@gmail.com', 'Katowice','+48666444222', '2023-07-11', '2023-07-11'),
    (11,   5,  'ACTIVATED', '7777777778', 'Tina', 'Kanson', 'tinak@gmail.com', 'Warszawa', '+48111555666', '2023-07-11', '2023-07-11'),
    (12,   5,  'ACTIVATED', '6666666669', 'Gretta', 'Pirson', 'grettap@gmail.com', 'Lublin', '+4888444222','2023-07-11', '2023-07-11'),
    (13,   5,   'CLOSED', '9999999990', 'Bary', 'Wilson', 'baryw@gmail.com', 'Warszawa', '+48222333777','2023-07-11', '2023-07-11'),
    (14,   5,  'ACTIVATED', '5555555552', 'Robby', 'Wolf', 'roobyw@gmail.com', 'Bydgoszcz', '+48999777222','2023-07-11', '2023-07-11');

# --Table Account (Bank's accounts table)
CREATE TABLE bank_account(
        id BIGINT NOT NULL UNIQUE AUTO_INCREMENT PRIMARY KEY, #--id binary(16) key of row - unique, not null, primary key |
        client_id BIGINT NOT NULL, # --binary(16) client id
        account_name varchar(100), # -- a name of bankAccount
        account_number varchar(26), # -- a name of bankAccount
        account_type varchar(50), # --bankAccount type
        status varchar(50), # --status of tne bankAccount
        balance numeric(15,2), # -- balance of the bankAccount in currency
        currency_code varchar(3),  # --bankAccount currency code
        created_at datetime, # -- timestamp of row creation
        updated_at datetime,  #  -- timestamp of last update
            FOREIGN KEY (client_id) REFERENCES client (id)
            ON DELETE CASCADE
            ON UPDATE CASCADE
);

# -- TABLE product
# -- TABLE transaction
INSERT INTO bank_account
(id, client_id, account_name, account_type,  currency_code, account_number, balance,status, created_at)
VALUES
    (1,1,'current_per_PLN','PERSONAL','PLN','11000022220000333300002222',750,'ACTIVATED', '2023-07-11'),
    (2,1,'current_per_EUR','PERSONAL','EUR','55000044440000333300004444',1000,'ACTIVATED', '2023-07-11'),
    (3,1,'current_bus_PLN','BUSINESS','PLN','55000044440000333300004444',1000,'ACTIVATED', '2023-07-11'),
    (4,1,'credit_USD' ,'CREDIT','USD','44000055550000333300005555', 30000,'ACTIVATED', '2023-07-11'),
    (5,1,'deposit_EUR','DEPOSIT','EUR','66000088880000333300008888', 1100,'ACTIVATED','2023-07-11'),

    (6,2,'current_per_PLN','PERSONAL','PLN','77000077770000333300007777',  65,  'ACTIVATED', '2023-07-11'),
    (7,2,'current_per_PLN','PERSONAL','PLN','88000099990000333300009999',7000, 'ACTIVATED',  '2023-07-11'),
    (8,3,'current_bus_PLN','BUSINESS','PLN','22000011110000333300001111',25005,'ACTIVATED',  '2023-07-11'),
    (9,4,'current_bus_PLN','BUSINESS', 'PLN','99000033330000333300003333',8900,  'ACTIVATED','2023-07-11'),

    (10,5,'current_bus_PLN','BUSINESS','PLN' ,'11000066660000666600006666', 5500,'ACTIVATED', '2023-07-11'),
    (11,5,'current_bus_USD','BUSINESS', 'USD','33000022220000333300002222',10000,'ACTIVATED',  '2023-07-11'),
    (12,5,'current_bus_EUR','BUSINESS','EUR','33000022220000333300002222',7700, 'CHECKING',  '2023-07-11'),
    (13,5,'deposit_EUR','DEPOSIT','EUR','33000022220000333300002222', -3000, 'BLOCKED', '2023-07-11'),
    (14,5,'credit_PLN','CREDIT','PLN','33000022220000333300002222',-2000, 'CLOSED',  '2023-07-11'),

    (15,7,'transit','TRANSIT','PLN','11000077770000555500007777',10000000,'ACTIVATED', '2023-07-11'),
    (16,7,'transit','TRANSIT','EUR','22000077770000555500007777',10000000,'ACTIVATED', '2023-07-11'),
    (17,7,'transit','TRANSIT','USD','33000077770000555500007777',10000000,'ACTIVATED', '2023-07-11');


#-- Table Agreement (Bank's - Client's Agreement table)
#--product_id BIGINT,#  -- product id (table product)
CREATE TABLE transaction(
    id BIGINT NOT NULL UNIQUE AUTO_INCREMENT PRIMARY KEY, # --id key of row - unique, not null, primary key |
    account_id BIGINT, # -- binary(16) client's bankAccount
    sender varchar(100),
    source_account varchar(26),
    beneficiary varchar(100),
    destination_account varchar(26),
    transaction_amount numeric(25,2),
    description varchar(150),
    interest_rate numeric(6,4),#  -- current interest rate of agreement
    transaction_type varchar(70),
    transaction_status varchar(70),
    transaction_code varchar(70),
    transaction_date datetime, # --timestamp of row creation
    effective_date datetime,  # -- timestamp of last update
        FOREIGN KEY (account_id) REFERENCES bank_account (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

INSERT INTO transaction
(id, account_id, sender, source_account,
 beneficiary, destination_account,
 transaction_amount, description,
 interest_rate, transaction_type,transaction_status, transaction_date)
VALUES
    (1,1,  'Garry Bred','11000022220000333300002222',
     'Kerry Truemen','11000066660000666600006666',
     50.00, 'payment for rent',
     0 , 'INTERNAL',  'DRAFT_INVALID', '2023-07-11' ),
    (2,1,  'Garry Bred','11000022220000333300002222',
     'Kerry Truemen','11000066660000666600006666',
     100.00, 'payment for rent',
     0 , 'INTERNAL',  'DRAFT_INVALID', '2023-07-12' ),
    (3,1,  'Garry Bred','11000022220000333300002222',
     'Kerry Truemen','11000066660000666600006666',
     150.00, 'payment for rent',
     0 , 'INTERNAL',  'DRAFT_INVALID', '2023-07-15' );


