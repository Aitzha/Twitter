CREATE TABLE IF NOT EXISTS `media` (
    `id` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `post_id` int,
    `type` text,
    `name` text
) DEFAULT CHARSET=UTF8;