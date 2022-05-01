CREATE TABLE IF NOT EXISTS `post` (
     `id` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
     `ownerId` text,
     `text` text,
     `time` timestamp
) DEFAULT CHARSET=UTF8;