alter table users add column wechat_account varchar(50), 
	add column wechat_uid varchar(50), 
	add index idx_wechat_account(wechat_account), 
	add index idx_wechat_uid(wechat_uid);
