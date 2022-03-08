
CREATE TABLE notes (
  id BIGINT NOT NULL,
   user_id BIGINT NOT NULL,
   record VARCHAR(255),
   CONSTRAINT pk_notes PRIMARY KEY (id)
);

CREATE TABLE users (
  id BIGINT NOT NULL,
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
values (1, 'Aleksey', 'Password1'),
       (2, 'Tatjana', 'Password2'),
       (3, 'Nikolay', 'Password3');


INSERT INTO notes (id, user_id, record)
VALUES (1, 1, 'Aleksey`s text'),
        (2, 2, 'Tatjana`s text'),
        (3, 3, 'Gleb`s text');


INSERT INTO user_role (user_id, roles)
VALUES (1, 'ROLE_USER'),
        (1, 'ROLE_ADMIN'),
        (2, 'ROLE_USER'),
        (3, 'ROLE_USER');


