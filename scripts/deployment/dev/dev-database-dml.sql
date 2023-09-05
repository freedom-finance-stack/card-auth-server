USE `cas_db`;

LOCK TABLES `card_detail` WRITE;
INSERT INTO `card_detail` VALUES ('1', '1', 'R1','I1','4016000000000018','0525',0,NULL,NOW(),NOW(),NULL,'dev-user','dev-user',NULL);
INSERT INTO `card_detail` VALUES ('2', '1', 'R2','I1','7654310438720050','0535',0,NULL,NOW(),NOW(),NULL,'dev-user','dev-user',NULL);
INSERT INTO `card_detail` VALUES ('3', '1', 'R3','I1','7654350720400013','0535',0,NULL,NOW(),NOW(),NULL,'dev-user','dev-user',NULL);
INSERT INTO `card_detail` VALUES ('4', '1', 'R5','I1','5116000000000018','0535',0,NULL,NOW(),NOW(),NULL,'dev-user','dev-user',NULL);
INSERT INTO `card_detail` VALUES ('5', '1', 'R2','I1','7654310438700047','0535',0,NULL,NOW(),NOW(),NULL,'dev-user','dev-user',NULL);
INSERT INTO `card_detail` VALUES ('6', '1', 'R2','I1','7654310438700062','0535',0,NULL,NOW(),NOW(),NULL,'dev-user','dev-user',NULL);
INSERT INTO `card_detail` VALUES ('7', '1', 'R2','I1','7654310438700070','0535',0,NULL,NOW(),NOW(),NULL,'dev-user','dev-user',NULL);
INSERT INTO `card_detail` VALUES ('8', '1', 'R2','I1','7654310438746321','0535',0,NULL,NOW(),NOW(),NULL,'dev-user','dev-user',NULL);
INSERT INTO `card_detail` VALUES ('9', '1', 'R2','I1','7654310438700096','0535',0,NULL,NOW(),NOW(),NULL,'dev-user','dev-user',NULL);
UNLOCK TABLES;

LOCK TABLES `card_range` WRITE;
INSERT INTO `card_range` VALUES ('R1', 'I1', 4016000000000000, 4016000000000100, 'ACTIVE', 'CREDIT', 'NO_CHALLENGE', 'Visa Testing', 'ACS', 1, NOW(), NOW(), NULL, 'dev-user', 'dev-user', NULL);
INSERT INTO `card_range` VALUES ('R5', 'I1', 5116000000000000, 5516000000000100, 'ACTIVE', 'CREDIT', 'NO_CHALLENGE', 'MasterCard Testing', 'ACS', 2, NOW(), NOW(), NULL, 'dev-user', 'dev-user', NULL);
INSERT INTO `card_range` VALUES ('R2', 'I1', 7654310400000000, 7654310499999999, 'ACTIVE', 'CREDIT', 'NO_CHALLENGE', '3DS Portal Frictionless Testing', 'ACS', 1, NOW(), NOW(), NULL, 'dev-user', 'dev-user', NULL);
INSERT INTO `card_range` VALUES ('R3', 'I1', 7654350700000000, 7654350799999999, 'ACTIVE', 'CREDIT', 'NO_CHALLENGE', '3DS Portal Frictionless Testing', 'ACS', 1, NOW(), NOW(), NULL, 'dev-user', 'dev-user', NULL);
INSERT INTO `card_range` VALUES ('R4', 'I1', 7654360800000000, 7654360899999999, 'ACTIVE', 'CREDIT', 'NO_CHALLENGE', '3DS Portal Frictionless Testing', 'ACS', 1, NOW(), NOW(), NULL, 'dev-user', 'dev-user', NULL);
UNLOCK TABLES;

LOCK TABLES `card_range_group` WRITE;
INSERT INTO `card_range_group` VALUES ('RG1','I1','Platinum',NOW(),NOW(),NULL,'dev-user','dev-user',NULL);
UNLOCK TABLES;

LOCK TABLES `cardholder` WRITE;
INSERT INTO `cardholder` VALUES ('1','9988776655','dev-user@mail.com','01-01-2023','dev-user',NOW(),NOW(),NULL,'dev-user','dev-user',NULL);
UNLOCK TABLES;

LOCK TABLES `institution` WRITE;
INSERT INTO `institution` VALUES ('I1','HDFC Bank','HDFC',356,'Asia/Kolkata','ACTIVE',NOW(),'dev-user',NOW(),'dev-user',NULL,NULL);
UNLOCK TABLES;

LOCK TABLES `institution_acs_url` WRITE;
INSERT INTO `institution_acs_url` VALUES ('I1', '01', 1, 'http://localhost:8080/v1/transaction/challenge-request', NOW(), 'dev-user', NOW(), 'dev-user', NULL, NULL);
INSERT INTO `institution_acs_url` VALUES ('I1', '02', 1, 'http://localhost:8080/v1/transaction/challenge-request', NOW(), 'dev-user', NOW(), 'dev-user', NULL, NULL);
INSERT INTO `institution_acs_url` VALUES ('I1', '03', 1, 'http://localhost:8080/v1/transaction/challenge-request', NOW(), 'dev-user', NOW(), 'dev-user', NULL, NULL);
INSERT INTO `institution_acs_url` VALUES ('I1', '02', 2, 'http://localhost:8080/v1/transaction/challenge-request', NOW(), 'dev-user', NOW(), 'dev-user', NULL, NULL);
INSERT INTO `institution_acs_url` VALUES ('I1', '01', 2, 'http://localhost:8080/v1/transaction/challenge-request', NOW(), 'dev-user', NOW(), 'dev-user', NULL, NULL);
UNLOCK TABLES;

LOCK TABLES `network` WRITE;
INSERT INTO `network` VALUES ('01',1,'VISA',NOW(),'dev-user',NOW(),'dev-user',NULL,NULL),('02',2,'MASTERCARD',NOW(),'dev-user',NOW(),'dev-user',NULL,NULL);
UNLOCK TABLES;