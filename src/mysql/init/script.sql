drop database if exists eksamensprojekt;
CREATE DATABASE eksamensprojekt DEFAULT CHARACTER SET utf8;
USE eksamensprojekt;

CREATE TABLE user (
userid INT auto_increment,
name VARCHAR(30),
username VARCHAR(50) UNIQUE,
userpassword VARCHAR(50),
PRIMARY KEY (userid)
);

CREATE TABLE project (
projectid INT auto_increment,
projectname VARCHAR(50),
PRIMARY KEY (projectid)
);

CREATE TABLE board (
boardid INT auto_increment,
boardname VARCHAR(50),
projectid INT,
PRIMARY KEY (boardid),
FOREIGN KEY (projectid) REFERENCES project (projectid)
);

CREATE TABLE story (
storyid INT auto_increment,
storyname VARCHAR(50),
storydescription VARCHAR(500),
acceptcriteria VARCHAR(500),
deadline DATE,
boardid INT,
PRIMARY KEY (storyid),
FOREIGN KEY (boardid) REFERENCES board (boardid)
);

CREATE TABLE task (
taskid INT auto_increment,
taskname VARCHAR(50),
taskdescription VARCHAR(500),
storypoints INT,
storyid INT,
PRIMARY KEY (taskid),
FOREIGN KEY (storyid) REFERENCES story (storyid)
);

CREATE TABLE userproject (
userid INT,
projectid INT,
PRIMARY KEY (userid, projectid),
FOREIGN KEY (userid) REFERENCES user (userid),
FOREIGN KEY (projectid) REFERENCES project (projectid)
);

commit;
