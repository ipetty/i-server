-- users
insert into sys_uid_pool(uid,enable) values(101,0) on duplicate key update enable=enable;
insert into sys_uid_pool(uid,enable) values(102,0) on duplicate key update enable=enable;
insert into sys_uid_pool(uid,enable) values(103,0) on duplicate key update enable=enable;
insert into sys_uid_pool(uid,enable) values(104,0) on duplicate key update enable=enable;

insert into users(id,uid,unique_name,email,password,salt) select 1,10000,'admin','service@ipetty.net','be8d68e706c7067deb0a1c150965ad6cabf50610','4f49f396ae6d9dc3' from users where not exists (select * from users where email='service@ipetty.net');
insert into users(id,uid,unique_name,email,password,salt) select 101,101,'zhangzuliang','zhangzuliang@ipetty.net','0e9a23404e086821e827bc647c7ecd73339e5101','45a37e962bd54fdf' from users where not exists (select * from users where email='zhangzuliang@ipetty.net');
insert into users(id,uid,unique_name,email,password,salt) select 102,102,'xiaojinghai','xiaojinghai@ipetty.net','9bd68c251be901430d6b3bd111ac8cba13b008ce','7a2a9b7682d478ef' from users where not exists (select * from users where email='xiaojinghai@ipetty.net');
insert into users(id,uid,unique_name,email,password,salt) select 103,103,'kongchun','kongchun@ipetty.net','95f1d877c00ec440163c236e32f68da74c65e594','b37e309c16febafc' from users where not exists (select * from users where email='kongchun@ipetty.net');
insert into users(id,uid,unique_name,email,password,salt) select 104,104,'luocanfeng','luocanfeng@ipetty.net','f62de3b1c5829bac32efae54551db05dbddf67f9','40363981c8dac616' from users where not exists (select * from users where email='luocanfeng@ipetty.net');

-- select * from users;


-- user_profile
insert into user_profile(user_id) select u.id from users u where not exists (select 1 from user_profile up where up.user_id=u.id);
-- select u.id from users u where not exists (select 1 from user_profile up where up.user_id=u.id);


-- user_zone
insert into user_zone(user_id) select u.id from users u where not exists (select 1 from user_zone uz where uz.user_id=u.id);
-- select u.id from users u where not exists (select 1 from user_zone uz where uz.user_id=u.id);


-- user_statistics
insert into user_statistics(user_id) select u.id from users u where not exists (select 1 from user_statistics us where us.user_id=u.id);
select u.id from users u where not exists (select 1 from user_statistics us where us.user_id=u.id);
