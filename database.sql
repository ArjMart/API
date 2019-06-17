-- MySQL dump 10.13  Distrib 5.7.26, for Linux (x86_64)
--
-- Host: db1.cyf2ksy495ph.us-west-2.rds.amazonaws.com    Database: arjmart
-- ------------------------------------------------------
-- Server version	5.7.22-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Inventory`
--

DROP TABLE IF EXISTS `Inventory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Inventory` (
  `SKU` int(11) NOT NULL,
  `ItemAttributeID` int(11) NOT NULL,
  `LocationID` int(11) NOT NULL,
  `InventoryAmount` int(11) NOT NULL,
  PRIMARY KEY (`SKU`,`ItemAttributeID`,`LocationID`),
  KEY `Inventory_ItemAttributeID_idx` (`ItemAttributeID`),
  KEY `Inventory_LocationID_idx` (`LocationID`),
  CONSTRAINT `Inventory_LocationID` FOREIGN KEY (`LocationID`) REFERENCES `LocationMaster` (`LocationID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `Inventory_SKU_ItemAttributeID` FOREIGN KEY (`SKU`, `ItemAttributeID`) REFERENCES `ItemAttributeMaster` (`SKU`, `ItemAttributeID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Inventory`
--

LOCK TABLES `Inventory` WRITE;
/*!40000 ALTER TABLE `Inventory` DISABLE KEYS */;
INSERT INTO `Inventory` VALUES (2,1,1,1),(2,2,1,5),(2,3,1,5),(2,4,1,5);
/*!40000 ALTER TABLE `Inventory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ItemAttributeMaster`
--

DROP TABLE IF EXISTS `ItemAttributeMaster`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ItemAttributeMaster` (
  `SKU` int(11) NOT NULL,
  `ItemAttributeID` int(11) NOT NULL DEFAULT '0',
  `Color` varchar(45) DEFAULT NULL,
  `Size` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`SKU`,`ItemAttributeID`),
  UNIQUE KEY `ItemAttribute_Unique` (`SKU`,`Color`,`Size`),
  CONSTRAINT `ItemAttribute_SKU` FOREIGN KEY (`SKU`) REFERENCES `ItemMaster` (`SKU`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ItemAttributeMaster`
--

LOCK TABLES `ItemAttributeMaster` WRITE;
/*!40000 ALTER TABLE `ItemAttributeMaster` DISABLE KEYS */;
INSERT INTO `ItemAttributeMaster` VALUES (1,3,'green','L'),(1,2,'white','L'),(1,1,'white','M'),(2,3,'black','M'),(2,2,'navy blue','L'),(2,1,'navy blue','XL'),(2,4,'red','M'),(3,1,'blue','S'),(4,1,'neon yellow','M'),(5,3,'blue','L'),(5,2,'blue','M'),(5,1,'blue','S'),(6,2,'brown','Adult'),(6,1,'brown','Child'),(7,1,'purple','Adult'),(8,1,'black','L'),(8,3,'black','M'),(8,5,'black','S'),(8,2,'white','L'),(8,4,'white','M'),(8,6,'white','S'),(9,4,'blue','L'),(9,3,'green','L'),(9,2,'green','M'),(9,1,'green','S'),(10,5,'green','L'),(10,3,'green','M'),(10,6,'green','XL'),(10,4,'pink','L'),(10,2,'pink','M'),(10,1,'pink','S');
/*!40000 ALTER TABLE `ItemAttributeMaster` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`%`*/ /*!50003 TRIGGER `arjmart`.`ItemAttributeMaster_BEFORE_INSERT` BEFORE INSERT ON `ItemAttributeMaster` FOR EACH ROW
BEGIN
        IF NEW.ItemAttributeID = 0 THEN
                SELECT COALESCE(MAX(ItemAttributeID) + 1, 1) INTO @ItemAttributeID FROM ItemAttributeMaster WHERE SKU = NEW.SKU;
                SET NEW.ItemAttributeID = @ItemAttributeID;
        END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `ItemMaster`
--

DROP TABLE IF EXISTS `ItemMaster`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ItemMaster` (
  `SKU` int(11) NOT NULL,
  `ItemName` varchar(45) DEFAULT NULL,
  `ItemThumbnails` varchar(500) DEFAULT NULL,
  `ItemDescription` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`SKU`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ItemMaster`
--

LOCK TABLES `ItemMaster` WRITE;
/*!40000 ALTER TABLE `ItemMaster` DISABLE KEYS */;
INSERT INTO `ItemMaster` VALUES (1,'Snoopy T-Shirt','https://goo.gl/VeFQ8f','A casual tee to show of your love for snoopy'),(2,'Jeans','https://goo.gl/AUBzfj','The most fashionable pair of pants, but yet so simple, that it goes with any top'),(3,'Long skirt','https://goo.gl/YSE9TY','A nice, simple skirt, but the most beautiful one you\'ve ever seen'),(4,'Capri','https://goo.gl/MjYf5e','Show your style with an amazing, and bright, pair of pants'),(5,'Denim shorts','https://goo.gl/xkVBqJ','The perfect way to be fashionable, and bear with hot summer weather'),(6,'Cowboy hat','https://goo.gl/GbVdPm','A comfy, but stylish hat to show that you are as tough as a cowboy'),(7,'Night dress','https://goo.gl/V6Gzz9','The perfect, and comfortable way to sleep'),(8,'Plain socks','https://goo.gl/gWAmkJ','5 splendid pairs of super soft socks'),(9,'Winter scarf','https://goo.gl/pSsFyi','A soft, beautiful scarf to keep you warm, and stylish'),(10,'Athletic shorts','https://goo.gl/b4TTEj','The perfect companion for any sports jersey');
/*!40000 ALTER TABLE `ItemMaster` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ItemPrice`
--

DROP TABLE IF EXISTS `ItemPrice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ItemPrice` (
  `SKU` int(11) NOT NULL,
  `ItemAttributeID` int(11) NOT NULL,
  `Price` double NOT NULL,
  PRIMARY KEY (`SKU`,`ItemAttributeID`),
  CONSTRAINT `ItemPrice_SKU_ItemAttributeID` FOREIGN KEY (`SKU`, `ItemAttributeID`) REFERENCES `ItemAttributeMaster` (`SKU`, `ItemAttributeID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ItemPrice`
--

LOCK TABLES `ItemPrice` WRITE;
/*!40000 ALTER TABLE `ItemPrice` DISABLE KEYS */;
INSERT INTO `ItemPrice` VALUES (1,1,7.99),(1,2,7.99),(1,3,8.99),(2,1,11.99),(2,2,11.99),(2,3,11.99),(2,4,12.99),(3,1,10.99),(4,1,10.99),(5,1,5.99),(5,2,5.99),(5,3,5.99),(6,1,3.99),(6,2,3.99),(7,1,7.99),(8,1,6.99),(8,2,6.99),(8,3,6.99),(8,4,6.99),(8,5,6.99),(8,6,6.99),(9,1,6.99),(9,2,6.99),(9,3,6.99),(9,4,6.99),(10,1,5.49),(10,2,5.49),(10,3,5.49),(10,4,5.49),(10,5,5.49),(10,6,5.49);
/*!40000 ALTER TABLE `ItemPrice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `LocationMaster`
--

DROP TABLE IF EXISTS `LocationMaster`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `LocationMaster` (
  `LocationID` int(11) NOT NULL AUTO_INCREMENT,
  `Address` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`LocationID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `LocationMaster`
--

LOCK TABLES `LocationMaster` WRITE;
/*!40000 ALTER TABLE `LocationMaster` DISABLE KEYS */;
INSERT INTO `LocationMaster` VALUES (1,'1425 Kingsmill Ct Coppell TX 75019'),(2,'1600 Pennsylvania Avenue Washington D.C');
/*!40000 ALTER TABLE `LocationMaster` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Order`
--

DROP TABLE IF EXISTS `Order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Order` (
  `OrderID` int(11) NOT NULL AUTO_INCREMENT,
  `UserID` int(11) NOT NULL,
  `OrderStatus` varchar(45) NOT NULL DEFAULT 'Cart',
  PRIMARY KEY (`OrderID`),
  KEY `Order_UserID_idx` (`UserID`),
  CONSTRAINT `Order_UserID` FOREIGN KEY (`UserID`) REFERENCES `User` (`UserID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Order`
--

LOCK TABLES `Order` WRITE;
/*!40000 ALTER TABLE `Order` DISABLE KEYS */;
/*!40000 ALTER TABLE `Order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `OrderLine`
--

DROP TABLE IF EXISTS `OrderLine`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `OrderLine` (
  `OrderID` int(11) NOT NULL,
  `OrderLineID` int(11) NOT NULL DEFAULT '0',
  `SKU` int(11) NOT NULL,
  `ItemAttributeID` int(11) NOT NULL,
  `Quantity` int(11) NOT NULL,
  `Status` varchar(45) NOT NULL DEFAULT 'Pending',
  PRIMARY KEY (`OrderID`,`OrderLineID`),
  UNIQUE KEY `OrderLine_Unique` (`OrderID`,`SKU`,`ItemAttributeID`),
  KEY `OrderLine_OrderID_idx` (`OrderID`),
  KEY `OrderLine_SKU_ItemAttributeID_idx` (`SKU`,`ItemAttributeID`),
  CONSTRAINT `OrderLine_OrderID` FOREIGN KEY (`OrderID`) REFERENCES `Order` (`OrderID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `OrderLine_SKU_ItemAttributeID` FOREIGN KEY (`SKU`, `ItemAttributeID`) REFERENCES `ItemAttributeMaster` (`SKU`, `ItemAttributeID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `OrderLine`
--

LOCK TABLES `OrderLine` WRITE;
/*!40000 ALTER TABLE `OrderLine` DISABLE KEYS */;
/*!40000 ALTER TABLE `OrderLine` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`%`*/ /*!50003 TRIGGER `arjmart`.`OrderLine_BEFORE_INSERT` BEFORE INSERT ON `OrderLine` FOR EACH ROW
BEGIN
        IF NEW.OrderLineID = 0 THEN
                SELECT COALESCE(MAX(OrderLineID) + 1, 1) INTO @OrderLineID FROM OrderLine WHERE OrderID = NEW.OrderID;
                SET NEW.OrderLineID = @OrderLineID;
        END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `User`
--

DROP TABLE IF EXISTS `User`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `User` (
  `UserID` int(11) NOT NULL AUTO_INCREMENT,
  `Email` varchar(45) NOT NULL,
  `Password` varchar(45) NOT NULL,
  `CreditCardNumber` varchar(45) NOT NULL,
  PRIMARY KEY (`UserID`),
  UNIQUE KEY `Email_UNIQUE` (`Email`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `User`
--

LOCK TABLES `User` WRITE;
/*!40000 ALTER TABLE `User` DISABLE KEYS */;
INSERT INTO `User` VALUES (1,'arjun@arjmart.tk','abcd','12345'),(2,'aditi@arjmart.tk','abcd','4444'),(3,'latha@arjmart.tk','password','11223344'),(4,'arnav@arjmart.tk','arjunismean','3141592653589793238');
/*!40000 ALTER TABLE `User` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `UserRoles`
--

DROP TABLE IF EXISTS `UserRoles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `UserRoles` (
  `UserID` int(11) NOT NULL,
  `Role` varchar(45) NOT NULL,
  PRIMARY KEY (`UserID`,`Role`),
  CONSTRAINT `UserRoles_UserID` FOREIGN KEY (`UserID`) REFERENCES `User` (`UserID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `UserRoles`
--

LOCK TABLES `UserRoles` WRITE;
/*!40000 ALTER TABLE `UserRoles` DISABLE KEYS */;
INSERT INTO `UserRoles` VALUES (1,'SuperAdmin');
/*!40000 ALTER TABLE `UserRoles` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-05-17 23:34:56
