alter table pet add column birthday date;
alter table pet add column family varchar(50);
create index idx_family on pet(family);

alter table pet change column name nickname varchar(50);
alter table pet add column avatar varchar(100);
alter table pet add column signature varchar(255);
