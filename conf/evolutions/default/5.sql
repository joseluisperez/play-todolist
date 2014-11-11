# Tasks schema
 
# --- !Ups

CREATE SEQUENCE category_id_seq;
CREATE TABLE category (
    id integer NOT NULL DEFAULT nextval('category_id_seq'),
    name varchar(255)
);
 
# --- !Downs
 
DROP TABLE category;
DROP SEQUENCE category_id_seq;