-- sys_uid_pool
replace into sys_uid_pool(uid,enable) values(101,0),(102,0),(103,0),(104,0),(105,0),(201,0),(202,0),(203,0),(204,0),(205,0),(10000,0);

-- users
replace into users(id,uid,unique_name,email,password,salt) 
values(101,101,'admin','service@ipetty.net','be8d68e706c7067deb0a1c150965ad6cabf50610','4f49f396ae6d9dc3'),
(102,102,'zhangzuliang','zhangzuliang@ipetty.net','0e9a23404e086821e827bc647c7ecd73339e5101','45a37e962bd54fdf'),
(103,103,'xiaojinghai','xiaojinghai@ipetty.net','9bd68c251be901430d6b3bd111ac8cba13b008ce','7a2a9b7682d478ef'),
(104,104,'kongchun','kongchun@ipetty.net','95f1d877c00ec440163c236e32f68da74c65e594','b37e309c16febafc'),
(105,105,'luocanfeng','luocanfeng@ipetty.net','f62de3b1c5829bac32efae54551db05dbddf67f9','40363981c8dac616');

-- user_profile
insert into user_profile(user_id) select u.id from users u where not exists (select 1 from user_profile up where up.user_id=u.id);

-- user_zone
insert into user_zone(user_id) select u.id from users u where not exists (select 1 from user_zone uz where uz.user_id=u.id);

-- user_statistics
insert into user_statistics(user_id) select u.id from users u where not exists (select 1 from user_statistics us where us.user_id=u.id);


-- pet
replace into pet(id,uid,created_by,nickname) values(201,201,101,'狗狗'),(202,202,102,'猫猫'),(203,203,103,'小黄'),(204,204,104,'猫咪'),(205,205,105,'鱼鱼');

