CREATE TABLE If NOT EXISTS `session` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `token` VARCHAR(255) NOT NULL,
    `user_id` BIGINT NOT NULL,
    UNIQUE (`token`),
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`)
);
