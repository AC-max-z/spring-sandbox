ALTER TABLE brand
    ADD COLUMN is_deleted BOOLEAN NOT NULL default FALSE;

ALTER TABLE customer
    ADD COLUMN is_deleted BOOLEAN NOT NULL default FALSE;

ALTER TABLE product
    ADD COLUMN is_deleted BOOLEAN NOT NULL default FALSE;

ALTER TABLE users
    ADD COLUMN is_deleted BOOLEAN NOT NULL default FALSE;