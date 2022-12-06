CREATE TABLE files
(
    id             INT AUTO_INCREMENT PRIMARY KEY,
    name           VARCHAR(128) NOT NULL,
    size           BIGINT       NOT NULL,
    generated_name VARCHAR(128) NOT NULL UNIQUE
)