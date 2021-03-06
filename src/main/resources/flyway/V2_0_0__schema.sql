-- uid_pool
create table sys_uid_pool (
	uid int primary key,
	enable boolean
) engine=innodb default charset=utf8;
create index idx_enable on sys_uid_pool(enable);

-- options
create table options (
	option_group varchar(50) not null,
	value varchar(50) not null,
	label varchar(50) not null,
	icon varchar(50),
	tips varchar(255),
	idx int,
	primary key (option_group, value)
) engine=innodb default charset=utf8;
create index idx_group on options(option_group);

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
	version int not null default 1,
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

-- user_refresh_token
create table user_refresh_token (
	user_id int not null,
	device_screen_name varchar(50),
	device_id varchar(50), -- IMEI
	device_mac varchar(50),
	device_uuid varchar(50) not null, -- 客户端根据设备的多个唯一ID生成的UUID
	refresh_token varchar(50) not null,
	created_on timestamp default current_timestamp,
	foreign key(user_id) references users(id)
) engine=innodb default charset=utf8;
create index idx_refresh_token on user_refresh_token(refresh_token);
create index idx_device_uuid on user_refresh_token(device_uuid);

-- user_profile
create table user_profile (
	user_id int primary key,
	nickname varchar(50),
	avatar varchar(100),
	background varchar(100),
	gender varchar(10),
	state_and_region varchar(100),
	signature varchar(255),
	birthday date,
	foreign key(user_id) references users(id)
) engine=innodb default charset=utf8;
create index idx_nickname on user_profile(nickname);
create index idx_gender on user_profile(gender);
create index idx_state_and_region on user_profile(state_and_region);

-- user_zone
create table user_zone (
	user_id int primary key,
	foreign key(user_id) references users(id)
) engine=innodb default charset=utf8;

-- user_relationship
create table user_relationship (
	friend_id int,
	follower_id int,
	followed_on timestamp default current_timestamp,
	primary key (friend_id, follower_id),
	foreign key(friend_id) references users(id),
	foreign key(follower_id) references users(id)
) engine=innodb default charset=utf8;

-- user_statistics
create table user_statistics (
	user_id int primary key,
	bonus_of_history int default 0,
	bonus_current int default 0,
	friends_num int default 0,
	follower_num int default 0,
	feed_num int default 0,
	comment_num int default 0,
	favor_num int default 0,
	login_num int default 0,
	foreign key(user_id) references users(id)
) engine=innodb default charset=utf8;

-- pet
create table pet (
	id int primary key auto_increment,
	created_by int not null,
	created_on timestamp default current_timestamp,
	uid int not null unique,
	unique_name varchar(50),
	nickname varchar(50),
	avatar varchar(100),
	gender varchar(10),
	family varchar(50),
	birthday date,
	signature varchar(255),
	sort_order tinyint,
	version int not null default 1,
	foreign key(created_by) references users(id),
	foreign key(uid) references sys_uid_pool(uid)
) engine=innodb default charset=utf8;
create index idx_unique_name on pet(unique_name);
create index idx_gender on pet(gender);
create index idx_family on pet(family);



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
	deleted boolean default false,
	foreign key(created_by) references users(id)
) engine=innodb default charset=utf8;
create index idx_created_on on image(created_on desc);
create index idx_deleted on image(deleted);

-- feed
create table feed (
	id bigint primary key auto_increment,
	created_by int,
	created_on timestamp default current_timestamp,
	image_id bigint,
	text varchar(255),
	location_id bigint,
	deleted boolean default false,
	foreign key(created_by) references users(id),
	foreign key(image_id) references image(id),
	foreign key(location_id) references location(id)
) engine=innodb default charset=utf8;
create index idx_created_on on feed(created_on desc);
create index idx_deleted on feed(deleted);

-- feed_comment
create table feed_comment (
	id bigint primary key auto_increment,
	created_by int,
	created_on timestamp default current_timestamp,
	feed_id bigint,
	text varchar(255),
	deleted boolean default false,
	foreign key(created_by) references users(id),
	foreign key(feed_id) references feed(id)
) engine=innodb default charset=utf8;
create index idx_created_on on feed_comment(created_on desc);
create index idx_deleted on feed_comment(deleted);

-- feed_favor
create table feed_favor (
	id bigint primary key auto_increment,
	created_by int,
	created_on timestamp default current_timestamp,
	feed_id bigint,
	foreign key(created_by) references users(id),
	constraint uc_favor unique (created_by, feed_id),
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



-- activity
create table activity (
	id bigint primary key auto_increment,
	type varchar(20),
	created_by int,
	target_id bigint,
	created_on timestamp default current_timestamp,
	foreign key(created_by) references users(id)
) engine=innodb default charset=utf8;
create index idx_created_on on activity(created_on desc);
create index idx_type on activity(type);

-- bonus_point
create table bonus_point (
	id bigint primary key auto_increment,
	activity_id bigint,
	bonus int,
	expired boolean default false,
	spent boolean default false,
	created_by int,
	created_on timestamp default current_timestamp,
	foreign key(activity_id) references activity(id),
	foreign key(created_by) references users(id)
) engine=innodb default charset=utf8;
create index idx_created_on on bonus_point(created_on desc);
create index idx_expired on bonus_point(expired);
create index idx_spent on bonus_point(spent);

-- bonus_point_consumption
create table bonus_point_consumption (
	id bigint primary key auto_increment,
	activity_id bigint,
	bonus int,
	created_by int,
	created_on timestamp default current_timestamp,
	foreign key(activity_id) references activity(id),
	foreign key(created_by) references users(id)
) engine=innodb default charset=utf8;
create index idx_created_on on bonus_point_consumption(created_on desc);

-- bonus_point_bill
create table bonus_point_bill (
	bonus_point_id bigint not null,
	bonus_point_consumption_id bigint not null,
	bonus int,
	primary key (bonus_point_id, bonus_point_consumption_id),
	foreign key(bonus_point_id) references bonus_point(id),
	foreign key(bonus_point_consumption_id) references bonus_point_consumption(id)
) engine=innodb default charset=utf8;



-- invitation
create table invitation (
	id int primary key auto_increment,
	invite_code varchar(50) unique not null,
	invite_type varchar(20) not null,
	inviter varchar(50),
	inviter_id int,
	invited boolean default false,
	invited_on datetime,
	expired_datetime datetime not null,
	expired boolean default false,
	created_by int,
	created_on timestamp default current_timestamp,
	foreign key(created_by) references users(id)
) engine=innodb default charset=utf8;
create index idx_created_on on invitation(created_on desc);
create index idx_expired_datetime on invitation(expired_datetime desc);
create index idx_invited on invitation(invited);
create index idx_expired on invitation(expired);

-- feedback
create table feedback (
	id int primary key auto_increment,
	title varchar(100),
	content text,
	contact varchar(100),
	created_by int,
	created_on timestamp default current_timestamp,
	foreign key(created_by) references users(id)
) engine=innodb default charset=utf8;
create index idx_created_on on feedback(created_on desc);



-- app_update
create table app_update (
	app_name varchar(50),
	app_key varchar(50) not null,
	app_secret varchar(50),
	version_code int not null,
	version_name varchar(50),
	version_description text,
	download_url varchar(255)
) engine=innodb default charset=utf8;

