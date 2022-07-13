-- phpMyAdmin SQL Dump
-- version 4.8.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Erstellungszeit: 13. Jun 2018 um 17:30
-- Server-Version: 10.1.26-MariaDB-0+deb9u1
-- PHP-Version: 7.0.27-0+deb9u1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Datenbank: `chatsphere`
--
CREATE DATABASE IF NOT EXISTS `chatsphere` DEFAULT CHARACTER SET utf8 COLLATE utf8_bin;
USE `chatsphere`;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `hydra_client`
--

DROP TABLE IF EXISTS `hydra_client`;
CREATE TABLE IF NOT EXISTS `hydra_client` (
  `id` varchar(255) COLLATE utf8_bin NOT NULL,
  `client_name` text COLLATE utf8_bin NOT NULL,
  `client_secret` text COLLATE utf8_bin NOT NULL,
  `redirect_uris` text COLLATE utf8_bin NOT NULL,
  `grant_types` text COLLATE utf8_bin NOT NULL,
  `response_types` text COLLATE utf8_bin NOT NULL,
  `scope` text COLLATE utf8_bin NOT NULL,
  `owner` text COLLATE utf8_bin NOT NULL,
  `policy_uri` text COLLATE utf8_bin NOT NULL,
  `tos_uri` text COLLATE utf8_bin NOT NULL,
  `client_uri` text COLLATE utf8_bin NOT NULL,
  `logo_uri` text COLLATE utf8_bin NOT NULL,
  `contacts` text COLLATE utf8_bin NOT NULL,
  `public` tinyint(1) NOT NULL,
  `client_secret_expires_at` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `hydra_client_migration`
--

DROP TABLE IF EXISTS `hydra_client_migration`;
CREATE TABLE IF NOT EXISTS `hydra_client_migration` (
  `id` varchar(255) NOT NULL,
  `applied_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Daten für Tabelle `hydra_client_migration`
--

INSERT INTO `hydra_client_migration` (`id`, `applied_at`) VALUES
('1', '2018-06-13 15:15:50'),
('2', '2018-06-13 15:15:51');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `hydra_jwk`
--

CREATE TABLE IF NOT EXISTS `hydra_jwk` (
  `sid` varchar(255) COLLATE utf8_bin NOT NULL,
  `kid` varchar(255) COLLATE utf8_bin NOT NULL,
  `version` int(11) NOT NULL DEFAULT '0',
  `keydata` text COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`sid`,`kid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--
-- Daten für Tabelle `hydra_jwk`
--

-- Removed due to private security keys


-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `hydra_jwk_migration`
--

DROP TABLE IF EXISTS `hydra_jwk_migration`;
CREATE TABLE IF NOT EXISTS `hydra_jwk_migration` (
  `id` varchar(255) NOT NULL,
  `applied_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Daten für Tabelle `hydra_jwk_migration`
--

INSERT INTO `hydra_jwk_migration` (`id`, `applied_at`) VALUES
('1', '2018-06-13 15:15:51');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `hydra_oauth2_access`
--

DROP TABLE IF EXISTS `hydra_oauth2_access`;
CREATE TABLE IF NOT EXISTS `hydra_oauth2_access` (
  `signature` varchar(255) COLLATE utf8_bin NOT NULL,
  `request_id` varchar(255) COLLATE utf8_bin NOT NULL,
  `requested_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `client_id` text COLLATE utf8_bin NOT NULL,
  `scope` text COLLATE utf8_bin NOT NULL,
  `granted_scope` text COLLATE utf8_bin NOT NULL,
  `form_data` text COLLATE utf8_bin NOT NULL,
  `session_data` text COLLATE utf8_bin NOT NULL,
  `subject` varchar(255) COLLATE utf8_bin NOT NULL DEFAULT '',
  `active` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`signature`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `hydra_oauth2_authentication_consent_migration`
--

DROP TABLE IF EXISTS `hydra_oauth2_authentication_consent_migration`;
CREATE TABLE IF NOT EXISTS `hydra_oauth2_authentication_consent_migration` (
  `id` varchar(255) NOT NULL,
  `applied_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Daten für Tabelle `hydra_oauth2_authentication_consent_migration`
--

INSERT INTO `hydra_oauth2_authentication_consent_migration` (`id`, `applied_at`) VALUES
('1', '2018-06-13 15:15:51');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `hydra_oauth2_authentication_request`
--

DROP TABLE IF EXISTS `hydra_oauth2_authentication_request`;
CREATE TABLE IF NOT EXISTS `hydra_oauth2_authentication_request` (
  `challenge` varchar(40) COLLATE utf8_bin NOT NULL,
  `requested_scope` text COLLATE utf8_bin NOT NULL,
  `verifier` varchar(40) COLLATE utf8_bin NOT NULL,
  `csrf` varchar(40) COLLATE utf8_bin NOT NULL,
  `subject` varchar(255) COLLATE utf8_bin NOT NULL,
  `request_url` text COLLATE utf8_bin NOT NULL,
  `skip` tinyint(1) NOT NULL,
  `client_id` varchar(255) COLLATE utf8_bin NOT NULL,
  `requested_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `authenticated_at` timestamp NULL DEFAULT NULL,
  `oidc_context` text COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`challenge`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `hydra_oauth2_authentication_request_handled`
--

DROP TABLE IF EXISTS `hydra_oauth2_authentication_request_handled`;
CREATE TABLE IF NOT EXISTS `hydra_oauth2_authentication_request_handled` (
  `challenge` varchar(40) COLLATE utf8_bin NOT NULL,
  `subject` varchar(255) COLLATE utf8_bin NOT NULL,
  `remember` tinyint(1) NOT NULL,
  `remember_for` int(11) NOT NULL,
  `error` text COLLATE utf8_bin NOT NULL,
  `acr` text COLLATE utf8_bin NOT NULL,
  `requested_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `authenticated_at` timestamp NULL DEFAULT NULL,
  `was_used` tinyint(1) NOT NULL,
  PRIMARY KEY (`challenge`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `hydra_oauth2_authentication_session`
--

DROP TABLE IF EXISTS `hydra_oauth2_authentication_session`;
CREATE TABLE IF NOT EXISTS `hydra_oauth2_authentication_session` (
  `id` varchar(40) COLLATE utf8_bin NOT NULL,
  `authenticated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `subject` varchar(255) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `hydra_oauth2_code`
--

DROP TABLE IF EXISTS `hydra_oauth2_code`;
CREATE TABLE IF NOT EXISTS `hydra_oauth2_code` (
  `signature` varchar(255) COLLATE utf8_bin NOT NULL,
  `request_id` varchar(255) COLLATE utf8_bin NOT NULL,
  `requested_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `client_id` text COLLATE utf8_bin NOT NULL,
  `scope` text COLLATE utf8_bin NOT NULL,
  `granted_scope` text COLLATE utf8_bin NOT NULL,
  `form_data` text COLLATE utf8_bin NOT NULL,
  `session_data` text COLLATE utf8_bin NOT NULL,
  `subject` varchar(255) COLLATE utf8_bin NOT NULL DEFAULT '',
  `active` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`signature`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `hydra_oauth2_consent_request`
--

DROP TABLE IF EXISTS `hydra_oauth2_consent_request`;
CREATE TABLE IF NOT EXISTS `hydra_oauth2_consent_request` (
  `challenge` varchar(40) COLLATE utf8_bin NOT NULL,
  `verifier` varchar(40) COLLATE utf8_bin NOT NULL,
  `client_id` varchar(255) COLLATE utf8_bin NOT NULL,
  `subject` varchar(255) COLLATE utf8_bin NOT NULL,
  `request_url` text COLLATE utf8_bin NOT NULL,
  `skip` tinyint(1) NOT NULL,
  `requested_scope` text COLLATE utf8_bin NOT NULL,
  `csrf` varchar(40) COLLATE utf8_bin NOT NULL,
  `authenticated_at` timestamp NULL DEFAULT NULL,
  `requested_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `oidc_context` text COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`challenge`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `hydra_oauth2_consent_request_handled`
--

DROP TABLE IF EXISTS `hydra_oauth2_consent_request_handled`;
CREATE TABLE IF NOT EXISTS `hydra_oauth2_consent_request_handled` (
  `challenge` varchar(40) COLLATE utf8_bin NOT NULL,
  `granted_scope` text COLLATE utf8_bin NOT NULL,
  `remember` tinyint(1) NOT NULL,
  `remember_for` int(11) NOT NULL,
  `error` text COLLATE utf8_bin NOT NULL,
  `requested_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `session_access_token` text COLLATE utf8_bin NOT NULL,
  `session_id_token` text COLLATE utf8_bin NOT NULL,
  `authenticated_at` timestamp NULL DEFAULT NULL,
  `was_used` tinyint(1) NOT NULL,
  PRIMARY KEY (`challenge`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `hydra_oauth2_migration`
--

DROP TABLE IF EXISTS `hydra_oauth2_migration`;
CREATE TABLE IF NOT EXISTS `hydra_oauth2_migration` (
  `id` varchar(255) NOT NULL,
  `applied_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Daten für Tabelle `hydra_oauth2_migration`
--

INSERT INTO `hydra_oauth2_migration` (`id`, `applied_at`) VALUES
('1', '2018-06-13 15:15:51'),
('2', '2018-06-13 15:15:51'),
('3', '2018-06-13 15:15:51'),
('4', '2018-06-13 15:15:51');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `hydra_oauth2_oidc`
--

DROP TABLE IF EXISTS `hydra_oauth2_oidc`;
CREATE TABLE IF NOT EXISTS `hydra_oauth2_oidc` (
  `signature` varchar(255) COLLATE utf8_bin NOT NULL,
  `request_id` varchar(255) COLLATE utf8_bin NOT NULL,
  `requested_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `client_id` text COLLATE utf8_bin NOT NULL,
  `scope` text COLLATE utf8_bin NOT NULL,
  `granted_scope` text COLLATE utf8_bin NOT NULL,
  `form_data` text COLLATE utf8_bin NOT NULL,
  `session_data` text COLLATE utf8_bin NOT NULL,
  `subject` varchar(255) COLLATE utf8_bin NOT NULL DEFAULT '',
  `active` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`signature`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `hydra_oauth2_pkce`
--

DROP TABLE IF EXISTS `hydra_oauth2_pkce`;
CREATE TABLE IF NOT EXISTS `hydra_oauth2_pkce` (
  `signature` varchar(255) COLLATE utf8_bin NOT NULL,
  `request_id` varchar(255) COLLATE utf8_bin NOT NULL,
  `requested_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `client_id` text COLLATE utf8_bin NOT NULL,
  `scope` text COLLATE utf8_bin NOT NULL,
  `granted_scope` text COLLATE utf8_bin NOT NULL,
  `form_data` text COLLATE utf8_bin NOT NULL,
  `session_data` text COLLATE utf8_bin NOT NULL,
  `subject` varchar(255) COLLATE utf8_bin NOT NULL,
  `active` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`signature`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `hydra_oauth2_refresh`
--

DROP TABLE IF EXISTS `hydra_oauth2_refresh`;
CREATE TABLE IF NOT EXISTS `hydra_oauth2_refresh` (
  `signature` varchar(255) COLLATE utf8_bin NOT NULL,
  `request_id` varchar(255) COLLATE utf8_bin NOT NULL,
  `requested_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `client_id` text COLLATE utf8_bin NOT NULL,
  `scope` text COLLATE utf8_bin NOT NULL,
  `granted_scope` text COLLATE utf8_bin NOT NULL,
  `form_data` text COLLATE utf8_bin NOT NULL,
  `session_data` text COLLATE utf8_bin NOT NULL,
  `subject` varchar(255) COLLATE utf8_bin NOT NULL DEFAULT '',
  `active` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`signature`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
