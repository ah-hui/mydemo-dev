/*
Navicat MySQL Data Transfer

Source Server         : Mysql
Source Server Version : 50173
Source Host           : localhost:3306
Source Database       : mytest

Target Server Type    : MYSQL
Target Server Version : 50173
File Encoding         : 65001

Date: 2017-10-29 15:47:05
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` tinyint(4) DEFAULT '0',
  `description` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `method` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `request_ip` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `exception_code` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `exception_detail` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `params` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `created_by` bigint(20) DEFAULT NULL,
  `creation_date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
