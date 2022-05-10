-- MySQL dump 10.13  Distrib 5.7.29, for Linux (x86_64)
--
-- Host: localhost    Database: ihiwManagement
-- ------------------------------------------------------
-- Server version	5.7.29-0ubuntu0.18.04.1

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



DROP TABLE IF EXISTS `ihiw_lab`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ihiw_lab` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `lab_code` varchar(255) NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `director` varchar(255) DEFAULT NULL,
  `department` varchar(255) DEFAULT NULL,
  `institution` varchar(255) DEFAULT NULL,
  `address_1` varchar(255) DEFAULT NULL,
  `address_2` varchar(255) DEFAULT NULL,
  `s_address_1` varchar(255) DEFAULT NULL,
  `s_address` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  `zip` varchar(255) DEFAULT NULL,
  `country` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `fax` varchar(255) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `url` varchar(255) DEFAULT NULL,
  `old_lab_code` varchar(255) DEFAULT NULL,
  `s_name` varchar(255) DEFAULT NULL,
  `s_phone` varchar(255) DEFAULT NULL,
  `s_email` varchar(255) DEFAULT NULL,
  `d_name` varchar(255) DEFAULT NULL,
  `d_email` varchar(255) DEFAULT NULL,
  `d_phone` varchar(255) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ihiw_lab`
--

LOCK TABLES `ihiw_lab` WRITE;
/*!40000 ALTER TABLE `ihiw_lab` DISABLE KEYS */;
INSERT INTO `ihiw_lab` VALUES (1,'ACME','Acme Labs','Danny','Director','Danny Director','Department of Science','University of The World','123 Workshop Blvd',NULL,NULL,NULL,'Worktown',NULL,'12345','Netherlands','+31 0123456789',NULL,'central.email.acme.labs@gmail.com',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2020-04-21 14:43:00');
/*!40000 ALTER TABLE `ihiw_lab` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ihiw_user`
--

DROP TABLE IF EXISTS `ihiw_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ihiw_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `phone` varchar(255) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `lab_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_ihiw_user_user_id` (`user_id`),
  KEY `fk_ihiw_user_lab_id` (`lab_id`),
  CONSTRAINT `fk_ihiw_user_lab_id` FOREIGN KEY (`lab_id`) REFERENCES `ihiw_lab` (`id`),
  CONSTRAINT `fk_ihiw_user_user_id` FOREIGN KEY (`user_id`) REFERENCES `jhi_user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ihiw_user`
--

LOCK TABLES `ihiw_user` WRITE;
/*!40000 ALTER TABLE `ihiw_user` DISABLE KEYS */;
INSERT INTO `ihiw_user` VALUES (1,'+31 0123456789',5,1),(2,'',6,1),(7,'',17,1);
/*!40000 ALTER TABLE `ihiw_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jhi_authority`
--

DROP TABLE IF EXISTS `jhi_authority`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jhi_authority` (
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jhi_authority`
--

LOCK TABLES `jhi_authority` WRITE;
/*!40000 ALTER TABLE `jhi_authority` DISABLE KEYS */;
INSERT INTO `jhi_authority` VALUES ('ComponentAffiliate'),('ComponentChair'),('LabMember'),('PI'),('ProjectAffiliate'),('ProjectLeader'),('ROLE_ADMIN'),('ROLE_USER'),('ROLE_VALIDATION'),('WorkshopChair');
/*!40000 ALTER TABLE `jhi_authority` ENABLE KEYS */;
UNLOCK TABLES;


--
-- Table structure for table `jhi_user`
--

DROP TABLE IF EXISTS `jhi_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jhi_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `login` varchar(50) NOT NULL,
  `password_hash` varchar(60) NOT NULL,
  `first_name` varchar(50) DEFAULT NULL,
  `last_name` varchar(50) DEFAULT NULL,
  `email` varchar(191) DEFAULT NULL,
  `image_url` varchar(256) DEFAULT NULL,
  `activated` bit(1) NOT NULL,
  `lang_key` varchar(10) DEFAULT NULL,
  `activation_key` varchar(20) DEFAULT NULL,
  `reset_key` varchar(20) DEFAULT NULL,
  `created_by` varchar(50) NOT NULL,
  `created_date` timestamp NULL DEFAULT NULL,
  `reset_date` timestamp NULL DEFAULT NULL,
  `last_modified_by` varchar(50) DEFAULT NULL,
  `last_modified_date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_user_login` (`login`),
  UNIQUE KEY `ux_user_email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jhi_user`
--

LOCK TABLES `jhi_user` WRITE;
/*!40000 ALTER TABLE `jhi_user` DISABLE KEYS */;
INSERT INTO `jhi_user` VALUES (1,'system','$2a$10$mE.qmcV0mFU5NcKh73TZx.z4ueI/.bDWbj0T1BYyqP481kGGarKLG','System','System','system@localhost','',_binary '','en',NULL,NULL,'system',NULL,NULL,'system',NULL),(2,'anonymoususer','$2a$10$j8S5d7Sr7.8VTOYNviDPOeWX8KcYILUVJBsYV83Y5NtECayypx9lO','Anonymous','User','anonymous@localhost','',_binary '','en',NULL,NULL,'system',NULL,NULL,'system',NULL),(3,'admin','$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC','Administrator','Administrator','admin@localhost','',_binary '','en',NULL,NULL,'system',NULL,NULL,'system',NULL),(4,'user','$2a$10$VEjxo0jq2YG9Rbk2HmX9S.k1uZBGYUHdUcid3g/vfiEl7lwWgOH/K','User','User','user@localhost','',_binary '','en',NULL,NULL,'system',NULL,NULL,'system',NULL),(5,'dannydirector','$2a$10$KgaryBUIwThDojPoijvC4uEPQTr40wj.1s.K8pkseuYGRiMjAa0ei','Danny','Director','dannydirector@gmail.com',NULL,_binary '','en','71092861614722905106','42798578537785050204','anonymousUser','2020-04-21 12:43:51','2020-04-21 12:44:22','admin','2020-04-22 09:03:54'),(6,'larrylabmember','$2a$10$EK7s4RkupFk0uuj3T8I3sOnOftBTDBpdvUFfSVQm5wx1CUxfSf.T2','Larry','Labmember','larrylabmember@gmail.com',NULL,_binary '','en',NULL,NULL,'anonymousUser','2020-04-21 12:48:51',NULL,'admin','2020-04-22 09:03:41'),(17,'pennyprojectleader','$2a$10$HK/iGnH1Dh1tCy5JEA1p3e6JbHd9DBLtCwmSXI1HINgVJXzVHGrWi','Penny','Projectleader','pennyprojectleader@gmail.com',NULL,_binary '','en',NULL,NULL,'anonymousUser','2020-04-22 09:15:25',NULL,'dannydirector','2020-04-22 09:16:06');
/*!40000 ALTER TABLE `jhi_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jhi_user_authority`
--

DROP TABLE IF EXISTS `jhi_user_authority`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jhi_user_authority` (
  `user_id` bigint(20) NOT NULL,
  `authority_name` varchar(50) NOT NULL,
  PRIMARY KEY (`user_id`,`authority_name`),
  KEY `fk_authority_name` (`authority_name`),
  CONSTRAINT `fk_authority_name` FOREIGN KEY (`authority_name`) REFERENCES `jhi_authority` (`name`),
  CONSTRAINT `fk_user_id` FOREIGN KEY (`user_id`) REFERENCES `jhi_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jhi_user_authority`
--

LOCK TABLES `jhi_user_authority` WRITE;
/*!40000 ALTER TABLE `jhi_user_authority` DISABLE KEYS */;
INSERT INTO `jhi_user_authority` VALUES (6,'LabMember'),(17,'LabMember'),(5,'PI'),(5,'ProjectLeader'),(17,'ProjectLeader'),(1,'ROLE_ADMIN'),(3,'ROLE_ADMIN'),(1,'ROLE_USER'),(3,'ROLE_USER'),(4,'ROLE_USER'),(5,'ROLE_USER'),(6,'ROLE_USER'),(17,'ROLE_USER');
/*!40000 ALTER TABLE `jhi_user_authority` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `project`
--

DROP TABLE IF EXISTS `project`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `project` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` text,
  `created_at` datetime DEFAULT NULL,
  `modified_at` datetime DEFAULT NULL,
  `created_by_id` bigint(20) DEFAULT NULL,
  `modified_by_id` bigint(20) DEFAULT NULL,
  `activated` bit(1) DEFAULT b'0',
  `component` varchar(255) NOT NULL DEFAULT 'Bioinformatics',
  PRIMARY KEY (`id`),
  KEY `fk_project_modified_by_id` (`modified_by_id`),
  KEY `fk_project_created_by_id` (`created_by_id`),
  CONSTRAINT `fk_project_created_by_id` FOREIGN KEY (`created_by_id`) REFERENCES `ihiw_user` (`id`) ON DELETE SET NULL,
  CONSTRAINT `fk_project_modified_by_id` FOREIGN KEY (`modified_by_id`) REFERENCES `ihiw_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1 COMMENT='Management microservice entities';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `project`
--

LOCK TABLES `project` WRITE;
/*!40000 ALTER TABLE `project` DISABLE KEYS */;
INSERT INTO `project` VALUES (3,'Sample Project','A generic sample project for demonstration purposes.','2020-04-22 08:47:00','2020-04-22 08:47:00',NULL,NULL,_binary '','Bioinformatics');
/*!40000 ALTER TABLE `project` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `project_lab`
--

DROP TABLE IF EXISTS `project_lab`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `project_lab` (
  `lab_id` bigint(20) NOT NULL,
  `project_id` bigint(20) NOT NULL,
  `status` varchar(255) DEFAULT NULL,
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`),
  KEY `fk_project_lab_project_id` (`project_id`),
  KEY `fk_project_lab_lab_id` (`lab_id`),
  CONSTRAINT `fk_project_lab_lab_id` FOREIGN KEY (`lab_id`) REFERENCES `ihiw_lab` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_project_lab_project_id` FOREIGN KEY (`project_id`) REFERENCES `project` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `project_lab`
--

LOCK TABLES `project_lab` WRITE;
/*!40000 ALTER TABLE `project_lab` DISABLE KEYS */;
INSERT INTO `project_lab` VALUES (1,3,'SUBSCRIBED',1);
/*!40000 ALTER TABLE `project_lab` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `project_leader`
--

DROP TABLE IF EXISTS `project_leader`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `project_leader` (
  `ihiw_user_id` bigint(20) NOT NULL,
  `project_id` bigint(20) NOT NULL,
  PRIMARY KEY (`ihiw_user_id`,`project_id`),
  KEY `fk_project_leader_table` (`project_id`),
  CONSTRAINT `fk_ihiw_user_leader_table` FOREIGN KEY (`ihiw_user_id`) REFERENCES `ihiw_user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_project_leader_table` FOREIGN KEY (`project_id`) REFERENCES `project` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `project_leader`
--

LOCK TABLES `project_leader` WRITE;
/*!40000 ALTER TABLE `project_leader` DISABLE KEYS */;
INSERT INTO `project_leader` VALUES (1,3),(7,3);
/*!40000 ALTER TABLE `project_leader` ENABLE KEYS */;
UNLOCK TABLES;


/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-04-22 13:43:27
