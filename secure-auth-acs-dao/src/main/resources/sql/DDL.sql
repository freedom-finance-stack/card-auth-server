CREATE DATABASE `fps_acs`;

USE `fps_acs`;
CREATE TABLE IF NOT EXISTS `transaction`
(
    `id`                        varchar(36) PRIMARY KEY,
    `institution_id`            varchar(36)                                                                                                                                                                     NOT NULL,
    `message_category`          ENUM ('PA', 'NPA', 'PVPA', 'PVNPA', 'NW', 'TW', 'IT', 'AT', 'AW', 'DI', 'II' )                                                                                                  NOT NULL,
    `message_version`           varchar(10),
    `challenge_mandated`        bool,
    `transaction_status`        ENUM ('CREATED','SUCCESS','FAILED','UNABLE_TO_AUTHENTICATE','ATTEMPT','CHALLANGE_REQUIRED','CHALLANGE_REQUIRED_DECOUPLED','REJECTED','INFORMATIONAL')                           NOT NULL,
    `transaction_status_reason` varchar(80),
    `phase`                     ENUM ('AREQ','ARES','CREQ','RETRY_CREQ','CRES','RREQ','REDIRECT','RESEND_OTP','AUTH_INITIATE','GENERATE_OTP','AUTH_RESULT','SEAMLESS_GENERATE_OTP','VERIFY_OTP','RRES','ERROR') NOT NULL,
    `threeds_session_data`      varchar(1024),
    `auth_value`                varchar(200),
    `device_channel`            varchar(10)                                                                                                                                                                     NOT NULL,
    `device_name`               varchar(20),
    `interaction_count`         int,
    `error_code`                varchar(20),
    `created_at`                timestamp                                                                                                                                                                       NOT NULL,
    `modified_at`               timestamp,
    `deleted_at`                timestamp
);

CREATE TABLE IF NOT EXISTS `transaction_browser_detail`
(
    `transaction_id`     varchar(36) PRIMARY KEY,
    `javascript_enabled` boolean,
    `ip`                 varchar(60),
    `accept_header`      varchar(200),
    `created_at`         timestamp NOT NULL,
    `modified_at`        timestamp,
    `deleted_at`         timestamp
);

CREATE TABLE IF NOT EXISTS `transaction_sdk_detail`
(
    `transaction_id`     varchar(36) PRIMARY KEY,
    `sdk_transaction_id` varchar(36),
    `created_at`         timestamp NOT NULL,
    `modified_at`        timestamp,
    `deleted_at`         timestamp
);

CREATE TABLE IF NOT EXISTS `transaction_merchant`
(
    `transaction_id`        varchar(36) PRIMARY KEY,
    `acquirer_merchant_id`  varchar(100),
    `merchant_name`         varchar(200) NOT NULL,
    `merchant_country_code` smallint,
    `created_at`            timestamp    NOT NULL,
    `modified_at`           timestamp,
    `deleted_at`            timestamp
);

CREATE TABLE IF NOT EXISTS `transaction_message_type_detail`
(
    `id`                 varchar(36) PRIMARY KEY,
    `transaction_id`     varchar(36) NOT NULL,
    `message`            json,
    `received_timestamp` timestamp,
    `sent_timestamp`     timestamp,
    `message_type`       ENUM ('AReq', 'Ares', 'CReq', 'CRes', 'RReq', 'RRes'),
    `created_at`         timestamp NOT NULL,
    `modified_at`        timestamp,
    `deleted_at`         timestamp
);
# Create Index on transaction_id
CREATE INDEX `transaction_message_type_detail_transaction_id_idx` ON `transaction_message_type_detail` (`transaction_id`);

CREATE TABLE IF NOT EXISTS `transaction_reference_detail`
(
    `transaction_id`                  varchar(36) PRIMARY KEY ,
    `threeds_server_transaction_id`   varchar(36),
    `threeds_server_reference_number` varchar(36),
    `ds_transaction_id`               varchar(36),
    `created_at`                      timestamp NOT NULL,
    `modified_at`                     timestamp NOT NULL,
    `deleted_at`                      timestamp default NULL
);

CREATE TABLE `institution_acs_url`
(
    `institution_id` varchar(5)  NOT NULL,
    `device_channel` varchar(10) NOT NULL,
    `network_code`   varchar(2)  NOT NULL,
    `challenge_url`  varchar(400) DEFAULT NULL,
    `created_at`     timestamp   NOT NULL,
    `created_by`     varchar(40) NOT NULL,
    `modified_at`    timestamp,
    `modified_by`    varchar(40),
    `deleted_at`     timestamp default NULL,
    `deleted_by`     varchar(40),
    PRIMARY KEY (`institution_id`, `device_channel`, `network_code`)
);

CREATE TABLE IF NOT EXISTS `transaction_purchase_detail`
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

CREATE TABLE IF NOT EXISTS `transaction_card_detail`
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

CREATE TABLE IF NOT EXISTS `institution`
(
    `id`               varchar(36) PRIMARY KEY,
    `name`             varchar(100),
    `short_name`       varchar(20),
    `iso_country_code` smallint,
    `timezone`         varchar(25),
    `status`           ENUM ('ACTIVE', 'INACTIVE') NOT NULL,
    `created_at`       timestamp                   NOT NULL,
    `created_by`       varchar(40)                 NOT NULL,
    `modified_at`      timestamp,
    `modified_by`      varchar(40),
    `deleted_at`       timestamp default NULL,
    `deleted_by`       varchar(40)
);

CREATE TABLE IF NOT EXISTS `hsm_config`
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

CREATE TABLE IF NOT EXISTS `card_range`
(
    `id`                      varchar(36) PRIMARY KEY,
    `range_group_id`          varchar(36),
    `start_range`             decimal(25),
    `end_range`               decimal(25),
    `attempt_allowed`         tinyint,
    `block_on_exceed_attempt` tinyint                             NOT NULL,
    `status`                  ENUM ('ACTIVE', 'INACTIVE')         NOT NULL,
    `card_type`               ENUM ('CREDIT', 'DEBIT', 'PREPAID') NOT NULL,
    `auth_type`               ENUM ('NOCHALLENGE', 'CHALLENGE', 'RBA')  NOT NULL,
    `description`             varchar(255),
    `whitelisting_allowed`    tinyint,
    `card_details_store`      enum ('ACS', 'API_1'),
    `created_at`              timestamp                           NOT NULL,
    `modified_at`             timestamp                           NOT NULL,
    `deleted_at`              timestamp default NULL,
    `created_by`              varchar(40)                         NOT NULL,
    `modified_by`             varchar(40),
    `deleted_by`              varchar(40)
);

CREATE TABLE IF NOT EXISTS `card_range_group`
(
    `id`             varchar(36) PRIMARY KEY,
    `institution_id` varchar(36),
    `name`           varchar(50),
    `description`    varchar(150),
    `created_at`     timestamp   NOT NULL,
    `modified_at`    timestamp   NOT NULL,
    `deleted_at`     timestamp default NULL,
    `created_by`     varchar(40) NOT NULL,
    `modified_by`    varchar(40),
    `deleted_by`     varchar(40)
);

CREATE TABLE IF NOT EXISTS `feature`
(
    `id`          varchar(36) PRIMARY KEY,
    `entity_type` varchar(20),
    `entity_id`   varchar(36) NOT NULL,
    `active`      bool        NOT NULL,
    `name`        varchar(20),
    `properties`  json,
    `created_at`  timestamp   NOT NULL,
    `created_by`  varchar(40) NOT NULL,
    `modified_at` timestamp,
    `modified_by` varchar(40),
    `deleted_at`  timestamp,
    `deleted_by`  varchar(40)
);

CREATE TABLE IF NOT EXISTS `cardholder`
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

CREATE TABLE IF NOT EXISTS `card_detail`
(
    `id`             varchar(36) PRIMARY KEY,
    `cardholder_id`  varchar(36) NOT NULL,
    `card_range_id`       varchar(36) NOT NULL,
    `institution_id` varchar(36) NOT NULL,
    `card_number`    varchar(25),
    `card_expiry`    varchar(4),
    `blocked`        bool,
    `network_code`   varchar(4),
    `created_at`     timestamp   NOT NULL,
    `modified_at`    timestamp   NOT NULL,
    `deleted_at`     timestamp default NULL,
    `created_by`     varchar(40) NOT NULL,
    `modified_by`    varchar(40),
    `deleted_by`     varchar(40)
);

CREATE TABLE IF NOT EXISTS `network`
(
    `id`          varchar(36) PRIMARY KEY,
    `code`        tinyint     NOT NULL,
    `name`        varchar(50) NOT NULL,
    `created_at`  timestamp   NOT NULL,
    `created_by`  varchar(40) NOT NULL,
    `modified_at` timestamp,
    `modified_by` varchar(40),
    `deleted_at`  timestamp default NULL,
    `deleted_by`  varchar(40)
);

CREATE TABLE IF NOT EXISTS `otp_information`
(
    `id`          varchar(36) PRIMARY KEY,
    `unique_id`   varchar(36),
    `created_at`  timestamp NOT NULL,
    `modified_at` timestamp NOT NULL,
    `deleted_at`  timestamp default NULL
);

CREATE TABLE IF NOT EXISTS `otp`
(
    `id`                 varchar(36) PRIMARY KEY,
    `channel`            varchar(36),
    `otp_information_id` varchar(36) NOT NULL,
    `destination`        varchar(36),
    `otp_status`         varchar(36),
    `response`           varchar(36),
    `provider`           varchar(36),
    `attempts`           int,
    `created_at`         timestamp   NOT NULL,
    `modified_at`        timestamp   NOT NULL,
    `deleted_at`         timestamp default NULL
);

CREATE TABLE IF NOT EXISTS `otp_detail`
(
    `id`                  varchar(36),
    `otp_id`              varchar(36) NOT NULL,
    `transaction_id`      varchar(36) NOT NULL,
    `verification_status` ENUM ('CREATED', 'EXPIRED', 'VERIFIED', 'ATTEMPTED'),
    `resend_count`        int,
    `created_at`          timestamp   NOT NULL,
    `modified_at`         timestamp   NOT NULL,
    `deleted_at`          timestamp default NULL
);
