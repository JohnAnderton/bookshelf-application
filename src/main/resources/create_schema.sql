DROP ROLE IF EXISTS bookshelf;
DROP SCHEMA IF EXISTS bookshelf CASCADE;

CREATE ROLE bookshelf WITH LOGIN ENCRYPTED PASSWORD 'changeit';
CREATE SCHEMA bookshelf;

-- Table: bookshelf.author

CREATE TABLE bookshelf.author
(
  id serial NOT NULL,
  firstname text NOT NULL,
  lastname text NOT NULL,
  birthyear integer,
  CONSTRAINT author_pkey PRIMARY KEY (id)
);
ALTER TABLE bookshelf.author
  OWNER TO bookshelf;


-- Table: bookshelf.book

CREATE TABLE bookshelf.book
(
  id serial NOT NULL,
  title text NOT NULL,
  abstract text,
  CONSTRAINT book_pkey PRIMARY KEY (id)
);
ALTER TABLE bookshelf.book
  OWNER TO bookshelf;

-- Table: bookshelf.book_author

CREATE TABLE bookshelf.book_author
(
  book_id integer NOT NULL,
  author_id integer NOT NULL,
  CONSTRAINT book_author_pkey PRIMARY KEY (book_id, author_id),
  CONSTRAINT author_id_fkey FOREIGN KEY (author_id)
      REFERENCES bookshelf.author (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT book_id_fkey FOREIGN KEY (book_id)
      REFERENCES bookshelf.book (id) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
);
ALTER TABLE bookshelf.book_author
  OWNER TO bookshelf;

