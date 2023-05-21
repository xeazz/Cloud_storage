CREATE TABLE IF NOT EXISTS Netology_diplom.files
(
#     id          BIGINT AUTO_INCREMENT,
    file_name   VARCHAR(256) NOT NULL,
    file_data   MEDIUMBLOB   NOT NULL,
    size        BIGINT,
    upload_date DATETIME,
    user_id     BIGINT,
    PRIMARY KEY (file_name),
    FOREIGN KEY (user_id) REFERENCES Netology_diplom.users (id)
);