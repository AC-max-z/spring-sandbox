ALTER TABLE users
RENAME COLUMN username TO email;

ALTER TABLE users
RENAME COLUMN enabled TO is_active;

ALTER TABLE users
ADD CONSTRAINT user_email_unique UNIQUE (email);