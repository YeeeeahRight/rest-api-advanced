INSERT INTO certificates (name, description, price, create_date, last_update_date, duration)
VALUES ('certificate 1', 'description 1', 1.1, '2020-01-01 01:11:11', '2021-01-01 01:22:11', 1),
       ('certificate 2', 'description 2', 2.2, '2020-02-02 02:22:22', '2021-02-02 02:33:22', 2),
       ('certificate 3', 'description 3', 3.3, '2020-03-03 03:33:33', '2021-03-03 03:44:33', 3);

INSERT INTO tags (name) VALUES ('tag 1'), ('tag 2'), ('tag 3');

INSERT INTO certificates_tags (certificate_id, tag_id) VALUES (1, 1), (1, 3), (2, 2), (3,3);