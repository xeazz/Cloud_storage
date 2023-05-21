CREATE TABLE IF NOT EXISTS Netology_diplom.user_roles
(
    user_id BIGINT,
    role_id BIGINT,
    PRIMARY KEY (user_id, role_id)
);