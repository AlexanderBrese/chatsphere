CREATE TABLE IF NOT EXISTS `Password` (
  `id`          INT NOT NULL AUTO_INCREMENT,
  -- UNICODE_CaseInsensitiv = Fuzzy String Search
  `hash`        VARCHAR(256) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'Argon2 Key-Direvation-Function',
  `updatedAt`   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB CHARACTER SET utf8mb4 COLLATE utf8mb4_bin;

CREATE TABLE IF NOT EXISTS `File` (
  `id`          INT NOT NULL AUTO_INCREMENT,
  `name`        VARCHAR(256) NOT NULL COLLATE utf8mb4_unicode_ci ,
  `size`        INT UNSIGNED NOT NULL COMMENT 'in Bytes MAX 2 GiB',
  `type`        VARCHAR(127) NOT NULL COMMENT 'Mime Type RFC 4288',
  `hash`        BINARY(32) NOT NULL COMMENT 'SHA256',
  `createdAt`   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB CHARACTER SET utf8mb4 COLLATE utf8mb4_bin;

CREATE TABLE IF NOT EXISTS `NotificationLevel` (
  `level`       INT NOT NULL AUTO_INCREMENT,
  `description` VARCHAR(32) NOT NULL,
  PRIMARY KEY (`level`),
  UNIQUE (`description`)
) ENGINE=InnoDB CHARACTER SET utf8mb4 COLLATE utf8mb4_bin;

INSERT INTO `NotificationLevel` (`description`) VALUES ('INHERIT'),('NOTIFY'), ('MUTE');

CREATE TABLE IF NOT EXISTS `VisibilityLevel` (
  `level`       INT NOT NULL AUTO_INCREMENT,
  `description` VARCHAR(32) NOT NULL,
  PRIMARY KEY (`level`),
  UNIQUE (`description`)
) ENGINE=InnoDB CHARACTER SET utf8mb4 COLLATE utf8mb4_bin;

INSERT INTO `VisibilityLevel` (`description`)
VALUES ('INHERIT'),('PRIVATE'),('CONTACT'),('PUBLIC'),('HIDDEN');

CREATE TABLE IF NOT EXISTS `Language` (
  `id`          INT NOT NULL AUTO_INCREMENT,
  `code`        VARCHAR(2) COMMENT 'two letter ISO 639-1',
  PRIMARY KEY (`id`),
  UNIQUE (`code`)
) ENGINE=InnoDB CHARACTER SET utf8mb4 COLLATE utf8mb4_bin;

CREATE TABLE IF NOT EXISTS `Location` (
  `id`          INT NOT NULL AUTO_INCREMENT,
  `country`     VARCHAR(2) COMMENT 'ISO 3166-1 alpha 2',
  `province`    VARCHAR(3) COMMENT 'ISO 3166-2',
  PRIMARY KEY (`id`),
  UNIQUE (`country`, `province`)
) ENGINE=InnoDB CHARACTER SET utf8mb4 COLLATE utf8mb4_bin;

CREATE TABLE IF NOT EXISTS `Email` (
  `id`          INT NOT NULL AUTO_INCREMENT,
  -- `mail`        TEXT NOT NULL COLLATE utf8mb4_unicode_ci,
  `mail`        TEXT CHARACTER SET ascii NOT NULL COLLATE ascii_general_ci,
  `hash`        BINARY(32) NOT NULL COMMENT 'SHA256',
  `verified`    BOOLEAN,
  `visibility`  INT NOT NULL,
  `createdAt`   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updatedAt`   TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE (`mail`(512)),
  INDEX (`hash`),
  FOREIGN KEY (`visibility`) REFERENCES `VisibilityLevel`(`level`) ON UPDATE CASCADE ON DELETE RESTRICT,
  CONSTRAINT `CHK_Mail` CHECK (LENGTH(`mail`)<=512)
) ENGINE=InnoDB CHARACTER SET utf8mb4 COLLATE utf8mb4_bin;

-- NOTE: Both email confirmation for registration and password recovery.
CREATE TABLE IF NOT EXISTS `EmailVerification` (
  `id`          INT NOT NULL AUTO_INCREMENT,
  `email`       INT NOT NULL,
  `token`       VARCHAR(32) NOT NULL COLLATE utf8mb4_general_ci,
  `lifetime`    INT NOT NULL COMMENT 'in Seconds',
  `createdAt`   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`email`) REFERENCES `Email`(`id`) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB CHARACTER SET utf8mb4 COLLATE utf8mb4_bin;

CREATE TABLE IF NOT EXISTS `Phone` (
  `id`          INT NOT NULL AUTO_INCREMENT,
  -- NOTE: Not all countries have an area code
  `number`      VARCHAR(32) NOT NULL COMMENT '+countrycode-area-line ITU E.164',
  `hash`        BINARY(32) NOT NULL COMMENT 'SHA256',
  `visibility`  INT NOT NULL,
  `createdAt`  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updatedAt`     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE (`hash`),
  FOREIGN KEY (`visibility`) REFERENCES `VisibilityLevel`(`level`) ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB CHARACTER SET utf8mb4 COLLATE utf8mb4_bin;

CREATE TABLE IF NOT EXISTS `Color` (
  `id`          INT NOT NULL AUTO_INCREMENT,
  `hue`         SMALLINT UNSIGNED NOT NULL COMMENT 'in Degree',
  `saturation`  TINYINT UNSIGNED NOT NULL COMMENT 'in Percent',
  `lightness`   TINYINT UNSIGNED NOT NULL COMMENT 'in Percent',
  `alpha`       TINYINT UNSIGNED NOT NULL COMMENT 'in Percent',
  PRIMARY KEY (`id`),
  UNIQUE (`hue`,`saturation`,`lightness`,`alpha`),
  CONSTRAINT `CHK_Hue` CHECK (0<=`hue` AND `hue`<=360),
  CONSTRAINT `CHK_Saturation` CHECK (0<=`saturation` AND `saturation`<=100),
  CONSTRAINT `CHK_Lightness` CHECK (0<=`lightness` AND `lightness`<=100),
  CONSTRAINT `CHK_Alpha` CHECK (0<=`alpha` AND `alpha`<=100)
) ENGINE=InnoDB CHARACTER SET utf8mb4 COLLATE utf8mb4_bin;

/*
CREATE TABLE IF NOT EXISTS `Status` (
  `id`          INT NOT NULL AUTO_INCREMENT,
  `markup`      BOOL,
  `content`     VARCHAR(256) COLLATE utf8mb4_unicode_ci,
  `createdAt`  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB CHARACTER SET utf8mb4 COLLATE utf8mb4_bin;
*/

CREATE TABLE IF NOT EXISTS `Message` (
  `id`          INT NOT NULL AUTO_INCREMENT,
  `markup`      BOOL NOT NULL,
  -- TEXT = 64 KiB / 4 UTF-8-Bytes = 16 KiB
  `content`     TEXT NOT NULL COMMENT '16384 Characters',
  `createdAt`   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedAt`   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB CHARACTER SET utf8mb4 COLLATE utf8mb4_bin;

CREATE TABLE IF NOT EXISTS `Profile` (
  `id`              INT NOT NULL AUTO_INCREMENT,
  -- TODO: Maybe a Prefix (instead of reverse Lookup)
  `name`            VARCHAR(32) NOT NULL COLLATE utf8mb4_unicode_ci,
  `displayName`     VARCHAR(256) NOT NULL COLLATE utf8mb4_unicode_ci COMMENT 'formatted name',
  `backgroundColor` INT,
  `icon`            INT,
  PRIMARY KEY (`id`),
  UNIQUE (`name`),
  FOREIGN KEY (`backgroundColor`) REFERENCES `Color`(`id`) ON UPDATE CASCADE ON DELETE SET NULL,
  FOREIGN KEY (`icon`) REFERENCES `File`(`id`) ON UPDATE CASCADE ON DELETE SET NULL
) ENGINE=InnoDB CHARACTER SET utf8mb4 COLLATE utf8mb4_bin;

CREATE TABLE IF NOT EXISTS `User` (
  `id`            INT NOT NULL AUTO_INCREMENT,
  `profile`       INT NOT NULL,
  `email`         INT,
  `authentication`INT,
  `phone`         INT,
  `status`        INT,
  `createdAt`     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedAt`     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY (`profile`),
  FOREIGN KEY (`profile`) REFERENCES `Profile`(`id`) ON UPDATE CASCADE ON DELETE CASCADE,
  FOREIGN KEY (`authentication`) REFERENCES Password(`id`) ON UPDATE CASCADE ON DELETE SET NULL,
  FOREIGN KEY (`email`) REFERENCES `Email`(`id`) ON UPDATE CASCADE ON DELETE SET NULL,
  FOREIGN KEY (`phone`) REFERENCES `Phone`(`id`) ON UPDATE CASCADE ON DELETE SET NULL,
  FOREIGN KEY (`status`) REFERENCES `Message`(`id`) ON UPDATE CASCADE ON DELETE SET NULL
) ENGINE=InnoDB CHARACTER SET utf8mb4 COLLATE utf8mb4_bin;

CREATE TABLE IF NOT EXISTS `UserPreferences` (
  `id`            INT NOT NULL AUTO_INCREMENT,
  `user`          INT NOT NULL,
  `language`      INT NOT NULL,
  `location`      INT,
  -- Verschiedene Benachrichtungslevel
  `notification`  INT,
  `linkPreview`   BOOLEAN,
  `retention`     INT COMMENT 'in days',
  PRIMARY KEY (`id`),
  UNIQUE (`user`),
  FOREIGN KEY (`user`) REFERENCES `User`(`id`) ON UPDATE CASCADE ON DELETE CASCADE,
  FOREIGN KEY (`language`) REFERENCES `Language`(`id`) ON UPDATE CASCADE ON DELETE RESTRICT,
  FOREIGN KEY (`location`) REFERENCES `Location`(`id`) ON UPDATE CASCADE ON DELETE RESTRICT,
  FOREIGN KEY (`notification`) REFERENCES `NotificationLevel`(`level`) ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB CHARACTER SET utf8mb4 COLLATE utf8mb4_bin;

CREATE TABLE IF NOT EXISTS `UserLegal` (
  `id`            INT NOT NULL AUTO_INCREMENT,
  `user`          INT NOT NULL,
  `agb`           TIMESTAMP,
  `privacy`       TIMESTAMP,
  `locked`        TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE (`user`),
  FOREIGN KEY (`user`) REFERENCES `User`(`id`) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB CHARACTER SET utf8mb4 COLLATE utf8mb4_bin;

CREATE TABLE IF NOT EXISTS `UserRelationship` (
  `id`          INT NOT NULL AUTO_INCREMENT,
  `owner`       INT NOT NULL,
  `refers`      INT NOT NULL,
  `petName`     VARCHAR(256) COLLATE utf8_unicode_ci,
  `blocked`     TIMESTAMP NULL,
  `createdAt`   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedAt`   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE (`owner`, `refers`),
  FOREIGN KEY (`owner`) REFERENCES `User`(`id`) ON UPDATE CASCADE ON DELETE CASCADE,
  FOREIGN KEY (`refers`) REFERENCES `User`(`id`) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB CHARACTER SET utf8mb4 COLLATE utf8mb4_bin;

-- NOTE: You can extend `UserRelationship` with a `hide` attribute and
-- create a `UserRelationshipCard` table to store address book data
-- without registering the user. (have to remove `referes` ON DELETE CASCADE)
-- Privacy-Sidenote: Save hash values of strangers' contacts and compare them with the database.
-- Extended Correspondingly existing master data tables.;

-- Tree-Structure - Closure Table
-- https://stackoverflow.com/a/4297827

CREATE TABLE IF NOT EXISTS `Category` (
  `id`          INT NOT NULL AUTO_INCREMENT,
  `profile`     INT NOT NULL,
  `createdAt`   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updatedAt`   TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`profile`) REFERENCES `Profile`(`id`) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB CHARACTER SET utf8mb4 COLLATE utf8mb4_bin;

CREATE TABLE IF NOT EXISTS `CategoryPath` (
  `id`          INT NOT NULL AUTO_INCREMENT,
  `ancestor`    INT NOT NULL,
  `descendant`  INT NOT NULL,
  `depth`       INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE (`ancestor`,`descendant`),
  FOREIGN KEY (`ancestor`) REFERENCES `Category`(`id`) ON UPDATE CASCADE ON DELETE CASCADE,
  FOREIGN KEY (`descendant`) REFERENCES `Category`(`id`) ON UPDATE CASCADE ON DELETE CASCADE
) COMMENT='Closure Table'
  ENGINE=InnoDB CHARACTER SET utf8mb4 COLLATE utf8mb4_bin;

CREATE TABLE IF NOT EXISTS `Chat` (
  `id`          INT NOT NULL AUTO_INCREMENT,
  `category`    INT,
  `profile`     INT,
  PRIMARY KEY (`id`),
  INDEX (`category`),
  INDEX (`profile`),
  FOREIGN KEY (`category`) REFERENCES `Category`(`id`) ON UPDATE CASCADE ON DELETE RESTRICT,
  FOREIGN KEY (`profile`) REFERENCES `Profile`(`id`) ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT `CHK_ChatType` CHECK ((`category` IS NOT NULL AND `profile` IS NOT NULL) OR `category` IS NULL)
) ENGINE=InnoDB CHARACTER SET utf8mb4 COLLATE utf8mb4_bin;

CREATE TABLE IF NOT EXISTS `ChatLine` (

  `id`          INT NOT NULL AUTO_INCREMENT,
  `chat`        INT NOT NULL,
  `author`      INT NOT NULL,
  `message`     INT NOT NULL,
  `asset`       INT,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`chat`) REFERENCES `Chat`(`id`) ON UPDATE CASCADE ON DELETE CASCADE,
  FOREIGN KEY (`author`) REFERENCES `User`(`id`) ON UPDATE CASCADE ON DELETE CASCADE,
  FOREIGN KEY (`message`) REFERENCES `Message`(`id`) ON UPDATE CASCADE ON DELETE CASCADE,
  FOREIGN KEY (`asset`) REFERENCES `File`(`id`) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB CHARACTER SET utf8mb4 COLLATE utf8mb4_bin;

CREATE TABLE IF NOT EXISTS `ChatParticipant` (
  `id`          INT NOT NULL AUTO_INCREMENT,
  `chat`        INT NOT NULL,
  `participant` INT NOT NULL,
  `subscription` INT,
  `admin`       BOOLEAN NOT NULL,
  `visibility`  BOOLEAN NOT NULL,
  -- NOTE: Date of accession. Accordingly, you can choose whether older chat posts can be read.
  `createdAt`  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updatedAt`  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE (`chat`,`participant`),
  INDEX (`participant`),
  FOREIGN KEY (`participant`) REFERENCES `User`(`id`) ON UPDATE CASCADE ON DELETE CASCADE,
  FOREIGN KEY (`chat`) REFERENCES `Chat`(`id`) ON UPDATE CASCADE ON DELETE CASCADE,
  FOREIGN KEY (`subscription`) REFERENCES `NotificationLevel`(`level`) ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB CHARACTER SET utf8mb4 COLLATE utf8mb4_bin;

CREATE TABLE IF NOT EXISTS LinePosition (
  `id`          INT NOT NULL AUTO_INCREMENT,
  `participant` INT NOT NULL,
  `cursor`      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY(`id`),
  UNIQUE KEY (`participant`),
  FOREIGN KEY (`participant`) REFERENCES `ChatParticipant`(`id`) ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB CHARACTER SET utf8mb4 COLLATE utf8mb4_bin;

CREATE TABLE IF NOT EXISTS `Report` (
  `id`         INT NOT NULL AUTO_INCREMENT,
  `line`       INT,
  `reporter`   INT NOT NULL,
  `reported`   INT,
  `reason`     INT NOT NULL,
  `createdAt`  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updatedAt`  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE (`line`,`reporter`),
  UNIQUE (`reporter`, `reported`),
  FOREIGN KEY (`line`) REFERENCES `ChatLine`(`id`) ON UPDATE CASCADE ON DELETE RESTRICT,
  FOREIGN KEY (`reporter`) REFERENCES `User`(`id`) ON UPDATE CASCADE ON DELETE RESTRICT,
  FOREIGN KEY (`reported`) REFERENCES `User`(`id`) ON UPDATE CASCADE ON DELETE RESTRICT,
  FOREIGN KEY (`reason`) REFERENCES `Message`(`id`) ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT `CHK_Report` CHECK ((`line` IS NOT NULL AND `reported` IS NULL) OR (`line` IS NULL AND `reported` IS NOT NULL))
) ENGINE=InnoDB CHARACTER SET utf8mb4 COLLATE utf8mb4_bin;

CREATE TABLE IF NOT EXISTS `links` (
  `id`        INT NOT NULL AUTO_INCREMENT,
  `url`       TEXT NOT NULL,
  `description` TEXT NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB CHARACTER SET utf8mb4 COLLATE utf8mb4_bin;
