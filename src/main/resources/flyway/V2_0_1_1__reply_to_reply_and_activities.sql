alter table feed_comment add column reply_to_comment_id bigint default null;
alter table feed_comment add foreign key (reply_to_comment_id) references feed_comment(id);
alter table feed_comment add column reply_to_user_id int default null;
alter table feed_comment add foreign key (reply_to_user_id) references users(id);

alter table activity add column content varchar(255);

-- activity_inbox
create table activity_inbox (
	activity_id bigint,
	receiver_id int not null,
	foreign key(activity_id) references activity(id),
	foreign key(receiver_id) references users(id)
) engine=innodb default charset=utf8;

-- notification
create table notification (
	user_id int not null unique,
	new_fans_num int default 0,
	new_fans_last_check datetime,
	new_replies_num int default 0,
	new_replies_last_check datetime,
	new_favors_num int default 0,
	new_favors_last_check datetime,
	foreign key(user_id) references users(id)
) engine=innodb default charset=utf8;

insert into notification(user_id) select u.id from users u where not exists (select 1 from notification n where n.user_id=u.id);
