CREATE TABLE IF NOT EXISTS `cart` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `total_price` DECIMAL(10, 2),
    `user_id` BIGINT,
    FOREIGN KEY (`user_id`) REFERENCES `user`(id)
);

