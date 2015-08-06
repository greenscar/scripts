-- phpMyAdmin SQL Dump
-- version 2.6.4-pl3
-- http://www.phpmyadmin.net
-- 
-- Host: localhost
-- Generation Time: Jun 08, 2007 at 09:50 AM
-- Server version: 5.0.15
-- PHP Version: 5.1.6
-- 
-- Database: `environments`
-- 

-- --------------------------------------------------------

-- 
-- Table structure for table `application`
-- 

CREATE TABLE `application` (
  `APP_ID` int(11) NOT NULL auto_increment,
  `NAME` varchar(30) NOT NULL,
  PRIMARY KEY  (`APP_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=8 ;

-- 
-- Dumping data for table `application`
-- 

INSERT INTO `application` VALUES (1, 'TIERS App');
INSERT INTO `application` VALUES (2, 'Self Service Portal');
INSERT INTO `application` VALUES (3, 'State Portal');
INSERT INTO `application` VALUES (4, 'TIERS App (MRS)');
INSERT INTO `application` VALUES (5, 'TIERS Batch');
INSERT INTO `application` VALUES (6, 'MAXeIE');
INSERT INTO `application` VALUES (7, 'MAXeCHIP');

-- --------------------------------------------------------

-- 
-- Table structure for table `batch`
-- 

CREATE TABLE `batch` (
  `ENV_ID` int(11) NOT NULL,
  `BOX` varchar(10) NOT NULL,
  `PATH` varchar(15) NOT NULL,
  `MQ_ID` varchar(15) NOT NULL,
  `DB_ID` varchar(10) NOT NULL,
  `DATE_START` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `DATE_END` timestamp NULL default NULL,
  PRIMARY KEY  (`BOX`,`PATH`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- 
-- Dumping data for table `batch`
-- 

INSERT INTO `batch` VALUES (6, 'iedadu002', '/TIERS/AGING01', 'MIG_QM3', '218', '2006-12-28 13:08:23', NULL);
INSERT INTO `batch` VALUES (7, 'iedadu002', '/TIERS/AGING02', 'TIERS.QM1', '219', '2006-12-28 13:08:23', NULL);
INSERT INTO `batch` VALUES (4, 'iedadu002', '/TIERS/APT01', 'TIERS.QM15', '640', '2006-12-28 13:08:23', NULL);
INSERT INTO `batch` VALUES (5, 'iedadu002', '/TIERS/APT02', 'TIERS.QM17', '630', '2006-12-28 13:08:23', NULL);
INSERT INTO `batch` VALUES (8, 'iedadu004', '/TIERS/AGING03', 'TIERS.QM4', '229', '2006-12-28 13:08:23', NULL);
INSERT INTO `batch` VALUES (1, 'iedadu004', '/TIERS/AST01', 'TIERS.QM6', '210', '2006-12-28 13:08:23', NULL);
INSERT INTO `batch` VALUES (2, 'iedadu004', '/TIERS/AST02', 'TIERS.QM9', '240', '2006-12-28 13:08:23', NULL);
INSERT INTO `batch` VALUES (3, 'iedadu004', '/TIERS/AST03', 'TIERS.QM10', '611', '2006-12-28 13:08:23', NULL);
INSERT INTO `batch` VALUES (9, 'iedadu004', '/TIERS/MISC01', 'TIERS.QM2', '230', '2006-12-28 13:08:23', NULL);
INSERT INTO `batch` VALUES (10, 'iedadu004', '/TIERS/MISC02', 'TIERS.QM3', '220', '2006-12-28 13:08:23', NULL);

-- --------------------------------------------------------

-- 
-- Table structure for table `environment`
-- 

CREATE TABLE `environment` (
  `ENV_ID` int(11) NOT NULL auto_increment,
  `NAME` varchar(30) NOT NULL,
  PRIMARY KEY  (`ENV_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=25 ;

-- 
-- Dumping data for table `environment`
-- 

INSERT INTO `environment` VALUES (1, 'AST01');
INSERT INTO `environment` VALUES (2, 'AST02');
INSERT INTO `environment` VALUES (3, 'AST03');
INSERT INTO `environment` VALUES (4, 'APT01');
INSERT INTO `environment` VALUES (5, 'APT02');
INSERT INTO `environment` VALUES (6, 'AGING01');
INSERT INTO `environment` VALUES (7, 'AGING02');
INSERT INTO `environment` VALUES (8, 'AGING03');
INSERT INTO `environment` VALUES (9, 'MISC01');
INSERT INTO `environment` VALUES (10, 'MISC02');
INSERT INTO `environment` VALUES (11, 'IPT');
INSERT INTO `environment` VALUES (12, 'PROD FIX');
INSERT INTO `environment` VALUES (13, 'GENX');
INSERT INTO `environment` VALUES (15, 'CONV01');
INSERT INTO `environment` VALUES (16, 'CONV02');
INSERT INTO `environment` VALUES (17, 'COLA');
INSERT INTO `environment` VALUES (18, 'DAR');
INSERT INTO `environment` VALUES (19, 'PSR');
INSERT INTO `environment` VALUES (20, 'PERF TUNE');
INSERT INTO `environment` VALUES (21, 'PERF LAST');
INSERT INTO `environment` VALUES (22, 'CONV1');
INSERT INTO `environment` VALUES (23, 'CONV2');
INSERT INTO `environment` VALUES (24, 'PRODFIX');

-- --------------------------------------------------------

-- 
-- Table structure for table `mq`
-- 

CREATE TABLE `mq` (
  `MQ_ID` varchar(15) NOT NULL,
  `HOST` varchar(30) NOT NULL,
  `PORT` int(11) NOT NULL,
  `MGW` varchar(20) NOT NULL,
  `CHANNEL` varchar(20) NOT NULL,
  `DATE_START` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `DATE_END` timestamp NULL default NULL,
  PRIMARY KEY  (`MQ_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- 
-- Dumping data for table `mq`
-- 

INSERT INTO `mq` VALUES ('MIG_QM3', 'IETAWU003', 1417, '', 'TIERS.SVRCONN.CHL', '2007-03-08 19:30:28', NULL);
INSERT INTO `mq` VALUES ('TAA.TEST2', 'IEDAAU016', 1436, '', 'TIERS.SVRCONN.CHL', '2007-03-08 19:42:58', NULL);
INSERT INTO `mq` VALUES ('TAA.TEST3', 'IEDAAU016', 1437, '', 'TIERS.SVRCONN.CHL', '2007-03-08 19:42:58', NULL);
INSERT INTO `mq` VALUES ('TAA.TEST6', 'IEDAAU016', 1440, '', 'TIERS.SVRCONN.CHL', '2007-03-08 19:43:42', NULL);
INSERT INTO `mq` VALUES ('TIERS.QM1', '165.184.38.210', 1425, '', 'TIERS.SVRCONN.CHL', '2007-03-08 19:30:28', NULL);
INSERT INTO `mq` VALUES ('TIERS.QM10', 'iedaau006', 1424, '', 'TIERS.SVRCONN.CHL', '2007-03-08 19:30:28', NULL);
INSERT INTO `mq` VALUES ('TIERS.QM11', 'IEDAAU006', 1414, '', 'TIERS.SVRCONN.CHL', '2007-03-08 19:30:28', NULL);
INSERT INTO `mq` VALUES ('TIERS.QM12', 'iedaau006.txaccess.net', 1415, '', 'TIERS.SVRCONN.CHL', '2007-03-08 21:22:00', NULL);
INSERT INTO `mq` VALUES ('TIERS.QM13', '165.184.38.210', 1416, '', 'TIERS.SVRCONN.CHL', '2007-03-08 19:43:42', NULL);
INSERT INTO `mq` VALUES ('TIERS.QM14', 'iedaau006.txaccess.net', 1417, '', 'TIERS.SVRCONN.CHL', '2007-03-08 19:32:11', NULL);
INSERT INTO `mq` VALUES ('TIERS.QM15', 'IEDAAU006', 1429, '', 'TIERS.SVRCONN.CHL', '2007-03-08 19:30:28', NULL);
INSERT INTO `mq` VALUES ('TIERS.QM17', 'IEDAAU006', 1431, '', 'TIERS.SVRCONN.CHL', '2007-03-08 19:30:28', NULL);
INSERT INTO `mq` VALUES ('TIERS.QM2', 'IEDAAU006', 1426, '', 'TIERS.SVRCONN.CHL', '2007-03-08 19:30:28', NULL);
INSERT INTO `mq` VALUES ('TIERS.QM3', 'IEDAAU006', 1427, '', 'TIERS.SVRCONN.CHL', '2007-03-08 19:30:28', NULL);
INSERT INTO `mq` VALUES ('TIERS.QM4', 'iedaau006', 1418, '', 'TIERS.SVRCONN.CHL', '2007-03-08 19:30:28', NULL);
INSERT INTO `mq` VALUES ('TIERS.QM6', 'iedaau006', 1420, '', 'TIERS.SVRCONN.CHL', '2007-03-08 19:30:28', NULL);
INSERT INTO `mq` VALUES ('TIERS.QM9', 'iedaau006', 1423, '', 'TIERS.SVRCONN.CHL', '2007-03-08 19:30:28', NULL);

-- --------------------------------------------------------

-- 
-- Table structure for table `schema`
-- 

CREATE TABLE `schema` (
  `ID` int(11) NOT NULL,
  `BOX` varchar(10) NOT NULL,
  `PORT` int(11) NOT NULL,
  `DB` varchar(10) NOT NULL,
  `UID` varchar(30) NOT NULL,
  `PWD` varchar(30) NOT NULL,
  `URL` text NOT NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- 
-- Dumping data for table `schema`
-- 

INSERT INTO `schema` VALUES (210, 'iedadu004', 1521, 'TRDEV', 'TIERS3ATRNTEST2CON', 'TIERS3ATRNTEST2CON1C', 'jdbc:oracle:thin:@iedadu004:1521:TRDEV');
INSERT INTO `schema` VALUES (218, 'iedadu002', 1521, 'TRTSTTRN', 'TIERS3TRNDEVCON', 'NEVESREVO62TCO', 'jdbc:oracle:thin:@iedadu002:1521:TRTSTTRN');
INSERT INTO `schema` VALUES (219, 'iedadu002', 1521, 'TRTSTTRN', 'TIERS3TRNTEST1CON', 'ONEOFF', 'jdbc:oracle:thin:@iedadu002:1521:trtsttrn');
INSERT INTO `schema` VALUES (220, 'iedadu004', 1521, 'TRDEVTR', 'TIERS3ATRNTEST2CON', 'JASDJASDF89WJSK_TNR', 'jdbc:oracle:thin:@iedadu004:1521:TRDEVTR');
INSERT INTO `schema` VALUES (229, 'iedadu004', 1521, 'TRDEVTR', 'TIERS3ATRNTEST1CON', 'JAKDURQBNFDBK', 'jdbc:oracle:thin:@iedadu004:1521:TRDEVTR');
INSERT INTO `schema` VALUES (240, 'iedadu004', 1521, 'TRDEVBT', 'TIERS3ATRNTEST2CON', 'TIERS3ATRNTEST2CON6M', 'jdbc:oracle:thin:@iedadu004:1521:trdevbt');
INSERT INTO `schema` VALUES (605, 'iedadu002', 1521, 'TRTSTOLT', 'TIERSPRODDEVCON', 'ANYAK82UI', 'jdbc:oracle:thin:@iedadu002:1521:trtstolt');
INSERT INTO `schema` VALUES (610, 'iedadu002', 1521, 'TRTSTOLT', 'TIERS3ATRNTEST2CON', 'G05pur5G0', 'jdbc:oracle:thin:@iedadu002:1521:trtstolt');
INSERT INTO `schema` VALUES (611, 'iedadu002', 1521, 'TRTSTOLT', 'TIERS3TRNTEST1CON', 'WHAT3V3R', 'jdbc:oracle:thin:@iedadu002:1521:TRTSTOLT');
INSERT INTO `schema` VALUES (630, 'iedadu002', 1521, 'TRTSTBTC', 'TIERS3ATRNTEST2CON', 's00p3rs3cr3t', 'jdbc:oracle:thin:@iedadu002:1521:trtstbtc');
INSERT INTO `schema` VALUES (640, 'iedadu002', 1521, 'TRTSTTRN', 'TIERS3ATRNTEST2CON', 'W1ll13w0nka', 'jdbc:oracle:thin:@iedadu002:1521:trtsttrn');
INSERT INTO `schema` VALUES (650, 'iedadu002', 1521, 'TRTSTIPT', 'TIERS2CON', 'S00P3RS3CR3T', 'jdbc:oracle:thin:@iedadu002:1521:trtstipt');
INSERT INTO `schema` VALUES (680, 'iedadu002', 1521, 'TRTSTDA', 'TIERSCON', 'JAJDW83A9JDLA', 'jdbc:oracle:thin:@iedadu002:1521:trtstda');
INSERT INTO `schema` VALUES (700, 'iedadu001', 1521, 'TRSTAR', 'TIERS2CON', 'KT5S48VTU', 'jdbc:oracle:thin:@iedadu001:1521:trstar');
INSERT INTO `schema` VALUES (8010, 'iedadu001', 1525, 'TRTSTC1', 'TIERS2CON', 'KAU3JAKSD923LS', 'jdbc:oracle:thin:@(DESCRIPTION =(ENABLE=BROKEN)(LOAD_BALANCE=ON)(ADDRESS_LIST = (ADDRESS = (PROTOCOL = TCP)(HOST = iedadu001)(PORT = 1525))(ADDRESS = (PROTOCOL = TCP)(HOST = iedadu002)(PORT = 1525)))(CONNECT_DATA = (SERVICE_NAME = TRTSTC1.txaccess.net)))');
INSERT INTO `schema` VALUES (8020, 'iedadu001', 1525, 'TRTSTC2', 'TIERS2CON', 'H39HD04MVHWOSL5', 'jdbc:oracle:thin:@(DESCRIPTION=(ENABLE=BROKEN)(LOAD_BALANCE=ON)(ADDRESS_LIST=(ADDRESS=(PROTOCOL=TCP)(HOST=iedadu001)(PORT=1525))(ADDRESS=(PROTOCOL=TCP)(HOST=iedadu002)(PORT=1525)))(CONNECT_DATA=(SERVICE_NAME=TRTSTC2.txaccess.net)))');

-- --------------------------------------------------------

-- 
-- Table structure for table `test`
-- 

CREATE TABLE `test` (
  `ID` int(11) NOT NULL,
  `BOX` varchar(10) NOT NULL,
  `PORT` int(11) NOT NULL,
  `DB` varchar(10) NOT NULL,
  `UID` varchar(30) NOT NULL,
  `PWD` varchar(30) NOT NULL,
  `URL` text NOT NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- 
-- Dumping data for table `test`
-- 


-- --------------------------------------------------------

-- 
-- Table structure for table `ws_application`
-- 

CREATE TABLE `ws_application` (
  `ENV_ID` int(11) NOT NULL,
  `APP_ID` int(11) NOT NULL,
  `WS_APP_NAME` varchar(30) NOT NULL,
  `CLUSTER` varchar(15) NOT NULL,
  `WS_CELL_NAME` varchar(30) NOT NULL,
  `DNS` varchar(15) NOT NULL,
  `MQ_ID` varchar(15) NOT NULL,
  `DB_ID` varchar(10) NOT NULL,
  `DATE_START` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `DATE_END` timestamp NULL default NULL,
  PRIMARY KEY  (`WS_APP_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- 
-- Dumping data for table `ws_application`
-- 

INSERT INTO `ws_application` VALUES (17, 1, 'stage3tiersCOLA', 'iedaau002', 'tiers_misc_Network', 'tiers-cola', 'TIERS.QM12', '700', '2007-03-08 21:22:00', NULL);
INSERT INTO `ws_application` VALUES (22, 1, 'stage3tiersCONV1', 'iedaau002', 'tiers_conv_Network', 'tiers-conv1', 'TIERS.QM14', '8010', '2007-03-08 21:22:00', NULL);
INSERT INTO `ws_application` VALUES (23, 1, 'stage3tiersCONV2', 'iedaau002', 'tiers_conv_Network', 'tiers-conv2', 'TAA.TEST3', '8020', '2007-03-08 21:22:00', NULL);
INSERT INTO `ws_application` VALUES (18, 1, 'stage3tiersDAR', 'iedaau002', 'tiers_dar_Network', 'tiers-dar', 'TIERS.QM13', '680', '2007-03-08 21:22:00', NULL);
INSERT INTO `ws_application` VALUES (13, 1, 'stage3tiersGENX', 'iedaau002', 'tiers_genx_Network', 'tiers-genx', 'TIERS.QM12', '700', '2007-03-08 21:22:00', NULL);
INSERT INTO `ws_application` VALUES (11, 1, 'stage3tiersIPT', 'iedaau002', 'tiers_ipt_Network', 'tiers-ipt02', 'TIERS.QM11', '650', '2007-03-08 21:22:00', NULL);
INSERT INTO `ws_application` VALUES (24, 1, 'stage3tiersPRODFIX', 'iedaau002', 'tiers_prodfix_Network', 'tiers-prodfix', 'TAA.TEST6', '610', '2007-03-08 21:22:00', NULL);
INSERT INTO `ws_application` VALUES (19, 1, 'stage3tiersPSR', 'iedaau002', 'tiers_prodfix_Network', 'tiers-psr', 'TAA.TEST2', '605', '2007-03-08 21:22:00', NULL);
INSERT INTO `ws_application` VALUES (6, 1, 'tiersaging01', 'iedaau002', 'tiers_aging_Network', 'tiers-aging01', 'MIG_QM3', '218', '2007-03-08 21:22:00', NULL);
INSERT INTO `ws_application` VALUES (7, 1, 'tiersaging02', 'iedaau002', 'tiers_aging_Network', 'tiers-aging02', 'TIERS.QM1', '219', '2007-03-08 21:22:00', NULL);
INSERT INTO `ws_application` VALUES (8, 1, 'tiersaging03', 'iedaau002', 'tiers_aging_Network', 'tiers-aging03', 'TIERS.QM4', '229', '2007-03-08 21:22:00', NULL);
INSERT INTO `ws_application` VALUES (4, 1, 'tiersapt01', 'iedaau002', 'tiers_apt_Network', 'tiers-apt01', 'TIERS.QM15', '630', '2007-03-08 21:22:00', NULL);
INSERT INTO `ws_application` VALUES (5, 1, 'tiersapt02', 'iedaau002', 'tiers_apt_Network', 'tiers-apt02', 'TIERS.QM17', '640', '2007-03-08 21:22:00', NULL);
INSERT INTO `ws_application` VALUES (1, 1, 'tiersast01', 'iedaau002', 'tiers_ast_Network', 'tiers-ast01', 'TIERS.QM6', '210', '2007-03-08 21:22:00', NULL);
INSERT INTO `ws_application` VALUES (2, 1, 'tiersast02', 'iedaau002', 'tiers_ast_Network', 'tiers-ast02', 'TIERS.QM9', '240', '2007-03-08 21:22:00', NULL);
INSERT INTO `ws_application` VALUES (3, 1, 'tiersast03', 'iedaau002', 'tiers_ast_Network', 'tiers-ast03', 'TIERS.QM10', '611', '2007-03-08 21:22:00', NULL);
INSERT INTO `ws_application` VALUES (9, 1, 'tiersmisc01', 'iedaau002', 'tiers_misc_Network', 'tiers-misc01', 'TIERS.QM2', '220', '2007-03-08 21:22:00', NULL);
INSERT INTO `ws_application` VALUES (10, 1, 'tiersmisc02', 'iedaau002', 'tiers_misc_Network', 'tiers-misc02', 'TIERS.QM3', '220', '2007-03-08 21:22:00', NULL);

-- --------------------------------------------------------

-- 
-- Table structure for table `ws_appserver`
-- 

CREATE TABLE `ws_appserver` (
  `SERVER_NAME` varchar(20) NOT NULL,
  `NODE` varchar(9) NOT NULL,
  `PORT` int(11) NOT NULL default '0',
  `WS_APP_NAME` varchar(30) NOT NULL,
  `DATE_START` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `DATE_END` timestamp NULL default NULL,
  PRIMARY KEY  (`SERVER_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- 
-- Dumping data for table `ws_appserver`
-- 

INSERT INTO `ws_appserver` VALUES ('aging01-server2', 'iedaau002', 0, 'tiersaging01', '2007-03-08 21:22:00', NULL);
INSERT INTO `ws_appserver` VALUES ('aging02-server2', 'iedaau002', 0, 'tiersaging02', '2007-03-08 21:22:00', NULL);
INSERT INTO `ws_appserver` VALUES ('aging03-server2', 'iedaau002', 0, 'tiersaging03', '2007-03-08 21:22:00', NULL);
INSERT INTO `ws_appserver` VALUES ('apt01-server2', 'iedaau002', 0, 'tiersapt01', '2007-03-08 21:22:00', NULL);
INSERT INTO `ws_appserver` VALUES ('apt02-server2', 'iedaau002', 0, 'tiersapt02', '2007-03-08 21:22:00', NULL);
INSERT INTO `ws_appserver` VALUES ('ast01-server2', 'iedaau002', 0, 'tiersast01', '2007-03-08 21:22:00', NULL);
INSERT INTO `ws_appserver` VALUES ('ast02-server2', 'iedaau002', 0, 'tiersast02', '2007-03-08 21:22:00', NULL);
INSERT INTO `ws_appserver` VALUES ('ast03-server2', 'iedaau002', 0, 'tiersast03', '2007-03-08 21:22:00', NULL);
INSERT INTO `ws_appserver` VALUES ('cola-server2', 'iedaau002', 0, 'stage3tiersCOLA', '2007-03-08 21:22:00', NULL);
INSERT INTO `ws_appserver` VALUES ('conv1-server2', 'iedaau002', 0, 'stage3tiersCONV1', '2007-03-08 21:22:00', NULL);
INSERT INTO `ws_appserver` VALUES ('conv2-server2', 'iedaau002', 0, 'stage3tiersCONV2', '2007-03-08 21:22:00', NULL);
INSERT INTO `ws_appserver` VALUES ('dar-server2', 'iedaau002', 0, 'stage3tiersDAR', '2007-03-08 21:22:00', NULL);
INSERT INTO `ws_appserver` VALUES ('genx-server2', 'iedaau002', 0, 'stage3tiersGENX', '2007-03-08 21:22:00', NULL);
INSERT INTO `ws_appserver` VALUES ('ipt-server2', 'iedaau002', 0, 'stage3tiersIPT', '2007-03-08 21:22:00', NULL);
INSERT INTO `ws_appserver` VALUES ('misc01-server2', 'iedaau002', 0, 'tiersmisc01', '2007-03-08 21:22:00', NULL);
INSERT INTO `ws_appserver` VALUES ('misc02-server2', 'iedaau002', 0, 'tiersmisc02', '2007-03-08 21:22:00', NULL);
INSERT INTO `ws_appserver` VALUES ('prodfix-server2', 'iedaau002', 0, 'stage3tiersPRODFIX', '2007-03-08 21:22:00', NULL);
INSERT INTO `ws_appserver` VALUES ('psr-server2', 'iedaau002', 0, 'stage3tiersPSR', '2007-03-08 21:22:00', NULL);

-- --------------------------------------------------------

-- 
-- Table structure for table `ws_cell`
-- 

CREATE TABLE `ws_cell` (
  `WS_CELL_NAME` varchar(30) NOT NULL,
  `ADMIN_HTTP` varchar(50) NOT NULL,
  PRIMARY KEY  (`WS_CELL_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- 
-- Dumping data for table `ws_cell`
-- 

INSERT INTO `ws_cell` VALUES ('tiers_aging_Network', 'https://iedaau018:14204/admin/');
INSERT INTO `ws_cell` VALUES ('tiers_apt_Network', 'https://iedaau018:10504/admin/logon.jsp');
INSERT INTO `ws_cell` VALUES ('tiers_ast_Network', 'http://iedaau018:14503/admin/secure/logon.do');
INSERT INTO `ws_cell` VALUES ('tiers_conv_Network', 'http://iedaau018:14003/admin');
INSERT INTO `ws_cell` VALUES ('tiers_dar_Network', 'http://iedaau018:10903/admin');
INSERT INTO `ws_cell` VALUES ('tiers_dr_Network', 'http://iedaau018:10603/admin');
INSERT INTO `ws_cell` VALUES ('tiers_genx_Network', 'http://iedaau018:10803/admin');
INSERT INTO `ws_cell` VALUES ('tiers_ipt_Network', 'http://iedaau018:10203/admin');
INSERT INTO `ws_cell` VALUES ('tiers_last_Network', 'http://iedaau018:14103/admin');
INSERT INTO `ws_cell` VALUES ('tiers_misc_Network', 'http://iedaau018:14303/admin/');
INSERT INTO `ws_cell` VALUES ('tiers_prodfix_Network', 'http://iedaau018:10703/admin');
INSERT INTO `ws_cell` VALUES ('tiers_training_Network', 'http://ietsau001:11003/admin');
INSERT INTO `ws_cell` VALUES ('tiers_trainpractice_Network', 'http://ietsau001:11103/admin');
