# --- Sample dataset

# --- !Ups

 insert into user (id,name, email, password) values (  1,'John Doe.', 'test@test.com', 'testing');
 insert into user (id,name, email, password) values (  2,'Rick Ashtley', 'test1@test.com', 'testing');
 insert into user (id,name, email, password) values (  3,'Don Blooth', 'test2@test.com', 'testing');
 insert into user (id,name, email, password) values (  4,'Kerry King', 'test3@test.com', 'testing');
 insert into user (id,name, email, password) values (  5,'King Diamond', 'test4@test.com', 'testing');
 insert into user (id,name, email, password) values (  6,'Ozzy Osbourne', 'test5@test.com', 'testing');
 insert into user (id,name, email, password) values (  7,'Tom Araya', 'test6@test.com', 'testing');
 insert into user (id,name, email, password) values (  8,'Jane Doe', 'test7@test.com', 'testing');
 insert into user (id,name, email, password) values (  9,'Maria Doe', 'test8@test.com', 'testing');
 insert into user (id,name, email, password) values ( 10,'Ana James', 'test9@test.com', 'testing');


insert into issue (id,name,introduced, updated, discontinued, application, category, status, summary, description, user_id) values (  1, 'MacBook Pro 15.4 inch' , null , null, null, 'DemoApp1', 'Finance', 'Resolved', 'a test issue', 'a test issue', 1);
insert into issue (id,name,introduced, updated, discontinued, application, category, status, summary, description, user_id) values (  2,'CM-2a',null, null, null, 'DemoApp1', 'Finance', 'Resolved', 'a test issue', 'a test issue', 2);
insert into issue (id,name,introduced, updated, discontinued, application, category, status, summary, description, user_id) values (  3,'CM-200',null, null, null, 'DemoApp1', 'Finance', 'Resolved', 'a test issue', 'a test issue', 2);
insert into issue (id,name,introduced, updated, discontinued, application, category, status, summary, description, user_id) values (  4,'CM-5e',null, null, null, 'DemoApp1', 'Finance', 'UnResolved', 'a test issue', 'a test issue', 2);
insert into issue (id,name,introduced, updated, discontinued, application, category, status, summary, description, user_id) values (  5,'CM-5','1991-01-01',null, null, 'DemoApp1', 'Finance', 'Resolved', 'a test issue', 'a test issue', 2);
insert into issue (id,name,introduced, updated, discontinued, application, category, status, summary, description, user_id) values (  6,'MacBook Pro','2006-01-10',null, null, 'DemoApp1', 'Finance', 'Resolved', 'a test issue', 'a test issue', 1);
insert into issue (id,name,introduced, updated, discontinued, application, category, status, summary, description, user_id) values (  7,'Apple IIe',null, null, null, 'DemoApp1', 'Finance', 'Resolved', 'a test issue', 'a test issue', 3);
insert into issue (id,name,introduced, updated, discontinued, application, category, status, summary, description, user_id) values (  8,'Apple IIc',null, null, null, 'DemoApp1', 'Finance', 'Resolved', 'a test issue', 'a test issue', 8);
insert into issue (id,name,introduced, updated, discontinued, application, category, status, summary, description, user_id) values (  9,'Apple IIGS',null, null, null, 'DemoApp1', 'Finance', 'Resolved', 'a test issue', 'a test issue', 4);
insert into issue (id,name,introduced, updated, discontinued, application, category, status, summary, description, user_id) values ( 10,'Apple IIc Plus',null, null, null, 'DemoApp1', 'Finance', 'Resolved', 'a test issue', 'a test issue', 6);
insert into issue (id,name,introduced, updated, discontinued, application, category, status, summary, description, user_id) values ( 11,'Apple II Plus',null, null, null, 'DemoApp1', 'Finance', 'Resolved', 'a test issue', 'a test issue', 5);
insert into issue (id,name,introduced, updated, discontinued, application, category, status, summary, description, user_id) values ( 12,'Apple III','1980-05-01',null,'1984-04-01', 'DemoApp1', 'Finance', 'Resolved', 'a test issue', 'a test issue', 10);
insert into issue (id,name,introduced, updated, discontinued, application, category, status, summary, description, user_id) values ( 13,'Apple Lisa',null, null, null, 'DemoApp1', 'Finance', 'Resolved', 'a test issue', 'a test issue', 1);
insert into issue (id,name,introduced, updated, discontinued, application, category, status, summary, description, user_id) values ( 14,'CM-2',null, null, null, 'DemoApp1', 'Finance', 'UnResolved', 'a test issue', 'a test issue', 2);
insert into issue (id,name,introduced, updated, discontinued, application, category, status, summary, description, user_id) values ( 15,'Connection Machine','1987-01-01', null, null, 'DemoApp1', 'Finance', 'Resolved', 'a test issue', 'a test issue', 2);
insert into issue (id,name,introduced, updated, discontinued, application, category, status, summary, description, user_id) values ( 16,'Apple II','1977-04-01','1993-10-01',null, 'DemoApp1', 'Finance', 'Resolved', 'a test issue', 'a test issue', 2);
insert into issue (id,name,introduced, updated, discontinued, application, category, status, summary, description, user_id) values ( 17,'Apple III Plus','1983-12-01','1984-04-01',null, 'DemoApp1', 'Finance', 'Resolved', 'a test issue', 'a test issue', 2);
insert into issue (id,name,introduced, updated, discontinued, application, category, status, summary, description, user_id) values ( 18,'COSMAC ELF',null, null, null, 'DemoApp1', 'Finance', 'Resolved', 'a test issue', 'a test issue', 2);
insert into issue (id,name,introduced, updated, discontinued, application, category, status, summary, description, user_id) values ( 19,'COSMAC VIP','1977-01-01',null, null, 'DemoApp1', 'Finance', 'Resolved', 'a test issue', 'a test issue', 3);
insert into issue (id,name,introduced, updated, discontinued, application, category, status, summary, description, user_id) values ( 20,'ELF II','1977-01-01',null, null, 'DemoApp1', 'Finance', 'Resolved', 'a test issue', 'a test issue', 4);


# --- !Downs

delete from issue;
delete from user;
