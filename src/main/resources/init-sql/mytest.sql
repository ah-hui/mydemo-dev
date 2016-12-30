/*
Navicat MySQL Data Transfer

Source Server         : MySql
Source Server Version : 50173
Source Host           : localhost:3306
Source Database       : mytest

Target Server Type    : MYSQL
Target Server Version : 50173
File Encoding         : 65001

Date: 2016-09-12 13:33:56
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for mytest
-- ----------------------------
DROP TABLE IF EXISTS `mytest`;
CREATE TABLE `mytest` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(20) DEFAULT NULL,
  `sex` varchar(5) DEFAULT NULL,
  `password` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of mytest
-- ----------------------------
INSERT INTO `mytest` VALUES ('1', 'test', 'test', '1');
INSERT INTO `mytest` VALUES ('2', '33', '33', '1');
INSERT INTO `mytest` VALUES ('10', '66', '66', '2');
INSERT INTO `mytest` VALUES ('11', 'test1', '111', '111');
INSERT INTO `mytest` VALUES ('12', 'test2', '111', '111');
