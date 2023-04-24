drop database if exists eksamensprojekt;
CREATE DATABASE eksamensprojekt DEFAULT CHARACTER SET utf8;
USE eksamensprojekt;

CREATE TABLE user (
userid INT auto_increment,
username VARCHAR(50),
userpassword VARCHAR(50),
PRIMARY KEY (userid)
);

INSERT INTO user (username, userpassword) values
("martin123", "login123"),
("ulrik123", "login123");

commit;