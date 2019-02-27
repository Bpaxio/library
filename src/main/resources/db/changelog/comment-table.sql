CREATE SEQUENCE public.comment_seq;

CREATE TABLE public.comment
(
  id bigint NOT NULL DEFAULT nextval('public.comment_seq'),
  author_username character varying(255),
  created timestamp NOT NULL DEFAULT now(),
  message character varying(2000) NOT NULL,
  book_id bigint NOT NULL,
  CONSTRAINT comment_pkey PRIMARY KEY (id),
  CONSTRAINT fk_book FOREIGN KEY (book_id)
  REFERENCES public.book (id) MATCH SIMPLE
  ON UPDATE NO ACTION
  ON DELETE NO ACTION
)
WITH (
OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.comment
  OWNER to postgres;
