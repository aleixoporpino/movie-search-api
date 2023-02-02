CREATE SCHEMA IF NOT EXISTS moviesdb;

CREATE TABLE IF NOT EXISTS moviesdb.movie
(
    id             INTEGER      NOT NULL
        PRIMARY KEY,
    original_title VARCHAR(255) NOT NULL
);

CREATE INDEX IF NOT EXISTS movie_original_title_index
    ON moviesdb.movie (original_title);

CREATE TABLE IF NOT EXISTS moviesdb.tv_show
(
    id             INTEGER      NOT NULL
        PRIMARY KEY,
    original_name VARCHAR(255) NOT NULL
);

CREATE INDEX IF NOT EXISTS tv_show_original_name_index
    ON moviesdb.tv_show (original_name);
