-- phpMyAdmin SQL Dump
-- version 2.6.4-pl3
-- http://www.phpmyadmin.net
-- 
-- Host: localhost
-- Generation Time: Jun 08, 2007 at 09:50 AM
-- Server version: 5.0.15
-- PHP Version: 5.1.6
-- 
-- Database: `acs`
-- 

-- --------------------------------------------------------

-- 
-- Table structure for table `conference`
-- 

CREATE TABLE `conference` (
  `ID` int(11) NOT NULL auto_increment,
  `TITLE` varchar(50) NOT NULL default 'N/A',
  `OCCURS_ON` varchar(100) NOT NULL default 'N/A',
  `LOCATION` varchar(100) NOT NULL default 'N/A',
  `DESCR` text NOT NULL,
  `DATESTART` datetime NOT NULL,
  `DATEEND` datetime default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=18 ;

-- 
-- Dumping data for table `conference`
-- 

INSERT INTO `conference` VALUES (5, 'a conference', 'jan 20', 'houston', 'just a conf', '2007-04-09 15:56:14', NULL);
INSERT INTO `conference` VALUES (6, 'a conference', 'jan 20', 'houston', 'just a conf', '2007-04-09 15:58:33', NULL);
INSERT INTO `conference` VALUES (15, 'Cold Case Conference', 'Conference rescheduled for Fall 2007', 'Temple College', 'Solving the Case One Piece at at Time.  Discover the hidden truths by utilizing today''s high-tech tools in forensic science to catch a killer long hidden . . . . . but not forgotten!!!', '2007-04-13 09:20:57', NULL);
INSERT INTO `conference` VALUES (16, 'Forensic Conference - Criminalistics', 'May 4, 2007', 'Weatherford College<br>', 'A Conference pertaining to Forensics and Criminalistics.  Special Guest Speaker is Richard Saferstein, from the New Jersey State Crime Laboratory and renoun author. For more information click on <a href="./docs/wc_conf_may.doc">CONFERENCE</a>', '2007-04-13 09:22:31', NULL);
INSERT INTO `conference` VALUES (17, 'Forensic Conference', 'Conference  for 2007 will be held in November or December.  Dates to be posted soon ', 'Tarrant County Medical Examiners Office', 'A very informative Conference concerning forensic evidence and neuropsychology as it relates to criminal behavior.', '2007-04-13 09:23:29', NULL);

-- --------------------------------------------------------

-- 
-- Table structure for table `user`
-- 

CREATE TABLE `user` (
  `ID` varchar(10) NOT NULL,
  `PWD` varchar(10) NOT NULL,
  `FIRST_NAME` varchar(10) NOT NULL,
  `LAST_NAME` varchar(10) NOT NULL,
  `EMAIL` varchar(30) NOT NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- 
-- Dumping data for table `user`
-- 

INSERT INTO `user` VALUES ('c8h10n4o2', 'password', 'James', 'Sandlin', 'jsandlin@gmail.com');
