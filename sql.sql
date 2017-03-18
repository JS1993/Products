-- 创建数据库
CREATE DATABASE productStore;
USE productStore;
-- 用户表
CREATE TABLE `user` (
  `id` INT(11) AUTO_INCREMENT,
  `username` VARCHAR(20) ,
  `PASSWORD` VARCHAR(20) ,
  `gender` VARCHAR(10) ,
  `email` VARCHAR(50) ,
  `telephone` VARCHAR(20) ,
  `introduce` VARCHAR(100),  -- 介绍
  `activeCode` VARCHAR(50) ,  -- 激活码
  `state` INT(11) ,  -- 激活状态
  `role` VARCHAR(10) DEFAULT '普通用户',  -- 角色
  `registTime` TIMESTAMP ,
  PRIMARY KEY (`id`)
)
-- 商品表
CREATE TABLE `products` (
  `id` VARCHAR(100) ,
  `name` VARCHAR(40) ,
  `price` DOUBLE ,
  `category` VARCHAR(40),
  `pnum` INT(11) ,
  `imgurl`  VARCHAR(100) ,
  `description` VARCHAR(255) ,
  PRIMARY KEY (`id`)
)
-- 订单表
CREATE TABLE `orders` (
  `id` VARCHAR(100) ,
  `money` DOUBLE,
  `receiverAddress` VARCHAR(255),
  `receiverPhone` VARCHAR(20),
  `receiverName` VARCHAR(20),
  `paystate` INT(11),
  `ordertime` TIMESTAMP,
  `user_id` INT(11),
  PRIMARY KEY (`id`),
  FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
)

-- 订单项表
CREATE TABLE `orderitem` (
  `order_id` VARCHAR(100) ,
  `product_id` VARCHAR(100),
  `buynum` INT(11) ,
  PRIMARY KEY (`order_id`,`product_id`),
  FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
  FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
)

INSERT INTO USER(username,PASSWORD,gender,email,telephone,introduce,activeCode,state,registTime)
VALUES(?,?,?,?,?,?,?,?,?);
