-- auto-generated definition
create table user
(
  name     char(32)  null,
  avataUrl char(129) null,
  openId   char(28)  not null,
  id       int auto_increment,
  constraint user_id_uindex
    unique (id),
  constraint user_openId_uindex
    unique (openId)
);

alter table user
  add primary key (openId);

