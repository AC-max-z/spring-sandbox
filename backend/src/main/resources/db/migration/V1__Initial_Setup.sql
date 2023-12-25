CREATE TABLE customer(
    id INTEGER NOT NULL,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    age INTEGER NOT NULL,
    CONSTRAINT customer_pkey PRIMARY KEY (id)
);