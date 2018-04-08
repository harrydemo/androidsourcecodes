/*向user表插入数据*/
INSERT INTO user(u_no,u_pwd,u_name,u_email,u_state) VALUES('2008','123','王强','www@126.com','I am 快乐.');
INSERT INTO user(u_no,u_pwd,u_name,u_email,u_state) VALUES('2009','123','Tom','www@126.com','I am sad.');
INSERT INTO user(u_no,u_pwd,u_name,u_email,u_state) VALUES('2010','123','Jerry','www@126.com','I am busy.');
/*向friend表中插入数据*/
insert into friend(u_noz,u_noy) VALUES('2008','2009');
insert into friend(u_noz,u_noy) VALUES('2008','2010');
insert into friend(u_noz,u_noy) VALUES('2008','2029');
insert into friend(u_noz,u_noy) VALUES('2008','2030');
/*向diary表中插入数据*/
INSERT INTO diary(r_title,r_content,u_no) VALUES('工作日志1','今天天气不好，阴沉沉的。','2008');
INSERT INTO diary(r_title,r_content,u_no) VALUES('工作日志2','今天好冷。','2008');
INSERT INTO diary(r_title,r_content,u_no) VALUES('旅游日志','今天去看大海了。','2009');
/*向album表中插入数据*/
INSERT INTO album(x_name,u_no) VALUES('正定游','2008');
INSERT INTO album(x_name,u_no) VALUES('我的家人','2009');

/*向comment表里插入数据*/
--INSERT INTO comment(c_content,u_no,r_id) VALUES('你写的日志太好了，我很稀饭哦','2009',SELECT r_id FROM diary WHERE u_no='2008');

/*向visit表插入数据*/
# INSERT INTO visit(u_no,r_id) VALUES('2008'

/*向max表中插入数据*/
INSERT INTO max VALUES(0,0,0,0,0,0,2020);		#向最大编号表中插入惟一的记录