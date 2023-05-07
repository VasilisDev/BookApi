CREATE USER IF NOT EXISTS 'root'@'%' IDENTIFIED BY 'root';
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


INSERT INTO `review` VALUES (1,4,'2023-05-05 20:17:59.578340',1,'hello review'),(2,4,'2023-05-05 20:57:35.991887',3,'hello review 4'),(3,4,'2023-05-05 20:57:46.047648',5,'hello review 5'),(4,4,'2023-05-05 20:57:52.816496',1,'hello review 1'),(5,100,'2023-05-05 23:38:14.145043',3,'hello review 1'),(6,100,'2023-05-05 23:38:18.356223',4,'hello review 1');