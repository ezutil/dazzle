DROP TABLE IF EXISTS log;
CREATE TABLE log (
  id bigint,
  pid bigint,
  uid bigint,
  time datetime,
  msgSnapshot varchar(1024),
  PRIMARY KEY (id)
);
DROP TABLE IF EXISTS user;
CREATE TABLE user (
  id bigint,
  userName varchar(128),
  password varchar(32),
  PRIMARY KEY (id)
);