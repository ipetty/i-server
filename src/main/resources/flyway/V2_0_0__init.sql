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

-- user_relationship
create table user_relationship (
	subject_id int,
	follower_id int,
	followed_on timestamp default current_timestamp,
	primary key (subject_id, follower_id),
	foreign key(subject_id) references users(id),
	foreign key(follower_id) references users(id)
) engine=innodb default charset=utf8;

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



-- location
create table location (
	id bigint primary key auto_increment,
	longitude bigint,
	latitude bigint,
	geoHash varchar(50),
	address varchar(255)
) engine=innodb default charset=utf8;
create index idx_longitude on location(longitude);
create index idx_latitude on location(latitude);
create index idx_geoHash on location(geoHash);
create index idx_address on location(address);

-- image
create table image (
	id bigint primary key auto_increment,
	created_by int,
	created_on timestamp default current_timestamp,
	small_url varchar(255),
	cut_url varchar(255),
	original_url varchar(255),
	foreign key(created_by) references users(id)
) engine=innodb default charset=utf8;
create index idx_created_on on image(created_on desc);

-- feed
create table feed (
	id bigint primary key auto_increment,
	created_by int,
	created_on timestamp default current_timestamp,
	image_id bigint,
	text varchar(255),
	location_id bigint,
	foreign key(created_by) references users(id),
	foreign key(image_id) references image(id),
	foreign key(location_id) references location(id)
) engine=innodb default charset=utf8;
create index idx_created_on on feed(created_on desc);

-- feed_comment
create table feed_comment (
	id bigint primary key auto_increment,
	created_by int,
	created_on timestamp default current_timestamp,
	feed_id bigint,
	text varchar(255),
	foreign key(created_by) references users(id),
	foreign key(feed_id) references feed(id)
) engine=innodb default charset=utf8;
create index idx_created_on on feed_comment(created_on desc);

-- feed_favor
create table feed_favor (
	id bigint primary key auto_increment,
	created_by int,
	created_on timestamp default current_timestamp,
	feed_id bigint,
	foreign key(created_by) references users(id),
	foreign key(feed_id) references feed(id)
) engine=innodb default charset=utf8;
create index idx_created_on on feed_favor(created_on desc);

-- feed_statistics
create table feed_statistics (
	feed_id bigint primary key,
	comment_count int,
	favor_count int,
	foreign key(feed_id) references feed(id)
) engine=innodb default charset=utf8;
