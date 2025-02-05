CREATE DATABASE IF NOT EXISTS payment_db;
USE payment_db;

CREATE TABLE user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE payment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    concept VARCHAR(255),
    quantity INT,
    amount DOUBLE,
    status VARCHAR(50),
    payer_id BIGINT,
    payee_id BIGINT,
    FOREIGN KEY (payer_id) REFERENCES user(id),
    FOREIGN KEY (payee_id) REFERENCES user(id)
);
