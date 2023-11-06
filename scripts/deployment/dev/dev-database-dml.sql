USE `cas_db`;

LOCK TABLES `card_detail` WRITE;
INSERT INTO `card_detail` VALUES ('1', '1', 'R1','I1','4016000000000018','0525',  0, 1,NOW(),NOW(),NULL,'dev-user','dev-user',NULL);
INSERT INTO `card_detail` VALUES ('2', '1', 'R2','I1','7654310438720050','0535',  0, 1,NOW(),NOW(),NULL,'dev-user','dev-user',NULL);
INSERT INTO `card_detail` VALUES ('3', '1', 'R3','I1','7654350720400013','0535',  0, 1,NOW(),NOW(),NULL,'dev-user','dev-user',NULL);
INSERT INTO `card_detail` VALUES ('4', '1', 'R5','I1','5116000000000018','0535',  0, 1,NOW(),NOW(),NULL,'dev-user','dev-user',NULL);
INSERT INTO `card_detail` VALUES ('5', '1', 'R2','I1','7654310438700047','0535',  0, 1,NOW(),NOW(),NULL,'dev-user','dev-user',NULL);
INSERT INTO `card_detail` VALUES ('6', '1', 'R2','I1','7654310438700062','0535',  0, 1,NOW(),NOW(),NULL,'dev-user','dev-user',NULL);
INSERT INTO `card_detail` VALUES ('7', '1', 'R2','I1','7654310438700070','0535',  0, 1,NOW(),NOW(),NULL,'dev-user','dev-user',NULL);
INSERT INTO `card_detail` VALUES ('8', '1', 'R2','I1','7654310438746321','0535',  0, 1,NOW(),NOW(),NULL,'dev-user','dev-user',NULL);
INSERT INTO `card_detail` VALUES ('9', '1', 'R2','I1','7654310438700096','0535',  0, 1,NOW(),NOW(),NULL,'dev-user','dev-user',NULL);
INSERT INTO `card_detail` VALUES ('10', '1', 'R2','I1','7654310438700112','0535', 0, 1,NOW(),NOW(),NULL,'dev-user','dev-user',NULL);
INSERT INTO `card_detail` VALUES ('11', '1', 'R2','I1','7654310438700047','0535', 0, 1,NOW(),NOW(),NULL,'dev-user','dev-user',NULL);
INSERT INTO `card_detail` VALUES ('12', '1', 'R2','I1','7654310438700187','0535', 0, 1,NOW(),NOW(),NULL,'dev-user','dev-user',NULL);
INSERT INTO `card_detail` VALUES ('13', '1', 'R2','I1','7654310438700203','0535', 0, 1,NOW(),NOW(),NULL,'dev-user','dev-user',NULL);
INSERT INTO `card_detail` VALUES ('14', '1', 'R6','I1','7654320530001871','0535', 1, 1,NOW(),NOW(),NULL,'dev-user','dev-user',NULL);
INSERT INTO `card_detail` VALUES ('15', '1', 'R6','I1','7654320530000246','0535', 1, 1,NOW(),NOW(),NULL,'dev-user','dev-user',NULL);
INSERT INTO `card_detail` VALUES ('16', '1', 'R6','I1','7654320530000261','0535', 1, 1,NOW(),NOW(),NULL,'dev-user','dev-user',NULL);
INSERT INTO `card_detail` VALUES ('17', '1', 'R6','I1','7654320530000279','0535', 1, 1,NOW(),NOW(),NULL,'dev-user','dev-user',NULL);
INSERT INTO `card_detail` VALUES ('18', '1', 'R6','I1','7654320530046371','0535', 1, 1,NOW(),NOW(),NULL,'dev-user','dev-user',NULL);
INSERT INTO `card_detail` VALUES ('19', '1', 'R7','I1','4030000000000018','0535', 0, 1,NOW(),NOW(),NULL,'dev-user','dev-user',NULL);
INSERT INTO `card_detail` VALUES ('20', '1', 'R8','I1','765430270001402', '0535', 0, 1,NOW(),NOW(),NULL,'dev-user','dev-user',NULL);
UNLOCK TABLES;

LOCK TABLES `card_range` WRITE;
INSERT INTO `card_range` VALUES ('R1', 'I1', 4016000000000000, 4016000000000100, 'ACTIVE', 'CREDIT', 'NO_CHALLENGE', 'Visa Testing', 'ACS', 1, NOW(), NOW(), NULL, 'dev-user', 'dev-user', NULL);
INSERT INTO `card_range` VALUES ('R7', 'I1', 4030000000000000, 4030000000000100, 'ACTIVE', 'CREDIT', 'CHALLENGE', 'Visa Testing', 'ACS', 1, NOW(), NOW(), NULL, 'dev-user', 'dev-user', NULL);
INSERT INTO `card_range` VALUES ('R5', 'I1', 5116000000000000, 5516000000000100, 'ACTIVE', 'CREDIT', 'NO_CHALLENGE', 'MasterCard Testing', 'ACS', 2, NOW(), NOW(), NULL, 'dev-user', 'dev-user', NULL);
INSERT INTO `card_range` VALUES ('R2', 'I1', 7654310400000000, 7654310499999999, 'ACTIVE', 'CREDIT', 'NO_CHALLENGE', '3DS Portal Frictionless Testing', 'ACS', 1, NOW(), NOW(), NULL, 'dev-user', 'dev-user', NULL);
INSERT INTO `card_range` VALUES ('R3', 'I1', 7654350700000000, 7654350799999999, 'ACTIVE', 'CREDIT', 'NO_CHALLENGE', '3DS Portal Frictionless Testing', 'ACS', 1, NOW(), NOW(), NULL, 'dev-user', 'dev-user', NULL);
INSERT INTO `card_range` VALUES ('R4', 'I1', 7654360800000000, 7654360899999999, 'ACTIVE', 'CREDIT', 'NO_CHALLENGE', '3DS Portal Frictionless Testing', 'ACS', 1, NOW(), NOW(), NULL, 'dev-user', 'dev-user', NULL);
INSERT INTO `card_range` VALUES ('R6', 'I1', 7654320500000000, 7654320599999999, 'ACTIVE', 'CREDIT', 'NO_CHALLENGE', '3DS Portal Testing CardholderNotAuth', 'ACS', 1, NOW(), NOW(), NULL, 'dev-user', 'dev-user', NULL);
INSERT INTO `card_range` VALUES ('R8', 'I1', 765430270000000,  765430279999999,   'ACTIVE', 'CREDIT', 'CHALLENGE',    '3DS Portal Testing CardholderNotAuth', 'ACS', 1, NOW(), NOW(), NULL, 'dev-user', 'dev-user', NULL);
UNLOCK TABLES;

LOCK TABLES `card_range_group` WRITE;
INSERT INTO `card_range_group` VALUES ('RG1','I1','Platinum',NOW(),NOW(),NULL,'dev-user','dev-user',NULL);
UNLOCK TABLES;

LOCK TABLES `cardholder` WRITE;
INSERT INTO `cardholder` VALUES ('1','9988776655','dev-user@mail.com','01-01-2023','dev-user',NOW(),NOW(),NULL,'dev-user','dev-user',NULL);
UNLOCK TABLES;

LOCK TABLES `institution` WRITE;
INSERT INTO `institution` VALUES ('I1','HDFC Bank','HDFC',356,'Asia/Kolkata','ACTIVE',NOW(),'dev-user',NOW(),'dev-user',NULL,NULL, '2.2.0');
UNLOCK TABLES;

LOCK TABLES `institution_acs_url` WRITE;
INSERT INTO `institution_acs_url` VALUES ('I1', '01', 1, 'https://ec2-52-66-119-35.ap-south-1.compute.amazonaws.com/v1/transaction/challenge/app', NOW(), 'dev-user', NOW(), 'dev-user', NULL, NULL);
INSERT INTO `institution_acs_url` VALUES ('I1', '02', 1, 'https://ec2-52-66-119-35.ap-south-1.compute.amazonaws.com/v1/transaction/challenge/browser', NOW(), 'dev-user', NOW(), 'dev-user', NULL, NULL);
INSERT INTO `institution_acs_url` VALUES ('I1', '03', 1, 'https://ec2-52-66-119-35.ap-south-1.compute.amazonaws.com/v1/transaction/challenge/three-ri', NOW(), 'dev-user', NOW(), 'dev-user', NULL, NULL);
INSERT INTO `institution_acs_url` VALUES ('I1', '02', 2, 'https://ec2-52-66-119-35.ap-south-1.compute.amazonaws.com/v1/transaction/challenge/browser', NOW(), 'dev-user', NOW(), 'dev-user', NULL, NULL);
INSERT INTO `institution_acs_url` VALUES ('I1', '01', 2, 'https://ec2-52-66-119-35.ap-south-1.compute.amazonaws.com/v1/transaction/challenge/app', NOW(), 'dev-user', NOW(), 'dev-user', NULL, NULL);
UNLOCK TABLES;

LOCK TABLES `feature` WRITE;
INSERT INTO `feature` VALUES ('1', 'INSTITUTION', 'I1', 1, 'CHALLENGE_AUTH_TYPE', '{
    "purchase_amount_threshold" : 20000,
    "above_threshold_auth_type" :  "OTP",
    "default_auth_type" :  "OTP"
}', NOW(), 'dev-user', NOW(), 'dev-user', null, null);

INSERT INTO `feature` VALUES ('2', 'INSTITUTION', 'I1', 1, 'CHALLENGE_ATTEMPT', '{
    "attempt_threshold": 3,
    "resend_threshold": 3,
    "block_on_exceed_attempt": true,
    "whitelisting_allowed": true
}', NOW(), 'dev-user', NOW(), 'dev-user', null, null);

INSERT INTO `feature` VALUES ('3', 'INSTITUTION', 'I1', 1, 'OTP', '{ "length" : 4}', NOW(), 'dev-user', NOW(), 'dev-user', null, null);
UNLOCK TABLES;

LOCK TABLES `rendering_type_config` WRITE;
INSERT INTO `rendering_type_config` VALUES ('I1', 'R1', '01', '01', '1', '01', NOW(), 'dev-user', NOW(), 'dev-user', NULL, NULL);
INSERT INTO `rendering_type_config` VALUES ('I1', 'R2', '01', '01', '0', '01', NOW(), 'dev-user', NOW(), 'dev-user', NULL, NULL);
INSERT INTO `rendering_type_config` VALUES ('I1', 'R3', '01', '02', '1', '01', NOW(), 'dev-user', NOW(), 'dev-user', NULL, NULL);
INSERT INTO `rendering_type_config` VALUES ('I1', 'R4', '02', '03', '0', '01', NOW(), 'dev-user', NOW(), 'dev-user', NULL, NULL);
INSERT INTO `rendering_type_config` VALUES ('I1', 'R5', '02', '04', '1', '01', NOW(), 'dev-user', NOW(), 'dev-user', NULL, NULL);
INSERT INTO `rendering_type_config` VALUES ('I1', 'R6', '02', '05', '0', '01', NOW(), 'dev-user', NOW(), 'dev-user', NULL, NULL);
INSERT INTO `rendering_type_config` VALUES ('I1', 'R7', '02', '03', '1', '01', NOW(), 'dev-user', NOW(), 'dev-user', NULL, NULL);
INSERT INTO `rendering_type_config` VALUES ('I1', 'R8', '01', '05', '0', '01', NOW(), 'dev-user', NOW(), 'dev-user', NULL, NULL);
UNLOCK TABLES;