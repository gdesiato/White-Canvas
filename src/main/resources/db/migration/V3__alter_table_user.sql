ALTER TABLE `user`
  ADD COLUMN `cart_id` BIGINT,
  ADD CONSTRAINT fk_user_cart FOREIGN KEY (`cart_id`) REFERENCES `cart`(id);
