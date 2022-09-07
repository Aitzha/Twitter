CREATE TABLE IF NOT EXISTS `post` (
     `id` text(10),
     `ownerId` text,
     `text` text,
     `time` timestamp
) DEFAULT CHARSET=UTF8;