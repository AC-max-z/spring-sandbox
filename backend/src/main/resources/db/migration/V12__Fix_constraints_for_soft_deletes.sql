ALTER TABLE users
    DROP CONSTRAINT user_email_unique;

CREATE UNIQUE INDEX unique_active_user_email
    ON users (email)
    WHERE is_active = TRUE;

ALTER TABLE customer
    DROP CONSTRAINT customer_email_unique;

CREATE UNIQUE INDEX unique_active_customer_email
    ON customer (email)
    WHERE is_deleted = FALSE;