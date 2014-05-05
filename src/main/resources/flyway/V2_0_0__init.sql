-- users
create table users (
	id int primary key auto_increment,
	created_on timestamp default current_timestamp,
	uid int not null,
	account varchar(50),
	phone_number varchar(20),
	email varchar(50),
	qq varchar(20),
	qzone_uid varchar(50),
	weibo_account varchar(50),
	weibo_uid varchar(50),
	password varchar(50),
	salt varchar(20)
) engine=innodb default charset=utf8;

-- user_profile
create table user_profile (
	id int primary key,
	nickname varchar(50),
	avatar varchar(100),
	background varchar(100),
	gender tinyint,
	state_and_region varchar(100),
	signature varchar(255)
) engine=innodb default charset=utf8;
