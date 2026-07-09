-- Clean MySQL dump for accounts DB

DROP TABLE IF EXISTS `user_role`;
DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS `role`;

-- =======================
-- ROLE
-- =======================
CREATE TABLE `role` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `role` (id, name) VALUES
(1, 'ROLE_USER');

-- =======================
-- USER
-- =======================
CREATE TABLE `user` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(255),
  `userEmail` VARCHAR(255),
  `profileImg` VARCHAR(255),
  `profileImgPath` VARCHAR(255),
  `dateOfBirth` VARCHAR(255),
  `fatherName` VARCHAR(255),
  `motherName` VARCHAR(255),
  `gender` VARCHAR(255),
  `maritalStatus` VARCHAR(255),
  `permanentAddress` VARCHAR(255),
  `tempAddress` VARCHAR(255),
  `primaryOccupation` VARCHAR(255),
  `secondaryOccupation` VARCHAR(255),
  `skills` VARCHAR(255),
  `phoneNumber` VARCHAR(255),
  `secondaryPhoneNumber` VARCHAR(255),
  `nationality` VARCHAR(255),
  `language` VARCHAR(255),
  `workingExperience` VARCHAR(255),
  `password` VARCHAR(255),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `user`
(id, username, userEmail, dateOfBirth, fatherName, motherName, gender, maritalStatus,
 permanentAddress, tempAddress, primaryOccupation, secondaryOccupation, skills,
 phoneNumber, secondaryPhoneNumber, nationality, language, workingExperience, password)
VALUES
(7,'admin_vp','admin@visualpathit.com',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,
'$2a$11$0a7VdTr4rfCQqtsvpng6GuJnzUmQ7gZiHXgzGPgm5hkRa3avXgBLK'),
(8,'WahidKhan','wahid.khan74@gmail.com','28/03/1994','M Khan','R Khan','male','unMarried',
'Ameerpet,Hyderabad','Ameerpet,Hyderabad','Software Engineer','Software Engineer',
'Java HTML CSS','8888888888','8888888888','Indian','english','2',
'$2a$11$UgG9TkHcgl02LxlqxRHYhOf7Xv4CxFmFEgS0FpUdk42OeslI.6JAW'),
(9,'Gayatri','gayatri@gmail.com','20/06/1993','K','L','male','unMarried',
'Ameerpet,Hyderabad','Ameerpet,Hyderabad','Software Engineer','Software Engineer',
'Java HTML CSS','9999999999','9999999999','India','english','5',
'$2a$11$gwvsvUrFU.YirMM1Yb7NweFudLUM91AzH5BDFnhkNzfzpjG.FplYO'),
(10,'WahidKhan2','wahid.k@gmail.com','28/03/1994','M Khan','R Khan','male','unMarried',
'Ameerpet,Hyderabad','Ameerpet,Hyderabad','Software Engineer','Software Engineer',
'Java HTML CSS','7777777777','777777777','India','english','7',
'$2a$11$6oZEgfGGQAH23EaXLVZ2WOSKxcEJFnBSw2N2aghab0s2kcxSQwjhC'),
(11,'KiranKumar','kiran@gmail.com','8/12/1993','K K','RK','male','unMarried',
'California','James Street','Software Engineer','Software Engineer',
'Java HTML CSS','1010101010','1010101010','India','english','10',
'$2a$11$EXwpna1MlFFlKW5ut1iVi.AoeIulkPPmcOHFO8pOoQt1IYU9COU0m'),
(12,'Saikumar','sai@gmail.com','20/06/1993','Sai RK','Sai AK','male','unMarried',
'California','US','Software Engineer','Software Engineer',
'Java HTML CSS AWS','8888888111','8888888111','India','english','8',
'$2a$11$pzWNzzR.HUkHzz2zhAgqOeCl0WaTgY33NxxJ7n0l.rnEqjB9JO7vy'),
(13,'RamSai','ram@gmail.com',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,
'$2a$11$6BSmYPrT8I8b9yHmx.uTRu/QxnQM2vhZYQa8mR33aReWA4WFihyGK');

-- =======================
-- USER_ROLE
-- =======================
CREATE TABLE `user_role` (
  `user_id` INT NOT NULL,
  `role_id` INT NOT NULL,
  PRIMARY KEY (`user_id`, `role_id`)
);

INSERT INTO `user_role` VALUES
(7,1),(8,1),(9,1),(10,1),(11,1),(12,1),(13,1);