
drop table crush_log;

-- crash_log
create table crash_log (
	user_id int default null,
	user_name varchar(50),
	android_version varchar(20),
	app_version_code int default null,
	app_version_name varchar(50),
	crash_type varchar(20),
	log text
) engine=innodb default charset=utf8;
