-- uid_pool
create table sys_uid_pool (
	uid int primary key,
	enable boolean
) engine=innodb default charset=utf8;
create index idx_enable on sys_uid_pool(enable);

-- users
create table users (
	id int primary key auto_increment,
	created_on timestamp default current_timestamp,
	uid int not null unique,
	unique_name varchar(50),
	phone_number varchar(20),
	email varchar(50),
	qq varchar(20),
	qzone_uid varchar(50),
	weibo_account varchar(50),
	weibo_uid varchar(50),
	password varchar(50),
	salt varchar(20),
	foreign key(uid) references sys_uid_pool(uid)
) engine=innodb default charset=utf8;
create index idx_created_on on users(created_on desc);
create index idx_unique_name on users(unique_name);
create index idx_phone_number on users(phone_number);
create index idx_email on users(email);
create index idx_qq on users(qq);
create index idx_qzone_uid on users(qzone_uid);
create index idx_weibo_account on users(weibo_account);
create index idx_weibo_uid on users(weibo_uid);
-- 插入一条初始值，密码888888
insert into sys_uid_pool(uid,enable) values(10000,0);
insert into users(id,uid,unique_name,email,password,salt) values(1,10000,'admin','service@ipetty.net','be8d68e706c7067deb0a1c150965ad6cabf50610','4f49f396ae6d9dc3');

-- user_profile
create table user_profile (
	user_id int primary key,
	nickname varchar(50),
	avatar varchar(100),
	background varchar(100),
	gender varchar(10),
	state_and_region varchar(100),
	signature varchar(255),
	foreign key(user_id) references users(id)
) engine=innodb default charset=utf8;
create index idx_nickname on user_profile(nickname);
create index idx_gender on user_profile(gender);
create index idx_state_and_region on user_profile(state_and_region);

-- pet
create table pet (
	id int primary key auto_increment,
	created_on timestamp default current_timestamp,
	user_id int not null,
	uid int not null unique,
	unique_name varchar(50),
	name varchar(50),
	gender varchar(10),
	sort_order tinyint,
	foreign key(user_id) references users(id),
	foreign key(uid) references sys_uid_pool(uid)
) engine=innodb default charset=utf8;
create index idx_unique_name on pet(unique_name);
create index idx_gender on pet(gender);
