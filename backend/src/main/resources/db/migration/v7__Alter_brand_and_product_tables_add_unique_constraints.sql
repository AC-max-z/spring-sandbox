ALTER TABLE brand
ADD CONSTRAINT brand_name_unique UNIQUE (name);

ALTER TABLE product
ADD CONSTRAINT product_name_unique UNIQUE (name);