CREATE TABLE IF NOT EXISTS `cart_item` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `service_id` BIGINT NOT NULL,
    `cart_id` BIGINT NOT NULL,
    `user_id` BIGINT,
    FOREIGN KEY (`service_id`) REFERENCES `consultancy_service`(id),
    FOREIGN KEY (`cart_id`) REFERENCES `cart`(id),
    FOREIGN KEY (`user_id`) REFERENCES `user`(id)
);

CREATE TABLE IF NOT EXISTS `order_item` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `order_id` BIGINT NOT NULL,
    `service_id` BIGINT NOT NULL,
    FOREIGN KEY (`order_id`) REFERENCES `order`(id),
    FOREIGN KEY (`service_id`) REFERENCES `consultancy_service`(id)
);
