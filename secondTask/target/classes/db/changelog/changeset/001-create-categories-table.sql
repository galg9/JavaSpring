--liquibase formatted sql

--changeset author:create-categories-table
CREATE TABLE categories (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL
);
