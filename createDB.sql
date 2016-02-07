CREATE TABLE `users` (
  `user_id` int(35) NOT NULL AUTO_INCREMENT,
  `email` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `type` tinyint(1) NOT NULL,
   PRIMARY KEY (`id`)
) ENGINE=InnoDB CHARSET=utf8;

CREATE TABLE `products` (
  `user_id` int(24) NOT NULL,
  `product_id` int(24) NOT NULL,
  `product_name` varchar(50) NOT NULL,
  `sum_cost` FLOAT(20, 4) NULL,
  FOREIGN KEY (`product_index_id`) REFERENCES products(`product_index_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `costs` (
  `id` INT(24) NOT NULL,
  `user_id` INT(24) NOT NULL,
  `product_id` INT(24) NOT NULL ,
  `costs_name` VARCHAR(50) NOT NULL ,
  `K` FLOAT(5,2) NOT NULL ,
  `price` FLOAT(10,3) NOT NULL ,
  `amount` INT(11) NOT NULL ,
  `type` BOOLEAN NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET=utf8;
