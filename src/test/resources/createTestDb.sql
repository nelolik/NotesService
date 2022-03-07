create schema test;

CREATE SEQUENCE user_sequence
  start 1
  increment 1;

CREATE SEQUENCE note_sequence
  start 1
  increment 1;


CREATE TABLE test.users (
  id BIGSERIAL NOT NULL,
   username VARCHAR(255),
   password VARCHAR(255),
   CONSTRAINT pk_users PRIMARY KEY (id)
);

insert into test.users (username, password)
values (nextval('user_sequense'), 'Aleksey', 'Password1'),
       (nextval('user_sequense'), 'Tatjana', 'Password2'),
       (nextval('user_sequense'), 'Nikolay', 'Password3');



CREATE TABLE test.notes (
  id BIGSERIAL NOT NULL,
   user_id BIGINT NOT NULL,
   record VARCHAR(255),
   CONSTRAINT pk_notes PRIMARY KEY (id)
);

INSERT INTO test.notes (user_id, record)
VALUES (nextval('note_sequense'), 1, "Aleksey`s text"),
        (nextval('note_sequense'), 2, "Tatjana`s text"),
        (nextval('note_sequense'), 3, "Gleb`s text");

CREATE TABLE test.user_role (
  user_id BIGINT NOT NULL,
   roles VARCHAR(255),
   CONSTRAINT pk_user_role PRIMARY KEY (user_id)
);

INSERT INTO test.user_role (user_id, roles)
VALUES (1, 'ROLE_USER'),
        (1, 'ROLE_ADMIN'),
        (2, 'ROLE_USER'),
        (3, 'ROLE_USER');


