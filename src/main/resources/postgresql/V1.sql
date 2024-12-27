--jwt schema 
create table if not exists word_app.users(
    id 		  bigint generated always as identity primary key ,
    username  varchar(50) not null,
    password  varchar(250) not null,
    email 	  varchar(50) not null
);

create table if not exists word_app.roles(
    id 		int8  generated always as identity primary key,
    name 	varchar(50) not null
);

create table word_app.user_roles(
    user_id  	int8,
    role_id 	int8,
    primary key (user_id, role_id),
    foreign key (user_id) references users(id),
    foreign key (role_id) references roles(id)
);



