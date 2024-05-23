--liquibase formatted sql
--changeset aychernyavskiy:init
CREATE TABLE IF NOT EXISTS users (
	id varchar NOT NULL,
	firstname varchar NULL,
	secondname varchar NULL,
	birthdate date NULL,
	biography text NULL,
	city varchar NULL,
	"password" varchar NULL,
	CONSTRAINT pk PRIMARY KEY (id)
);