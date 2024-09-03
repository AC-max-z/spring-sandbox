ALTER TABLE brand
    ADD is_deleted BOOLEAN default FALSE;

ALTER TABLE brand
    ALTER COLUMN is_deleted SET NOT NULL;

ALTER TABLE product
    ADD is_deleted BOOLEAN default FALSE;

ALTER TABLE product
    ALTER COLUMN is_deleted SET NOT NULL;

ALTER TABLE customer
    ADD is_deleted BOOLEAN default FALSE;

ALTER TABLE customer
    ALTER COLUMN is_deleted SET NOT NULL;