--liquibase formatted sql

--changeset author:create-news-table
CREATE TABLE news (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    text TEXT NOT NULL,
    date TIMESTAMP NOT NULL,
    category_id BIGINT NOT NULL,
    CONSTRAINT fk_news_category FOREIGN KEY (category_id) REFERENCES categories(id)
);
