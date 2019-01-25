-- phpMyAdmin SQL Dump
-- version 4.7.4
-- https://www.phpmyadmin.net/
--
-- Gép: 127.0.0.1:3306
-- Létrehozás ideje: 2019. Jan 25. 05:57
-- Kiszolgáló verziója: 5.7.19
-- PHP verzió: 5.6.31

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Adatbázis: `wob`
--
CREATE DATABASE IF NOT EXISTS `wob` DEFAULT CHARACTER SET utf8 COLLATE utf8_hungarian_ci;
USE `wob`;

-- --------------------------------------------------------

--
-- Tábla szerkezet ehhez a táblához `orders`
--

DROP TABLE IF EXISTS `orders`;
CREATE TABLE IF NOT EXISTS `orders` (
  `OrderId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `BuyerName` varchar(50) COLLATE utf8_hungarian_ci NOT NULL,
  `BuyerEmail` varchar(50) COLLATE utf8_hungarian_ci NOT NULL,
  `OrderDate` date NOT NULL,
  `OrderTotalValue` double(10,0) NOT NULL,
  `Address` varchar(255) COLLATE utf8_hungarian_ci NOT NULL,
  `Postcode` int(11) NOT NULL,
  PRIMARY KEY (`OrderId`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_hungarian_ci;

--
-- A tábla adatainak kiíratása `orders`
--

INSERT INTO `orders` (`OrderId`, `BuyerName`, `BuyerEmail`, `OrderDate`, `OrderTotalValue`, `Address`, `Postcode`) VALUES
(1, 'Matthew S Occhipinti', 'mso@example.com', '2019-01-15', 183, '2776  Star Route, Chicago, Illinois', 60654),
(2, 'Morgan Smith', 'moth@example.com', '2019-01-17', 148, '4830 Thrash Trail, Dallas, TX', 75207);

-- --------------------------------------------------------

--
-- Tábla szerkezet ehhez a táblához `order_item`
--

DROP TABLE IF EXISTS `order_item`;
CREATE TABLE IF NOT EXISTS `order_item` (
  `OrderItemId` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `OrderId` int(11) NOT NULL,
  `SalePrice` double NOT NULL,
  `ShippingPrice` double NOT NULL,
  `TotalItemPrice` int(11) NOT NULL,
  `Sku` varchar(30) COLLATE utf8_hungarian_ci NOT NULL,
  `Status` varchar(255) COLLATE utf8_hungarian_ci NOT NULL,
  PRIMARY KEY (`OrderItemId`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_hungarian_ci;

--
-- A tábla adatainak kiíratása `order_item`
--

INSERT INTO `order_item` (`OrderItemId`, `OrderId`, `SalePrice`, `ShippingPrice`, `TotalItemPrice`, `Sku`, `Status`) VALUES
(1, 3, 159, 24, 183, '198-763', 'IN_STOCK'),
(2, 1, 121, 27, 148, '172-632', 'IN_STOCK');
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
