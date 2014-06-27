-- pet
insert into sys_uid_pool(uid,enable) values(201,0) on duplicate key update enable=enable;
insert into sys_uid_pool(uid,enable) values(202,0) on duplicate key update enable=enable;
insert into sys_uid_pool(uid,enable) values(203,0) on duplicate key update enable=enable;
insert into sys_uid_pool(uid,enable) values(204,0) on duplicate key update enable=enable;
insert into sys_uid_pool(uid,enable) values(205,0) on duplicate key update enable=enable;

insert into pet(id,uid,created_by,nickname) select 201,201,id,'狗狗' from users where email='service@ipetty.net';
insert into pet(id,uid,created_by,nickname) select 202,202,id,'猫猫' from users where email='zhangzuliang@ipetty.net';
insert into pet(id,uid,created_by,nickname) select 203,203,id,'小黄' from users where email='xiaojinghai@ipetty.net';
insert into pet(id,uid,created_by,nickname) select 204,204,id,'猫咪' from users where email='kongchun@ipetty.net';
insert into pet(id,uid,created_by,nickname) select 205,205,id,'鱼鱼' from users where email='luocanfeng@ipetty.net';

-- select * from pet;
