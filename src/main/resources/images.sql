create database laboratory;
use laboratory;
-- auto-generated definition
-- auto-generated definition
create table images
(
  img      varchar(100) not null,
  openId   char(28)     not null,
  name     char(10)     null,
  hospital varchar(30)  null,
  type     varchar(30)  null,
  time     date         null,
  remark   varchar(50)  null,
  constraint images_image_uindex
    unique (img),
  constraint images_openId_uindex
    unique (openId),
  constraint openId
    foreign key (openId) references user (openId)
);

alter table images
  add primary key (img);
  drop procedure if exists  sortByTime;
create procedure  sortByTime(in a int,in n char(28))
BEGIN
if a = 1 then
  select * from images where openId=n order by time asc;
else
  select * from images where openId=n  order by time desc ;
end if;
end;

