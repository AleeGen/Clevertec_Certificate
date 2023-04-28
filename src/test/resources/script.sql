DROP SCHEMA IF EXISTS custom;
CREATE SCHEMA custom;

DROP TABLE IF EXISTS custom.gift_certificate;
CREATE TABLE custom.gift_certificate
(
    id               bigserial PRIMARY KEY,
    name             varchar(30)                 NOT NULL,
    description      varchar(100)                NOT NULL,
    price            double precision            NOT NULL,
    duration         smallint                    NOT NULL,
    create_date      timestamp without time zone NOT NULL,
    last_update_date timestamp without time zone NOT NULL
);

DROP TABLE IF EXISTS custom.tag;
CREATE TABLE custom.tag
(
    id   bigserial PRIMARY KEY,
    name varchar(30) NOT NULL UNIQUE
);

DROP TABLE IF EXISTS custom.gift_certificate_tag;
CREATE TABLE custom.gift_certificate_tag
(
    gift_certificate_id bigint REFERENCES custom.gift_certificate (id),
    tag_id              bigint REFERENCES custom.tag (id) ON DELETE CASCADE,
    CONSTRAINT gift_certificate_tag_pk PRIMARY KEY (gift_certificate_id, tag_id)
);

INSERT INTO custom.gift_certificate (name, description, price, duration, create_date, last_update_date)
VALUES ('n1', 'd1', 1.1, 1, '2023-01-01T01:11:11.111', '2023-01-01T01:11:11.111'),
       ('n2', 'd2', 2.2, 2, '2023-02-02T02:22:22.222', '2023-02-02T02:22:22.222'),
       ('n3', 'd3', 3.3, 3, '2023-03-03T03:33:33.333', '2023-03-03T03:33:33.333'),
       ('n4', 'd4', 4.4, 4, '2023-04-04T04:44:44.444', '2023-04-04T04:44:44.444'),
       ('n5', 'd5', 5.5, 5, '2023-05-05T05:55:55.555', '2023-05-05T05:55:55.555');

INSERT INTO custom.tag (name)
VALUES ('n1'),
       ('n2'),
       ('n3'),
       ('n4'),
       ('n5'),
       ('n6'),
       ('n7'),
       ('n8'),
       ('n9'),
       ('n10');

INSERT INTO custom.gift_certificate_tag (gift_certificate_id, tag_id)
VALUES (1, 1),
       (2, 2),
       (2, 3),
       (3, 4),
       (3, 5),
       (3, 6),
       (4, 7),
       (4, 8),
       (4, 9),
       (4, 10),
       (5, 1),
       (5, 2),
       (5, 3),
       (5, 4),
       (5, 5);