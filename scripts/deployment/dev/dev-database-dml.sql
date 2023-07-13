USE `fps_acs`;

LOCK TABLES `card_detail` WRITE;
INSERT INTO `card_detail` VALUES ('1','1','R1','I1','4016000000000018','0525',0,NULL,NOW(),NOW(),NULL,'dev-user','dev-user',NULL);
UNLOCK TABLES;

LOCK TABLES `card_range` WRITE;
INSERT INTO `card_range` VALUES ('R1','RG1',4016000000000000,4016000000000100,3,1,'ACTIVE','CREDIT','NO_CHALLENGE','Visa Testing',1,'ACS',1,NOW(),NOW(),NULL,'dev-user','dev-user',NULL);
UNLOCK TABLES;

LOCK TABLES `card_range_group` WRITE;
INSERT INTO `card_range_group` VALUES ('RG1','I1','Platinum','Testing',NOW(),NOW(),NULL,'dev-user','dev-user',NULL);
UNLOCK TABLES;

LOCK TABLES `cardholder` WRITE;
INSERT INTO `cardholder` VALUES ('1','9988776655','dev-user@mail.com','01-01-2023','dev-user',NOW(),NOW(),NULL,'dev-user','dev-user',NULL);
UNLOCK TABLES;

LOCK TABLES `institution` WRITE;
INSERT INTO `institution` VALUES ('I1','HDFC Bank','HDFC',356,'Asia/Kolkata','ACTIVE',NOW(),'dev-user',NOW(),'dev-user',NULL,NULL);
UNLOCK TABLES;

LOCK TABLES `institution_acs_url` WRITE;
INSERT INTO `institution_acs_url` VALUES ('I1','02',1,'http://localhost:8080/v1/transaction/challenge-request',NOW(),'dev-user',NOW(),'dev-user',NULL,NULL);
UNLOCK TABLES;

LOCK TABLES `network` WRITE;
INSERT INTO `network` VALUES ('01',1,'VISA',NOW(),'dev-user',NOW(),'dev-user',NULL,NULL),('02',2,'MASTERCARD',NOW(),'dev-user',NOW(),'dev-user',NULL,NULL);
UNLOCK TABLES;