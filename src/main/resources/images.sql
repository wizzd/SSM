create database laboratory;
use laboratory;
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

