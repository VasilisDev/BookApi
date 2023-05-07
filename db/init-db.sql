CREATE USER 'root'@'%' IDENTIFIED BY 'root';
GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' WITH GRANT OPTION;

CREATE DATABASE IF NOT EXISTS `book_rating`;

USE `book_rating`;

CREATE TABLE IF NOT EXISTS `review`
(
    `id`          BIGINT       NOT NULL AUTO_INCREMENT,
    `book_id`     BIGINT       NOT NULL,
    `created_at`  DATETIME(6)  NULL DEFAULT NULL,
    `rating`      INT          NULL DEFAULT NULL,
    `review_text` VARCHAR(255) NULL DEFAULT NULL,
    PRIMARY KEY (`id`)
);
