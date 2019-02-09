CREATE SEQUENCE public.author_seq;
CREATE SEQUENCE public.book_seq;
CREATE SEQUENCE public.genre_seq;

CREATE TABLE public.author
(
  id bigint NOT NULL DEFAULT nextval('public.author_seq'),
  name character varying(255),
  surname character varying(255),
  country character varying(255),
  CONSTRAINT author_pkey PRIMARY KEY (id)
);

CREATE TABLE public.genre
(
  id bigint NOT NULL DEFAULT nextval('public.genre_seq'),
  name character varying(255),
  CONSTRAINT genre_pkey PRIMARY KEY (id)
);

CREATE TABLE public.book
(
  id bigint NOT NULL DEFAULT nextval('public.book_seq'),
  name character varying(255),
  publication_date integer,
  publishing_office character varying(255),
  price numeric(19,2),
  genre_id bigint,
  author_id bigint,
  CONSTRAINT book_pkey PRIMARY KEY (id),
  CONSTRAINT fk_author FOREIGN KEY (author_id)
  REFERENCES public.author (id)
  ON UPDATE NO ACTION
  ON DELETE NO ACTION,
  CONSTRAINT fk_genre FOREIGN KEY (genre_id)
  REFERENCES public.genre (id)
  ON UPDATE NO ACTION
  ON DELETE NO ACTION
);