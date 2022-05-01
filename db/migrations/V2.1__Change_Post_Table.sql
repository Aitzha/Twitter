ALTER TABLE post DROP COLUMN ownerId;
ALTER TABLE post ADD COLUMN owner_id text;