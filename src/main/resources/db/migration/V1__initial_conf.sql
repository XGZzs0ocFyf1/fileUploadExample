--V1__initial_conf.sql
create table if not exists word_app.users(
    user_id 		  bigint generated always as identity primary key ,
    username  varchar(50) not null,
    password  varchar(250) not null,
    email 	  varchar(50) not null,
    email_verified boolean default false
    );

create table if not exists word_app.roles(
     role_id 		int8  generated always as identity primary key,
     name 	varchar(50) not null unique
    );

create table word_app.user_roles(
    user_id  	int8,
    role_id 	int8,
    primary key (user_id, role_id),
    foreign key (user_id) references word_app.users(user_id),
    foreign key (role_id) references word_app.roles(role_id)
);


create table if not exists word_app.auth_codes(
    auth_code_id int8  generated always as identity primary key,
    auth_code varchar,
    created_at timestamp not null,
    was_it_used boolean default true,
    user_id bigint not null references word_app.users(user_id)
);

CREATE TABLE word_app.tokens (
        token_id int8  generated always as identity primary key,
        access_token varchar,
        refresh_token varchar,
        logged_out varchar,
        user_id bigint,
        FOREIGN KEY (user_id) REFERENCES users(user_id)
);




