insert  into log(id,pid,uid,time,msgSnapshot) values 
(1,0,10000,'2016-01-03 05:48:57','{\"userName\":\"张三\",\"type\":\"register\"}'),
(2,1,10000,'2016-01-03 05:49:03','{\"userName\":\"张三\",\"type\":\"login\"}'),
(3,1,10000,'2016-01-03 05:49:51','{\"userName\":\"张三\",\"type\":\"logout\"}'),
(4,0,10001,'2016-01-03 05:52:13','{\"userName\":\"lisi\",\"type\":\"register\"}'),
(5,4,10001,'2016-01-03 05:53:43','{\"userName\":\"lisi\",\"type\":\"login\"}'),
(6,4,10001,'2016-01-03 05:54:01','{\"userName\":\"lisi\",\"type\":\"logout\"}');
insert  into user(id,userName,password) values 
(10000,'张三','96e79218965eb72c92a549dd5a330112'),
(10001,'lisi','96e79218965eb72c92a549dd5a330112');