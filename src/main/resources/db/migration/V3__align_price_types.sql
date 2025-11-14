-- Приводим типы к double precision с учётом реальных имён таблиц
ALTER TABLE order_items ALTER COLUMN price TYPE double precision USING price::double precision;
ALTER TABLE products     ALTER COLUMN price TYPE double precision USING price::double precision;
ALTER TABLE orders       ALTER COLUMN total TYPE double precision USING total::double precision;
