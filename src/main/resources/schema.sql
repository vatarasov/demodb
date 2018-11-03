-- Table: public.country

-- DROP TABLE public.country;

CREATE TABLE public.country
(
  name character varying(255) NOT NULL,
  id integer NOT NULL,
  CONSTRAINT country_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.country
  OWNER TO postgres;

-- Table: public.man

-- DROP TABLE public.man;

CREATE TABLE public.man
(
  name character varying(255) NOT NULL,
  id integer NOT NULL,
  country integer,
  CONSTRAINT man_pkey PRIMARY KEY (id),
  CONSTRAINT man_country_fkey FOREIGN KEY (country)
      REFERENCES public.country (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.man
  OWNER TO postgres;

-- Index: public.fki_man_country_fkey

-- DROP INDEX public.fki_man_country_fkey;

CREATE INDEX fki_man_country_fkey
  ON public.man
  USING btree
  (country);

-- Table: public.factory

-- DROP TABLE public.factory;

CREATE TABLE public.factory
(
  name character varying(255) NOT NULL,
  country integer,
  id integer NOT NULL,
  CONSTRAINT factory_pkey PRIMARY KEY (id),
  CONSTRAINT factory_country_fkey FOREIGN KEY (country)
      REFERENCES public.country (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.factory
  OWNER TO postgres;

-- Index: public.fki_factory_country_fkey

-- DROP INDEX public.fki_factory_country_fkey;

CREATE INDEX fki_factory_country_fkey
  ON public.factory
  USING btree
  (country);

-- Table: public.film

-- DROP TABLE public.film;

CREATE TABLE public.film
(
  id integer NOT NULL,
  name character varying(255) NOT NULL,
  genre character varying(255),
  year integer NOT NULL,
  producer integer,
  factory integer,
  description text,
  CONSTRAINT film_pkey PRIMARY KEY (id),
  CONSTRAINT film_factory_fkey FOREIGN KEY (factory)
      REFERENCES public.factory (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT film_producer_fkey FOREIGN KEY (producer)
      REFERENCES public.man (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT film_name_year_key UNIQUE (name, year)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.film
  OWNER TO postgres;

-- Index: public.fki_film_factory_fkey

-- DROP INDEX public.fki_film_factory_fkey;

CREATE INDEX fki_film_factory_fkey
  ON public.film
  USING btree
  (factory);

-- Index: public.fki_film_producer_fkey

-- DROP INDEX public.fki_film_producer_fkey;

CREATE INDEX fki_film_producer_fkey
  ON public.film
  USING btree
  (producer);

-- Table: public.filmstar

-- DROP TABLE public.filmstar;

CREATE TABLE public.filmstar
(
  star integer NOT NULL,
  film integer NOT NULL,
  CONSTRAINT filmstar_pkey PRIMARY KEY (star, film),
  CONSTRAINT filmstar_film_fkey FOREIGN KEY (film)
      REFERENCES public.film (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT filmstar_star_fkey FOREIGN KEY (star)
      REFERENCES public.man (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.filmstar
  OWNER TO postgres;

-- Index: public.fki_filmstar_film_fkey

-- DROP INDEX public.fki_filmstar_film_fkey;

CREATE INDEX fki_filmstar_film_fkey
  ON public.filmstar
  USING btree
  (film);

-- Sequence: public.country_id_seq

-- DROP SEQUENCE public.country_id_seq;

CREATE SEQUENCE public.country_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE public.country_id_seq
  OWNER TO postgres;

-- Sequence: public.factory_id_seq

-- DROP SEQUENCE public.factory_id_seq;

CREATE SEQUENCE public.factory_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE public.factory_id_seq
  OWNER TO postgres;

-- Sequence: public.film_id_seq

-- DROP SEQUENCE public.film_id_seq;

CREATE SEQUENCE public.film_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE public.film_id_seq
  OWNER TO postgres;

-- Sequence: public.man_id_seq

-- DROP SEQUENCE public.man_id_seq;

CREATE SEQUENCE public.man_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE public.man_id_seq
  OWNER TO postgres;
