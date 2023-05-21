CREATE TABLE IF NOT EXISTS Netology_diplom.users
(
    id       BIGINT AUTO_INCREMENT,
    username VARCHAR(100) UNIQUE,
    password VARCHAR(255),
    PRIMARY KEY (id)
);