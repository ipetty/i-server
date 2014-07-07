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
