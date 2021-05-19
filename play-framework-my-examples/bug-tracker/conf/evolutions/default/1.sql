# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table issue (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  introduced                    timestamp,
  updated                       timestamp,
  discontinued                  timestamp,
  application                   varchar(255),
  category                      varchar(255),
  status                        varchar(255),
  summary                       varchar(255),
  description                   varchar(255),
  user_id                       bigint,
  constraint pk_issue primary key (id)
);

create table user (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  email                         varchar(255),
  password                      varchar(255),
  constraint pk_user primary key (id)
);

create index ix_issue_user_id on issue (user_id);
alter table issue add constraint fk_issue_user_id foreign key (user_id) references user (id) on delete restrict on update restrict;


# --- !Downs

alter table issue drop constraint if exists fk_issue_user_id;
drop index if exists ix_issue_user_id;

drop table if exists issue;

drop table if exists user;

