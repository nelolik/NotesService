
CREATE SEQUENCE  IF NOT EXISTS user_id_sequence START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE  IF NOT EXISTS notes_id_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE notes (
  id SERIAL,
   user_id BIGINT NOT NULL,
   record VARCHAR(255),
   CONSTRAINT pk_notes PRIMARY KEY (id)
);

CREATE TABLE users (
  id SERIAL,
   username VARCHAR(255),
   password VARCHAR(255),
   CONSTRAINT pk_users PRIMARY KEY (id)
);

CREATE TABLE user_role (
  user_id BIGINT NOT NULL,
   roles VARCHAR(255),
   CONSTRAINT pk_user_role PRIMARY KEY (user_id, roles)
);


insert into users (id, username, password)
values (nextval('user_id_sequence'), 'Aleksey', 'Password1'),
       (nextval('user_id_sequence'), 'Tatjana', 'Password2'),
       (nextval('user_id_sequence'), 'Nikolay', 'Password3');
--values (1, 'Aleksey', 'Password1'),
--       (2, 'Tatjana', 'Password2'),
--       (3, 'Nikolay', 'Password3');


INSERT INTO notes (id, user_id, record)
VALUES (nextval('notes_id_seq'), 1, 'Aleksey`s text'),
        (nextval('notes_id_seq'), 2, 'Tatjana`s text'),
        (nextval('notes_id_seq'), 3, 'Gleb`s text');


INSERT INTO user_role (user_id, roles)
VALUES (1, 'ROLE_USER'),
        (1, 'ROLE_ADMIN'),
        (2, 'ROLE_USER'),
        (3, 'ROLE_USER');


