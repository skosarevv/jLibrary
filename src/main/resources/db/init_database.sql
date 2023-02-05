DROP TABLE IF EXISTS book;
DROP TABLE IF EXISTS person;


CREATE TABLE person
(
    id         INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    full_name  VARCHAR UNIQUE NOT NULL,
    birth_year INT CHECK (birth_year >= 1900)
);


CREATE TABLE book
(
    id        INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    person_id INT     REFERENCES person (id) ON DELETE SET NULL,
    title     VARCHAR NOT NULL,
    author    VARCHAR,
    year      INT
);
