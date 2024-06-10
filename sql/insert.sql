insert into users (username, email, password, created_at) values ('admin', 'admin@aulabt.it', '$2a$10$oMiUOq5ToRfUI/Zprg5nE.qt8nT9KKJZoDBu1SIWuj.UGx8aRHwxS', '20240607');

insert into roles (name) values ('admin');
insert into roles (name) values ('user');
insert into roles (name) values ('revisor');

insert into users_roles (user_id, role_id) values (1,1);

insert into categories (name) values ('politica'), ('economia'), ('food&drink') , ('sport'), ('intrattenimento'), ('tech');



