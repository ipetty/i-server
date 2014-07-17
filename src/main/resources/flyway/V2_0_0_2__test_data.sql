-- sys_uid_pool
replace into sys_uid_pool(uid,enable) values(101,0),(201,0),(10000,0);

-- users
replace into users(id,uid,unique_name,email,password,salt) 
values(101,101,'admin','service@ipetty.net','be8d68e706c7067deb0a1c150965ad6cabf50610','4f49f396ae6d9dc3');

-- user_profile
insert into user_profile(user_id) select u.id from users u where not exists (select 1 from user_profile up where up.user_id=u.id);
update users u, user_profile up set up.nickname='爱宠小秘书' where u.email='service@ipetty.net' and u.id=up.user_id;

-- user_zone
insert into user_zone(user_id) select u.id from users u where not exists (select 1 from user_zone uz where uz.user_id=u.id);

-- user_statistics
insert into user_statistics(user_id) select u.id from users u where not exists (select 1 from user_statistics us where us.user_id=u.id);


-- pet
replace into pet(id,uid,created_by,nickname) values(201,201,101,'狗狗');

