CREATE SCHEMA IF NOT EXISTS file_storage;

USE file_storage;

CREATE TABLE users
(
    id       INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(128) UNIQUE NOT NULL,
    email    VARCHAR(128) UNIQUE NOT NULL,
    password VARCHAR(128) UNIQUE NOT NULL,
    role     VARCHAR(32)         NOT NULL
);