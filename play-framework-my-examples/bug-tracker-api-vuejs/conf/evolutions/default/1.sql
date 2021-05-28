# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table comment (
  id                            bigint auto_increment not null,
  message                       varchar(255),
  introduced                    timestamp,
  updated                       timestamp,
  issue_id                      bigint,
  user_id                       bigint,
  constraint pk_comment primary key (id)
);

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
  user_id                       bigint not null,
  constraint pk_issue primary key (id)
);

create table user (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  email                         varchar(255),
  password                      varchar(255),
  constraint pk_user primary key (id)
);

create table user_issue (
  user_id                       bigint not null,
  issue_id                      bigint not null,
  constraint pk_user_issue primary key (user_id,issue_id)
);

create index ix_comment_issue_id on comment (issue_id);
alter table comment add constraint fk_comment_issue_id foreign key (issue_id) references issue (id) on delete restrict on update restrict;

create index ix_comment_user_id on comment (user_id);
alter table comment add constraint fk_comment_user_id foreign key (user_id) references user (id) on delete restrict on update restrict;

create index ix_issue_user_id on issue (user_id);
alter table issue add constraint fk_issue_user_id foreign key (user_id) references user (id) on delete restrict on update restrict;

create index ix_user_issue_user on user_issue (user_id);
alter table user_issue add constraint fk_user_issue_user foreign key (user_id) references user (id) on delete restrict on update restrict;

create index ix_user_issue_issue on user_issue (issue_id);
alter table user_issue add constraint fk_user_issue_issue foreign key (issue_id) references issue (id) on delete restrict on update restrict;


# --- !Downs

alter table comment drop constraint if exists fk_comment_issue_id;
drop index if exists ix_comment_issue_id;

alter table comment drop constraint if exists fk_comment_user_id;
drop index if exists ix_comment_user_id;

alter table issue drop constraint if exists fk_issue_user_id;
drop index if exists ix_issue_user_id;

alter table user_issue drop constraint if exists fk_user_issue_user;
drop index if exists ix_user_issue_user;

alter table user_issue drop constraint if exists fk_user_issue_issue;
drop index if exists ix_user_issue_issue;

drop table if exists comment;

drop table if exists issue;

drop table if exists user;

drop table if exists user_issue;

