CREATE DATABASE IF NOT EXISTS `cas_db`;
USE `cas_db`;

DROP TABLE IF EXISTS `transaction`;
CREATE TABLE `transaction`
(
    `id`                        varchar(36) PRIMARY KEY,
    `institution_id`            varchar(36),
    `card_range_id`             varchar(36),
    `message_category`          ENUM ('PA', 'NPA', 'PVPA', 'PVNPA', 'NW', 'TW', 'IT', 'AT', 'AW', 'DI', 'II' ),
    `message_version`           varchar(10),
    `challenge_mandated`        bool,
    `transaction_status`        ENUM ('CREATED','SUCCESS','FAILED','UNABLE_TO_AUTHENTICATE','ATTEMPT','CHALLENGE_REQUIRED','CHALLENGE_REQUIRED_DECOUPLED','REJECTED','INFORMATIONAL') NOT NULL,
    `transaction_status_reason` varchar(80),
    `phase`                     ENUM ('AREQ','ARES','AERROR','CREQ','CRES','RREQ','CDRES','CVREQ','ERROR') NOT NULL,
    `threeds_session_data`      varchar(1024),
    `three_ri_ind`              varchar(2),
    `auth_value`                varchar(200),
    `eci`                       varchar(3),
    `device_channel`            varchar(10),
    `device_name`               varchar(20),
    `authentication_type`       int,
    `interaction_count`         int DEFAULT 0,
    `resend_count`              int DEFAULT 0,
    `challenge_cancel_ind`      varchar(2),
    `error_code`                varchar(20),
    `created_at`                timestamp NOT NULL,
    `modified_at`               timestamp,
    `deleted_at`                timestamp
);

DROP TABLE IF EXISTS `transaction_browser_detail`;
CREATE TABLE `transaction_browser_detail`
(
    `transaction_id`     varchar(36) PRIMARY KEY,
    `javascript_enabled` boolean,
    `ip`                 varchar(60),
    `accept_header`      varchar(200),
    `created_at`         timestamp NOT NULL,
    `modified_at`        timestamp,
    `deleted_at`         timestamp
);

DROP TABLE IF EXISTS `transaction_sdk_detail`;
CREATE TABLE `transaction_sdk_detail`
(
    `transaction_id`       varchar(36) PRIMARY KEY,
    `sdk_trans_id`         varchar(36) DEFAULT NULL,
    `sdk_app_id`           varchar(36) DEFAULT NULL,
    `sdk_reference_number` varchar(32) DEFAULT NULL,
    `acs_interface`        char(2) DEFAULT NULL,
    `acs_ui_type`          char(2) DEFAULT NULL,
    `acs_secret_key`       LONGTEXT NULL,
    `encryption_algo`      VARCHAR(45) DEFAULT NULL,
    `device_info`          text,
    `acs_counter_a_to_s` char(3) DEFAULT '000',
    `created_at`           timestamp NOT NULL,
    `modified_at`          timestamp,
    `deleted_at`           timestamp
);

DROP TABLE IF EXISTS `transaction_merchant`;
CREATE TABLE `transaction_merchant`
(
    `transaction_id`        varchar(36) PRIMARY KEY,
    `acquirer_merchant_id`  varchar(100),
    `merchant_name`         varchar(200) NOT NULL,
    `merchant_country_code` smallint,
    `created_at`            timestamp    NOT NULL,
    `modified_at`           timestamp,
    `deleted_at`            timestamp
);

DROP TABLE IF EXISTS `transaction_message_log`;
CREATE TABLE `transaction_message_log`
(
    `id`             varchar(36) PRIMARY KEY,
    `transaction_id` varchar(36) NOT NULL,
    `message`        json,
    `message_type`   ENUM ('AReq', 'ARes', 'CReq', 'CRes', 'RReq', 'RRes', 'CDRes', 'CVReq'),
    `created_at`     timestamp   NOT NULL,
    `modified_at`    timestamp,
    `deleted_at`     timestamp
);

-- /* Create Index on transaction_id */
CREATE INDEX `transaction_message_type_detail_transaction_id_idx` ON transaction_message_log (`transaction_id`);

DROP TABLE IF EXISTS `transaction_reference_detail`;
CREATE TABLE `transaction_reference_detail`
(
    `transaction_id`                  varchar(36) PRIMARY KEY,
    `threeds_server_transaction_id`   varchar(36),
    `threeds_server_reference_number` varchar(36),
    `ds_transaction_id`               varchar(36),
    `ds_url`                          varchar(2048),
    `threeds_requestor_challenge_ind` char(2),
    `whitelisting_data_entry` char(1),
    `notification_url`                varchar(256),
    `created_at`                      timestamp NOT NULL,
    `modified_at`                     timestamp NOT NULL,
    `deleted_at`                      timestamp default NULL
);

DROP TABLE IF EXISTS `transaction_cardholder_detail`;
CREATE TABLE `transaction_cardholder_detail`
(
    `transaction_id` varchar(36) PRIMARY KEY,
    `mobile_number`  varchar(20),
    `email_id`       varchar(100),
    `name`           varchar(50),
    `created_at`     timestamp NOT NULL,
    `modified_at`    timestamp NOT NULL,
    `deleted_at`     timestamp default NULL
);

DROP TABLE IF EXISTS `transaction_purchase_detail`;
CREATE TABLE `transaction_purchase_detail`
(
    `transaction_id`     varchar(36) PRIMARY KEY,
    `purchase_amount`    varchar(255),
    `purchase_currency`  varchar(255),
    `purchase_exponent`  tinyint,
    `purchase_timestamp` timestamp,
    `pay_token_ind`      boolean,
    `created_at`         timestamp NOT NULL,
    `modified_at`        timestamp NOT NULL,
    `deleted_at`         timestamp default NULL
);

DROP TABLE IF EXISTS `transaction_card_detail`;
CREATE TABLE `transaction_card_detail`
(
    `transaction_id`  varchar(36) PRIMARY KEY,
    `card_number`     varchar(40) NOT NULL,
    `cardholder_name` varchar(120),
    `card_expiry`     varchar(10),
    `network_code`    tinyint,
    `created_at`      timestamp   NOT NULL,
    `modified_at`     timestamp   NOT NULL,
    `deleted_at`      timestamp default NULL
);

DROP TABLE IF EXISTS `institution`;
CREATE TABLE `institution`
(
    `id`               varchar(36) PRIMARY KEY,
    `name`             varchar(100),
    `short_name`       varchar(20),
    `iso_country_code` smallint,
    `timezone`         varchar(25),
    `status`           ENUM ('ACTIVE', 'INACTIVE') NOT NULL,
    `created_at`       timestamp   NOT NULL,
    `created_by`       varchar(40) NOT NULL,
    `modified_at`      timestamp,
    `modified_by`      varchar(40),
    `deleted_at`       timestamp   default NULL,
    `deleted_by`       varchar(40),
    `message_version`  varchar(10) default NULL
);

DROP TABLE IF EXISTS `hsm_config`;
CREATE TABLE `hsm_config`
(
    `institution_id`         varchar(36) NOT NULL,
    `network_id`             varchar(36) NOT NULL,
    `version`                varchar(50),
    `hsm_slot_id`            varchar(36),
    `hsm_usr_pwd`            varchar(40),
    `hsm_root_cert_key`      varchar(30),
    `hsm_inter_cert_key`     varchar(30),
    `hsm_credit_cert_key`    varchar(30),
    `hsm_credit_signer_key`  varchar(30),
    `hsm_credit_cvv_cvc_key` varchar(30),
    `hsm_debit_cert_key`     varchar(30),
    `hsm_debit_signer_key`   varchar(30),
    `hsm_debit_cvv_cvc_key`  varchar(30),
    `keystore`               VARCHAR(150),
    `keypass`                VARCHAR(100),
    `usr_terminal`           varchar(30),
    `created_at`             timestamp   NOT NULL,
    `modified_at`            timestamp   NOT NULL,
    `deleted_at`             timestamp default NULL,
    `created_by`             varchar(40) NOT NULL,
    `modified_by`            varchar(40),
    `deleted_by`             varchar(40)
);

DROP TABLE IF EXISTS `card_range`;
CREATE TABLE `card_range`
(
    `id`                 varchar(36) PRIMARY KEY,
    `institution_id`     varchar(36),
    `start_range`        decimal(25),
    `end_range`          decimal(25),
    `status`             ENUM ('ACTIVE', 'INACTIVE')               NOT NULL,
    `card_type`          ENUM ('PREPAID', 'CREDIT', 'DEBIT')       NOT NULL,
    `risk_flag`          ENUM ('NO_CHALLENGE', 'CHALLENGE', 'RBA', 'INFORMATIONAL', 'DECOUPLED_CHALLENGE') NOT NULL,
    `description`        varchar(255),
    `card_details_store` enum ('ACS', 'API_1', 'MOCK'),
    `network_code`       tinyint,
    `created_at`         timestamp   NOT NULL,
    `modified_at`        timestamp   NOT NULL,
    `deleted_at`         timestamp default NULL,
    `created_by`         varchar(40) NOT NULL,
    `modified_by`        varchar(40),
    `deleted_by`         varchar(40)
);

DROP TABLE IF EXISTS `card_range_group`;
CREATE TABLE `card_range_group`
(
    `id`          varchar(36) PRIMARY KEY,
    `name`        varchar(50),
    `description` varchar(150),
    `created_at`  timestamp   NOT NULL,
    `modified_at` timestamp   NOT NULL,
    `deleted_at`  timestamp default NULL,
    `created_by`  varchar(40) NOT NULL,
    `modified_by` varchar(40),
    `deleted_by`  varchar(40)
);

DROP TABLE IF EXISTS `feature`;
CREATE TABLE `feature`
(
    `id`          varchar(36) PRIMARY KEY,
    `entity_type` ENUM ('INSTITUTION', 'CARD_RANGE', 'CARD_RANGE_GROUP')                      NOT NULL,
    `entity_id`   varchar(36)                                                                 NOT NULL,
    `active`      bool                                                                        NOT NULL,
    `name`        ENUM ('CHALLENGE_AUTH_TYPE', 'CHALLENGE_ATTEMPT', 'OTP', 'PASSWORD', 'OOB', 'RENDERING_TYPE') NOT NULL,
    `properties`  varchar(500)                                                                NOT NULL,
    `created_at`  timestamp                                                                   NOT NULL,
    `created_by`  varchar(40)                                                                 NOT NULL,
    `modified_at` timestamp,
    `modified_by` varchar(40),
    `deleted_at`  timestamp,
    `deleted_by`  varchar(40)
);
CREATE INDEX feature_entity_type_entity_id_name ON feature (name, entity_type, entity_id);

DROP TABLE IF EXISTS `cardholder`;
CREATE TABLE `cardholder`
(
    `id`            varchar(36) PRIMARY KEY,
    `mobile_number` varchar(20),
    `email_id`      varchar(100),
    `dob`           varchar(10),
    `name`          varchar(50),
    `created_at`    timestamp   NOT NULL,
    `modified_at`   timestamp   NOT NULL,
    `deleted_at`    timestamp default NULL,
    `created_by`    varchar(40) NOT NULL,
    `modified_by`   varchar(40),
    `deleted_by`    varchar(40)
);

DROP TABLE IF EXISTS `card_detail`;
CREATE TABLE `card_detail`
(
    `id`             varchar(36) PRIMARY KEY,
    `cardholder_id`  varchar(36) NOT NULL,
    `institution_id` varchar(36) NOT NULL,
    `card_number`    varchar(25),
    `card_expiry`    varchar(4),
    `blocked`        bool,
    `created_at`     timestamp   NOT NULL,
    `modified_at`    timestamp   NOT NULL,
    `deleted_at`     timestamp default NULL,
    `created_by`     varchar(40) NOT NULL,
    `modified_by`    varchar(40),
    `deleted_by`     varchar(40)
);

DROP TABLE IF EXISTS `admin_user`;
CREATE TABLE `admin_user`
(
    `id`              varchar(36) PRIMARY KEY,
    `username`        varchar(50)  NOT NULL,
    `email_id`        varchar(100) NOT NULL,
    `mobile_number`   varchar(20) DEFAULT NULL,
    `password`        varchar(50)  NOT NULL,
    `status`          ENUM ('ACTIVE', 'INACTIVE') NOT NULL,
    `last_login_dttm` timestamp   DEFAULT NULL,
    `created_at`      timestamp    NOT NULL,
    `modified_at`     timestamp    NOT NULL,
    `deleted_at`      timestamp   DEFAULT NULL,
    `created_by`      varchar(40)  NOT NULL,
    `modified_by`     varchar(40)  NOT NULL,
    `deleted_by`      varchar(40) DEFAULT NULL
);

DROP TABLE IF EXISTS `admin_user_institution`;
CREATE TABLE `admin_user_institution`
(
    `id`             varchar(36) PRIMARY KEY,
    `user_id`        varchar(36) NOT NULL,
    `institution_id` varchar(36) DEFAULT NULL,
    `created_at`     timestamp   NOT NULL,
    `modified_at`    timestamp   NOT NULL,
    `deleted_at`     timestamp   DEFAULT NULL
);

DROP TABLE IF EXISTS `institution_meta`;
CREATE TABLE `institution_meta`
(
    `institution_id` varchar(36) PRIMARY KEY,
    `logo_data`      blob        NOT NULL,
    `logo_filename`  varchar(50) NOT NULL,
    `created_at`     timestamp   NOT NULL,
    `modified_at`    timestamp   NOT NULL,
    `deleted_at`     timestamp   DEFAULT NULL,
    `created_by`     varchar(40) NOT NULL,
    `modified_by`    varchar(40) NOT NULL,
    `deleted_by`     varchar(40) DEFAULT NULL
);

DROP TABLE IF EXISTS `notification_detail`;
CREATE TABLE `notification_detail`
(
    `id`          varchar(36) PRIMARY KEY,
    `entity_type` varchar(36),
    `entity_id`   varchar(36),
    `channel`     varchar(36),
    `destination` varchar(36),
    `response`    varchar(36),
    `provider`    varchar(36),
    `status`      ENUM ('PENDING', 'SENT', 'FAILED'),
    `created_at`  timestamp NOT NULL,
    `modified_at` timestamp NOT NULL,
    `deleted_at`  timestamp default NULL
);

DROP TABLE IF EXISTS `notification_detail`;
CREATE TABLE `notification_detail`
(
    `id`          varchar(36) PRIMARY KEY,
    `entity_type` varchar(36),
    `entity_id`   varchar(36),
    `channel`     varchar(36),
    `destination` varchar(36),
    `response`    varchar(36),
    `provider`    varchar(36),
    `status`      ENUM ('PENDING', 'SENT', 'FAILED'),
    `created_at`  timestamp NOT NULL,
    `modified_at` timestamp NOT NULL,
    `deleted_at`  timestamp default NULL
);

DROP TABLE IF EXISTS `signer_detail`;
CREATE TABLE `signer_detail`
(
    `institution_id`      varchar(36) NOT NULL,
    `network_code`        varchar(2) NOT NULL,
    `keystore`            varchar(150),
    `keypass`             varchar(100),
    `signer_cert_key`     varchar(30),
    `signer_key_pair`     varchar(30),
    `root_cert_key`       varchar(30),
    `inter_cert_key`      varchar(30),
    `created_at`          timestamp   NOT NULL,
    `created_by`          varchar(40) NOT NULL,
    `modified_at`         timestamp   NOT NULL,
    `modified_by`         varchar(40) NOT NULL,
    `deleted_at`          timestamp   DEFAULT NULL,
    `deleted_by`          varchar(40) DEFAULT NULL
);

DROP TABLE IF EXISTS `institution_ui_config`;
CREATE TABLE `institution_ui_config`
(
    `institution_id`      varchar(36) NOT NULL,
    `auth_type` ENUM('OTP', 'PASSWORD', 'NetBankingOOB', 'Decoupled'),
    `ui_type` ENUM('TEXT', 'SINGLE_SELECT', 'MULTI_SELECT', 'OOB', 'HTML_OTHER'),
    `display_page` varchar(99) DEFAULT NULL,
    `challenge_info_header` varchar(45) DEFAULT NULL,
    `challenge_info_label` varchar(45) DEFAULT NULL,
    `challenge_info_text` varchar(256) DEFAULT NULL,
    `expand_info_label` varchar(45) DEFAULT NULL,
    `expand_info_text` varchar(256) DEFAULT NULL,
    `submit_authentication_label` varchar(45) DEFAULT NULL,
    `resend_information_label` varchar(45) DEFAULT NULL,
    `why_info_label` varchar(45) DEFAULT NULL,
    `why_info_text` varchar(256) DEFAULT NULL,
    `whitelisting_info_text` varchar(64) DEFAULT NULL,
    `created_at`     timestamp   NOT NULL,
    `created_by`     varchar(40) NOT NULL,
    `modified_at`    timestamp   NOT NULL,
    `modified_by`    varchar(40) NOT NULL,
    `deleted_at`     timestamp   DEFAULT NULL,
    `deleted_by`     varchar(40) DEFAULT NULL
);

DROP TABLE IF EXISTS `otp_transaction_detail`;
CREATE TABLE `otp_transaction_detail`
(
    `id`                  varchar(36),
    `transaction_id`      varchar(36) NOT NULL,
    `value`               varchar(10) NOT NULL,
    `verification_status` ENUM ('CREATED', 'EXPIRED', 'VERIFIED', 'ATTEMPTED'),
    `created_at`          timestamp   NOT NULL,
    `modified_at`         timestamp   NOT NULL,
    `deleted_at`          timestamp default NULL
);
CREATE INDEX `otp_transaction_detail_transaction_id_idx` ON otp_transaction_detail (`transaction_id`);
