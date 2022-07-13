-- --------------------------------------------------------
-- Hat einen Privaten Chat in dem `admin` und `root` User Nachrichten geschrieben haben.
-- Hat einen Gruppenchat in dem alle Benutzer sind.
-- Hat 8 Benutzer insgesamt.
-- Hauptprotagonist in den Mocks ist der "admin" User. Er hat alle User als Kontakte und ist in beiden Arten von Chats vertreten sowie Admin des Gruppenchats.
-- Nur der `admin` User hat UserPreferences eingestellt und die AGB akzeptiert.
-- Passwörter sind immer: Password123
-- Für mails und Bilder wird SHA-256 benutzt
-- --------------------------------------------------------
-- phpMyAdmin SQL Dump
-- version 4.8.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Erstellungszeit: 17. Aug 2018 um 11:22
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

--
-- Daten für Tabelle `Color`
--

INSERT INTO `Color` (`id`, `hue`, `saturation`, `lightness`, `alpha`) VALUES
(1, 0, 100, 80, 100),
(4, 1, 100, 1, 100),
(2, 127, 100, 80, 100),
(3, 227, 100, 80, 100);

--
-- Daten für Tabelle `EMail`
--

INSERT INTO `Email` (`id`, `mail`, `hash`, `verified`, `visibility`, `createdAt`, `updatedAt`) VALUES
(1, 'admin@chatsphere.de', '0FF7168A0E570E3A8D8F07302BF92AD3D0B047CD77FA027ECC8445D1641EA9D3', 1, 2, '2018-08-15 10:08:48', '2018-08-15 12:46:02'),
(2, 'root@chatsphere.de', 'BD1320CF9C9C3A6DE29F68B73994E7B3F6AE9277D6865639E74A0340F45F6F96', 1, 1, '2018-08-15 10:11:01', '2018-08-15 12:46:09'),
(3, 'user@chatsphere.de', '2EB6D9787F757FA075E9007203BF0F0F845FA4CBD76493E34A1B97A75A4355DC', 1, 2, '2018-08-15 10:11:28', '2018-08-15 12:46:17'),
(4, 'poooo@ooo.op', '0749F7383F3CDBCB6F78514B59E0FF847AE01807521338B45E1D9896D3F40BFB', 0, 2, '2018-08-15 10:12:09', '2018-08-15 12:46:24'),
(5, 'spongey@bikinimail.com', 'D792665DC9454FA430144AF4F5A5121AD092BE2C47DE2D6EF4D9742322F68374', 0, 1, '2018-08-15 10:12:33', '2018-08-15 12:46:32'),
(6, 'i<3nature@interwebs.com', 'DC7EC48CD9346F00935F7EFB1C47A06A437E99CC0F3518A68C1F94FF152FB8C7', 1, 2, '2018-08-15 10:13:11', '2018-08-15 12:47:03'),
(7, 'pipin@linked.in', '3C8964BAD6177D276CEB0040341841A9CCD71DCFDB32EA24F30C0DEB96F2CD10', 0, 2, '2018-08-15 10:13:37', '2018-08-15 12:47:09'),
(8, '!\\\"§$%&/(()=123456789äöüß@de.in', 'EEBD2D07CF5F3898FECDAF192F2C39B3C92B0747C08C5573D547DD1E262C83DC', 1, 1, '2018-08-15 10:13:58', '2018-08-15 12:47:16'),
(9, 'haha@trashmail.de', 'D0FDD727165713C44BE4DBABF1A2F3CCDAD6707BD244DC4D20A66CFFA07DA3D3', 1, 1, '2018-08-15 16:10:35', '2018-08-15 16:10:35');

--
-- Daten für Tabelle `File`
--

INSERT INTO `File` (`id`, `name`, `size`, `type`, `hash`, `createdAt`) VALUES
(0, 'nichts', 0, 'text/plain', '0', '2018-08-15 11:11:27'),
(1, 'bildvonmir', 103000, 'image/jpeg', 0x3de7cf573ce503983b08c8ec9f42f59efb3779fedacf821911d646c4c10e703e, '2018-08-15 08:29:41'),
(2, 'gif_animation', 3000000, 'image/gif', 0xfa27df29e73db266da55c50bbee6889fc827383caec58d82ed48f9fc992cdf2b, '2018-08-15 08:39:38'),
(3, 'png_image', 74583, 'image/png', 0x446b2d17669177fb3008dca3532b9aa54ffc3d0c5706dbaf1f5bf0bebc7fbd29, '2018-08-15 09:40:08'),
(4, 'avatar', 32907, 'image/jpeg', 0x7e993cb6d0b74acc7323645f64789648585225f247152d4eadc8a2901d926d2e, '2018-08-15 09:42:09'),
(5, 'spongebob', 14169, 'image/jpeg', 0x9e1c61d0e4b802b6c61dfed72de73e2244426b626d72959c623d7e8ec9ee4a49, '2018-08-15 09:43:23'),
(6, 'spongebob', 14169, 'image/jpeg', 0x446b2d17669177fb3008dca3532b9aa54ffc3d0c5706dbaf1f5bf0bebc7fbd29, '2018-08-15 09:43:23'),
(7, 'spongebob', 14169, 'image/jpeg', 0x3de7cf573ce503983b08c8ec9f42f59efb3779fedacf821911d646c4c10e703e, '2018-08-15 09:43:23'),
(8, 'spongebob', 14169, 'image/jpeg', 0xb842705eedc097aebd1a0bb9ef5780e250d1b2e871563711dc3912a8764c1e16, '2018-08-15 09:43:23'),
(9, 'spongebob', 14169, 'image/jpeg', 0xf748847b97fd8f3b32bdf693740b059a3b725d67a50621ec028ca6a4bb063a5a, '2018-08-15 09:43:23');


--
-- Daten für Tabelle `Language`
--

INSERT INTO `Language` (`id`, `code`) VALUES
(1, 'de'),
(2, 'en');

--
-- Daten für Tabelle `Location`
--

INSERT INTO `Location` (`id`, `country`, `province`) VALUES
(1, '--', '--'),
(2, 'DE', 'BW'),
(3, 'US', 'TX');

--
-- Daten für Tabelle `Message`
--

INSERT INTO `Message` (`id`, `markup`, `content`, `createdAt`, `updatedAt`) VALUES
(1, 0, 'Admin der Anwendung', '2018-08-15 10:57:27', '2018-08-15 10:58:37'),
(2, 0, 'Pflanz dich ein', '2018-08-15 10:58:50', '2018-08-15 10:58:50'),
(3, 0, 'Take off your jacket', '2018-08-15 10:59:11', '2018-08-15 10:59:11'),
(4, 0, 'Laugh at my name I laugh at your life', '2018-08-15 10:59:46', '2018-08-15 11:00:07'),
(5, 0, 'Mitarbeiter des Jahres', '2018-08-15 11:00:38', '2018-08-15 11:00:38'),
(6, 0, 'Statusnachricht', '2018-08-15 11:00:55', '2018-08-15 11:00:55'),
(7, 0, '!\"§$%&//()=äüöß', '2018-08-15 11:01:15', '2018-08-15 11:01:15'),
(8, 0, 'Hey', '2018-08-15 11:07:40', '2018-08-15 11:07:40'),
(9, 0, 'Hi', '2018-08-15 11:07:49', '2018-08-15 11:07:49'),
(10, 0, 'Wie geht\'zz', '2018-08-15 11:08:00', '2018-08-15 11:08:00'),
(11, 0, 'gütchen', '2018-08-15 11:08:19', '2018-08-15 11:08:19'),
(12, 0, 'Die Prequels sind kacke', '2018-08-15 12:24:39', '2018-08-15 12:24:39'),
(13, 0, '٩(-̮̮̃-̃)۶ ٩(●̮̮̃•̃)۶ ٩(͡๏̯͡๏)۶ ٩(-̮̮̃•̃)', '2018-08-15 15:45:32', '2018-08-15 15:45:32');

--
-- Daten für Tabelle `Phone`
--

INSERT INTO `Phone` (`id`, `number`, `hash`, `visibility`, `createdAt`) VALUES
(1, '202-555-0138', '029C06A6151F3482A0721421B271AA9C063E4338DE0581AF736E753408A21153', 2, '2018-08-15 10:50:58'),
(2, '(0) 1234 1337', '6A4062FE660E8B69B1EC41597BE24C3FACAFFC6DD74AA647FC7E11E81C3DBFA5', 2, '2018-08-15 10:51:31'),
(3, '+22 1743 1337', 'E2E691D5D93AF0756BC265A41DC045D358235DD7EEA5D221C9D62AEFCCD30F4F', 1, '2018-08-15 10:52:21'),
(4, '+49 122 234 1337', '29BA7EBE7EF66159514B2150138522B4D8441429FA4D802270724F206140B428', 1, '2018-08-15 10:53:15'),
(5, '+49 9437 1447', 'C5DF9D1284C2A970BF684A86BC1CB18DBA78B4683313A147F2A8B086B899274A', 3, '2018-08-15 10:54:32'),
(6, '+1-202-555-0134', '3382C7633F018E63E40B46A20B112BE10D9FA0624D6D8F2F73EF65432475493F', 2, '2018-08-15 10:55:13');

--
-- Daten für Tabelle `Profile`
--

INSERT INTO `Profile` (`id`, `name`, `displayName`, `backgroundColor`, `icon`) VALUES
(1, 'admin', 'Badministrator', 1, 1),
(2, 'root', 'ChatSphere', 2, 4),
(3, 'user', 'user', 2, 3),
(4, 'pee', 'Peepeepoopoomann', 1, 2),
(5, 'spongebob', 'Spongy', 2, 5),
(6, 'iconless', 'TopSecret', 4, NULL),
(7, 'backgroundless', 'Hintergrund', NULL, 6),
(8, 'sonder', '!\"§$%&/(()=123456789äöüß', NULL, 7),
(9, 'stale', '5-jahre-alter-account', NULL, 8),
(10, 'starwarsfanclub', 'Die dunkle Seite der Macht', NULL, 9);


--
-- Daten für Tabelle `Password`
--

INSERT INTO `Password` (`id`, `hash`, `updatedAt`) VALUES
	(1, '$argon2i$v=19$m=65536,t=8,p=1$+5/TOSHwcSRy9CPhGn26LQ$wDQ7O3XKvk3ph4UxBPr30f3E/a3fgpeLihtrlJPanhA', '2018-08-15 12:37:37'),
	(2, '$argon2i$v=19$m=65536,t=8,p=1$+5/TOSHwcSRy9CPhGn26LQ$wDQ7O3XKvk3ph4UxBPr30f3E/a3fgpeLihtrlJPanhA', '2018-08-15 12:38:20'),
	(3, '$argon2i$v=19$m=65536,t=8,p=1$+5/TOSHwcSRy9CPhGn26LQ$wDQ7O3XKvk3ph4UxBPr30f3E/a3fgpeLihtrlJPanhA', '2018-08-15 12:38:35'),
	(4, '$argon2i$v=19$m=65536,t=8,p=1$+5/TOSHwcSRy9CPhGn26LQ$wDQ7O3XKvk3ph4UxBPr30f3E/a3fgpeLihtrlJPanhA', '2018-08-15 12:38:57'),
	(5, '$argon2i$v=19$m=65536,t=8,p=1$+5/TOSHwcSRy9CPhGn26LQ$wDQ7O3XKvk3ph4UxBPr30f3E/a3fgpeLihtrlJPanhA', '2018-08-15 12:39:18'),
	(6, '$argon2i$v=19$m=65536,t=8,p=1$+5/TOSHwcSRy9CPhGn26LQ$wDQ7O3XKvk3ph4UxBPr30f3E/a3fgpeLihtrlJPanhA', '2018-08-15 12:39:35'),
	(7, '$argon2i$v=19$m=65536,t=8,p=1$+5/TOSHwcSRy9CPhGn26LQ$wDQ7O3XKvk3ph4UxBPr30f3E/a3fgpeLihtrlJPanhA', '2018-08-15 12:39:45'),
	(8, '$argon2i$v=19$m=65536,t=8,p=1$+5/TOSHwcSRy9CPhGn26LQ$wDQ7O3XKvk3ph4UxBPr30f3E/a3fgpeLihtrlJPanhA', '2018-08-15 12:40:04'),
	(9, '$argon2i$v=19$m=65536,t=8,p=1$+5/TOSHwcSRy9CPhGn26LQ$wDQ7O3XKvk3ph4UxBPr30f3E/a3fgpeLihtrlJPanhA', '2018-08-15 12:40:05');

--
-- Daten für Tabelle `User`
--

INSERT INTO `User` (`profile`, `email`, `authentication`, `phone`, `status`, `createdAt`, `updatedAt`) VALUES
(1, 1, 1, 1, 1, '2018-08-15 09:52:48', '2018-08-15 11:01:32'),
(2, 2, 2, 2, 2, '2018-08-15 11:01:53', '2018-08-15 11:01:53'),
(3, 3, 3, 3, 3, '2018-08-15 11:02:11', '2018-08-15 11:02:11'),
(4, 4, 4, 4, 4, '2018-08-15 11:02:31', '2018-08-15 11:02:31'),
(5, 5, 5, 5, 5, '2018-08-15 11:02:48', '2018-08-15 11:02:51'),
(6, 6, 6, 6, 6, '2018-08-15 11:03:03', '2018-08-15 11:03:09'),
(7, 7, 7, NULL, NULL, '2018-08-15 11:03:14', '2018-08-15 11:03:23'),
(8, 8, 8, NULL, 7, '2018-08-15 11:03:45', '2018-08-15 11:03:49'),
(9, 9, 9, NULL, NULL, '2013-08-15 16:07:48', '2013-08-15 18:02:53');

--
-- Daten für Tabelle `UserLegal`
--

INSERT INTO `UserLegal` (`user`, `agb`, `privacy`, `locked`) VALUES
(1, '2018-08-15 12:25:58', '0000-00-00 00:00:00', '0000-00-00 00:00:00');

--
-- Daten für Tabelle `UserPreferences`
--

INSERT INTO `UserPreferences` (`user`, `language`, `location`, `notification`, `linkpreview`, `retention`) VALUES
(1, 1, 1, 1, 1, 365),
(2, 2, 2, 1, 1, 365);
(3, 2, 2, 1, 1, 365);
(4, 2, 2, 1, 1, 365);
(5, 2, 2, 1, 1, 365);
(6, 2, 2, 1, 1, 365);
(7, 2, 2, 1, 1, 365);
(8, 2, 2, 1, 1, 365);
(9, 2, 2, 1, 1, 365);

--
-- Daten für Tabelle `UserRelationship`
--

INSERT INTO `UserRelationship` (`owner`, `refers`, `petName`, `blocked`, `createdAt`, `updatedAt`) VALUES
(1, 2, 'rootie', NULL, '2018-08-15 12:58:07', '2018-08-16 05:37:32'),
(1, 3, 'user', NULL, '2018-08-15 13:03:46', '2018-08-16 05:37:28'),
(1, 4, 'pee', NULL, '2018-08-15 13:04:14', '2018-08-16 05:37:34'),
(1, 5, 'spongebob', NULL, '2018-08-15 13:05:46', '2018-08-16 05:37:35'),
(1, 6, 'iconless', NULL, '2018-08-15 13:05:56', '2018-08-16 05:37:35'),
(1, 7, NULL, NULL, '2018-08-15 13:06:06', '2018-08-16 05:38:46'),
(1, 8, 'sonder', NULL, '2018-08-15 13:06:15', '2018-08-16 05:37:37'),
(2, 1, NULL, NULL, '2018-08-15 13:01:47', '2018-08-16 05:38:37');
COMMIT;

--
-- Daten für Tabelle `Category`
--

INSERT INTO `Category` (`id`, `profile`, `createdAt`, `updatedAt`) VALUES
(1, 10, '2018-08-15 11:16:32', '2018-08-15 11:16:32');

--
-- Daten für Tabelle `CategoryPath`
--

INSERT INTO `CategoryPath` (`ancestor`, `descendant`, `depth`) VALUES
(1, 1, 1);

--
-- Daten für Tabelle `Chat`
--

INSERT INTO `Chat` (`id`, `category`, `profile`) VALUES
(1, NULL, NULL),
(2, 1, 10);

--
-- Daten für Tabelle `ChatLine`
--

INSERT INTO `ChatLine` (`id`, `chat`, `author`, `message`, `asset`) VALUES
(1, 1, 1, 8, NULL),
(2, 1, 2, 9, NULL),
(3, 1, 1, 10, NULL),
(4, 1, 2, 11, 5),
(5, 2, 1, 12, NULL),
(6, 2, 2, 13, NULL);

--
-- Daten für Tabelle `ChatParticipant`
--

INSERT INTO `ChatParticipant` (`participant`, `chat`, `subscription`, `admin`, `visibility`, `createdAt`, `updatedAt`) VALUES
(2, 1, 1, 1, true, NULL, NULL),
(1, 1, 1, 1, true, NULL, NULL),
(1, 2, 1, 1, true, '2018-08-15 12:21:13', '2018-08-15 12:21:21'),
(2, 2, 1, 1, true, '2018-08-15 12:21:49', '2018-08-15 12:23:19'),
(3, 2, 1, 0, true, '2018-08-15 12:22:27', '2018-08-15 12:22:30'),
(5, 2, 1, 0, true, '2018-08-15 12:22:42', '2018-08-15 12:22:48'),
(6, 2, 1, 0, true, '2018-08-15 12:23:03', '2018-08-15 12:23:03'),
(7, 2, 1, 0, true, '2018-08-15 12:23:17', '2018-08-15 12:23:17'),
(8, 2, 1, 0, true, '2018-08-15 12:23:33', '2018-08-15 12:23:33');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

