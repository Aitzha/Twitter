CREATE TABLE IF NOT EXISTS `subscription` (
     `id` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
     `subscriber_id` text,
     `user_id` text
) DEFAULT CHARSET=UTF8;