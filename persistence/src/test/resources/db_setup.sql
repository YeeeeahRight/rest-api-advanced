create table tags
(
    id   BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(60) NOT NULL UNIQUE
);

create table certificates
(
    id               BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    name             varchar(80)  NOT NULL UNIQUE,
    description      VARCHAR(200) NOT NULL,
    price            DECIMAL(10, 2),
    create_date      TIMESTAMP DEFAULT NOW(),
    last_update_date TIMESTAMP DEFAULT NOW(),
    duration         INT          NOT NULL
);

create table certificates_tags
(
    id             BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    certificate_id BIGINT UNSIGNED NOT NULL,
    tag_id         BIGINT UNSIGNED NOT NULL,
    FOREIGN KEY (certificate_id) REFERENCES certificates (id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (tag_id) REFERENCES tags (id)
        ON DELETE CASCADE ON UPDATE CASCADE
);