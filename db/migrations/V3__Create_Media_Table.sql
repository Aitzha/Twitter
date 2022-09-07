CREATE TABLE IF NOT EXISTS `media` (
    `id` text(50),
    `post_id` int,
    `type` text,
    `name` text
) DEFAULT CHARSET=UTF8;