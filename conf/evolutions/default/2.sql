# Usuarios

# --- !Ups

CREATE SEQUENCE usuario_id_seq;

CREATE TABLE task_user(
    id integer NOT NULL DEFAULT nextval('usuario_id_seq'),
    nick varchar(255) UNIQUE,
    constraint pk_usuario primary key(id)
);

ALTER TABLE task add usuario_id integer NOT NULL;
ALTER TABLE task add constraint fk_task_task_user foreign key(usuario_id) references task_user (id) on delete cascade on update restrict;


INSERT INTO task_user(nick) values ('Anonimo');
INSERT INTO task_user(nick) values ('Jose');
INSERT INTO task_user(nick) values ('Luis');

INSERT INTO task(label, usuario_id) values ('anonimo', 1);
INSERT INTO task(label, usuario_id) values ('hola', 2);
INSERT INTO task(label, usuario_id) values ('adios', 3);

# --- !Downs

DROP TABLE task_user;
DROP SEQUENCE usuario_id_seq;
