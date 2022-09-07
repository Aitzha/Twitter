ALTER TABLE post DROP COLUMN time;
ALTER TABLE post ADD COLUMN posted_date timestamp;
ALTER TABLE post ADD COLUMN likesCount BIGINT;