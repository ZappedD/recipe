-- phpMyAdmin SQL Dump
-- version 4.7.4
-- https://www.phpmyadmin.net/
--
-- Värd: 127.0.0.1
-- Tid vid skapande: 26 feb 2018 kl 10:23
-- Serverversion: 10.1.26-MariaDB
-- PHP-version: 7.1.9

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Databas: `recipe-app`
--
CREATE DATABASE IF NOT EXISTS `recipe-app` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `recipe-app`;

-- --------------------------------------------------------

--
-- Tabellstruktur `categorys`
--

CREATE TABLE `categorys` (
  `category` varchar(30) NOT NULL,
  `id` int(6) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumpning av Data i tabell `categorys`
--

INSERT INTO `categorys` (`category`, `id`) VALUES
('frukost', 4),
('lunch', 3),
('middag', 2),
('vegetariskt', 1);

-- --------------------------------------------------------

--
-- Tabellstruktur `foods`
--

CREATE TABLE `foods` (
  `name` varchar(30) NOT NULL,
  `unit` varchar(7) NOT NULL,
  `id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumpning av Data i tabell `foods`
--

INSERT INTO `foods` (`name`, `unit`, `id`) VALUES
('potatis', 'antal', 1),
('morott', 'antal', 2),
('lök', 'antal', 3),
('vatten', 'dl', 4),
('vetemjöl', 'dl', 5),
('havregryn', 'dl', 6),
('potatismjöl', 'dl', 7),
('mjölk', 'dl', 8),
('salt', 'tsk', 9),
('smör', 'g', 10),
('ananas', 'antal', 15),
('ananas', 'kg', 16),
('fjdsl', 'fjsd', 17),
('sdaf', 'vsf', 18),
('', '', 19),
('Kanel', 'msk', 20),
('hamburgare', 'antal', 21),
('ostskiva', 'antal', 22);

-- --------------------------------------------------------

--
-- Tabellstruktur `ingrediences`
--

CREATE TABLE `ingrediences` (
  `id` int(6) NOT NULL,
  `amount-of-unit` int(3) NOT NULL,
  `food_id` int(6) NOT NULL,
  `recipe_id` int(6) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumpning av Data i tabell `ingrediences`
--

INSERT INTO `ingrediences` (`id`, `amount-of-unit`, `food_id`, `recipe_id`) VALUES
(5, 6, 8, 7),
(6, 2, 9, 7),
(7, 3, 7, 7),
(15, 6, 8, 47),
(16, 2, 9, 47),
(17, 3, 7, 47),
(23, 0, 21, 76),
(24, 0, 22, 76);

-- --------------------------------------------------------

--
-- Tabellstruktur `recipes`
--

CREATE TABLE `recipes` (
  `name` varchar(60) NOT NULL,
  `preparing_time` varchar(15) NOT NULL,
  `description` varchar(400) NOT NULL,
  `instruktions` mediumtext NOT NULL,
  `serving_quantity` int(60) NOT NULL,
  `user_id` int(11) NOT NULL,
  `category_id` int(6) NOT NULL,
  `imageUrl` varchar(300) NOT NULL,
  `time_added` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `id` int(6) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumpning av Data i tabell `recipes`
--

INSERT INTO `recipes` (`name`, `preparing_time`, `description`, `instruktions`, `serving_quantity`, `user_id`, `category_id`, `imageUrl`, `time_added`, `id`) VALUES
('Pannkaka är bästa ', '15min', 'En klar vardagsfavorit! Här är ett enkelt grundrecept på tunna pannkakor, bara att steka och servera med sylt, grädde, glass eller kvarg.', 'Vispa ut mjölet i hälften av mjölken till en slät smet. Vispa i resterande mjölk, ägg och salt. Låt smeten svälla ca 10 min. Smält smör i en stekpanna och häll ner i smeten. Grädda tunna pannkakor.', 1, 1, 1, 'https://www.arla.se/globalassets/bilder-recept/pannkakor-857x600.jpg', '2018-01-21 13:27:04', 7),
('Kanelbullar', '1-4 timmar', 'Tillverka utsökta och goda kanelbullar med detta perfekta kanelbullerecept. Kanelbullarna blir stora, mjuka och får en god smak av sin fyllning som består av kanel, smör och socker.', 'Deg: Smula jästen i en bunke. Smält matfettet i en kastrull eller i en skål i mikrovågsugnen på full effekt (max 800 W) ca 30 sek. Tillsätt mjölken och värm till fingervarmt, 37°C. Häll lite av degvätskan över jästen och rör tills den löst sig.\r\nTillsätt resten av degvätskan, socker, salt och nästan allt vetemjölet, spara lite till utbakningen. Arbeta degen smidig för hand eller i maskin tills degen släpper bunkens kanter. Låt degen jäsa övertäckt ca 30 min.\r\nStarta timer\r\nFyllning: Rör matfettet smidigt med socker och kanel till fyllningen.\r\nTa upp degen på arbetsbänk och knåda den smidig med resterande mjöl. Kavla ut degen till en platta, ca 30x40 cm (för 20 st). Bred på fyllningen och rulla ihop från långsidan. Skär rullen i ca 2 cm breda bitar. Lägg bitarna med snittytan uppåt på en bakpappersklädd plåt eller i bullformar av papper.\r\nLåt bullarna jäsa under bakduk ca 20 min.\r\nStarta timer\r\nPensling: Pensla bullarna med uppvispat ägg och strö över lite pärlsocker. Grädda bullarna mitt i ugnen i 250°C, ca 8 min. Låt kallna på galler under bakduk innan de packas i påse eller burk med lock.', 20, 1, 4, 'https://www.ica.se//icase.azureedge.net/imagevaultfiles/id_30091/cf_259/kanelbullar-5342.jpg', '2018-01-17 13:27:04', 8),
('tomt', '1 min', 'this is good', 'do this then dhtis ', 10, 1, 4, 'https://www.arla.se/globalassets/bilder-recept/pannkakor-857x600.jpg', '2018-01-31 13:27:04', 9),
('Pannkakor', '30 minuter', 'This is a testtomt', '1. 2. 3.fdsfdfewfs', 1, 1, 4, 'https://www.arla.se/globalassets/bilder-recept/pannkakor-857x600.jpg', '2018-01-31 13:20:04', 13),
('Pannkaka är bästa ', '15min', 'En klar vardagsfavorit! Här är ett enkelt grundrecept på tunna pannkakor, bara att steka och servera med sylt, grädde, glass eller kvarg.', 'Vispa ut mjölet i hälften av mjölken till en slät smet. Vispa i resterande mjölk, ägg och salt. Låt smeten svälla ca 10 min. Smält smör i en stekpanna och häll ner i smeten. Grädda tunna pannkakor.', 1, 1, 1, 'https://www.arla.se/globalassets/bilder-recept/pannkakor-857x600.jpg', '2018-02-12 12:35:12', 47),
('Testar', '20 min', 'sdfgh', 'efdghjk', 10, 4, 4, 'https://static.olocdn.net/menu/chilis/f0b363f53d47cadfb3d94c873db89f4b.jpg', '2018-02-15 12:45:05', 76);

-- --------------------------------------------------------

--
-- Tabellstruktur `users`
--

CREATE TABLE `users` (
  `id` int(6) NOT NULL,
  `favorit category` int(11) NOT NULL,
  `rights` varchar(11) NOT NULL,
  `username` varchar(40) NOT NULL,
  `password` varchar(60) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumpning av Data i tabell `users`
--

INSERT INTO `users` (`id`, `favorit category`, `rights`, `username`, `password`) VALUES
(1, 4, 'admin', 'admin', 'root'),
(4, 1, 'none', 'root', '$2a$10$icm7I1j.yHu7w6PySnTnt.OSg8Te.p41Fj928vHRTqNMQxZwhrN4S'),
(5, 1, 'none', 'test', '$2a$10$Ev3It5MzBUz2HKu1Fja0u.H7jKwTPfMdqOP.ohYpdQV7AzRwTxOVa'),
(6, 1, 'none', 'q', '$2a$10$xIBiNn3xqnc6LYweD740s.uljl6kNzM2ALoWbunTkFqkBHrjCV1ki'),
(9, 1, 'none', 'Testar2', '$2a$10$mtkoJ10sXUuiPxsKOAPCZe9WXxTh4u3pfihXIkfTD7chckaBu/zoe'),
(11, 1, 'none', 'qa', '$2a$10$ovZ3ofssGIgwAmcDMnTmoObjSqVatSFHp7KOuSiTYbvtpTNArV4rW'),
(13, 1, 'none', 'qaqa', '$2a$10$RQg0M2ezC.6C45AX7.h8k.kEJ/pUfhoYRMPV8sd0ArBnmpJgys9VG'),
(14, 1, 'none', '1', '$2a$10$MWULNc6y112CO8chQG4jQe7lMfhgwFSSthHdfZk.GW.RLbgBK3aZi');

--
-- Index för dumpade tabeller
--

--
-- Index för tabell `categorys`
--
ALTER TABLE `categorys`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `name` (`category`),
  ADD KEY `id` (`id`);

--
-- Index för tabell `foods`
--
ALTER TABLE `foods`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id` (`id`);

--
-- Index för tabell `ingrediences`
--
ALTER TABLE `ingrediences`
  ADD PRIMARY KEY (`id`),
  ADD KEY `food id` (`food_id`),
  ADD KEY `recipe id` (`recipe_id`);

--
-- Index för tabell `recipes`
--
ALTER TABLE `recipes`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id` (`id`),
  ADD KEY `category id` (`category_id`),
  ADD KEY `creator id` (`user_id`);

--
-- Index för tabell `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`),
  ADD KEY `favorit category` (`favorit category`);

--
-- AUTO_INCREMENT för dumpade tabeller
--

--
-- AUTO_INCREMENT för tabell `categorys`
--
ALTER TABLE `categorys`
  MODIFY `id` int(6) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT för tabell `foods`
--
ALTER TABLE `foods`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

--
-- AUTO_INCREMENT för tabell `ingrediences`
--
ALTER TABLE `ingrediences`
  MODIFY `id` int(6) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=25;

--
-- AUTO_INCREMENT för tabell `recipes`
--
ALTER TABLE `recipes`
  MODIFY `id` int(6) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=77;

--
-- AUTO_INCREMENT för tabell `users`
--
ALTER TABLE `users`
  MODIFY `id` int(6) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- Restriktioner för dumpade tabeller
--

--
-- Restriktioner för tabell `ingrediences`
--
ALTER TABLE `ingrediences`
  ADD CONSTRAINT `ingrediences_ibfk_1` FOREIGN KEY (`food_id`) REFERENCES `foods` (`id`),
  ADD CONSTRAINT `ingrediences_ibfk_2` FOREIGN KEY (`recipe_id`) REFERENCES `recipes` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Restriktioner för tabell `recipes`
--
ALTER TABLE `recipes`
  ADD CONSTRAINT `recipes_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `categorys` (`id`),
  ADD CONSTRAINT `recipes_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Restriktioner för tabell `users`
--
ALTER TABLE `users`
  ADD CONSTRAINT `users_ibfk_1` FOREIGN KEY (`favorit category`) REFERENCES `categorys` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
