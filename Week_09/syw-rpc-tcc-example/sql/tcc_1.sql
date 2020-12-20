/*
 Navicat Premium Data Transfer

 Source Server         : DOCKER_MYSQL_1_M
 Source Server Type    : MySQL
 Source Server Version : 50732
 Source Host           : localhost:3308
 Source Schema         : tcc_1

 Target Server Type    : MySQL
 Target Server Version : 50732
 File Encoding         : 65001

 Date: 20/12/2020 15:20:03
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for rmb_account
-- ----------------------------
DROP TABLE IF EXISTS `rmb_account`;
CREATE TABLE `rmb_account`  (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `user_id` int(10) NOT NULL,
  `balance` decimal(10, 2) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for rmb_freeze_account
-- ----------------------------
DROP TABLE IF EXISTS `rmb_freeze_account`;
CREATE TABLE `rmb_freeze_account`  (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `user_id` int(10) NOT NULL,
  `balance` decimal(10, 2) NOT NULL,
  `account_id` int(10) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for usd_account
-- ----------------------------
DROP TABLE IF EXISTS `usd_account`;
CREATE TABLE `usd_account`  (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `user_id` int(10) NOT NULL,
  `balance` decimal(10, 2) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for usd_freeze_account
-- ----------------------------
DROP TABLE IF EXISTS `usd_freeze_account`;
CREATE TABLE `usd_freeze_account`  (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `user_id` int(10) NOT NULL,
  `balance` decimal(10, 2) NOT NULL,
  `account_id` int(10) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
