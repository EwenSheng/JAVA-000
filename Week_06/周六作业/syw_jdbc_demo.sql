/*
 Navicat Premium Data Transfer

 Source Server         : RDS - DEV
 Source Server Type    : MySQL
 Source Server Version : 50726
 Source Host           : 47.102.152.243:3306
 Source Schema         : syw_jdbc_demo

 Target Server Type    : MySQL
 Target Server Version : 50726
 File Encoding         : 65001

 Date: 22/11/2020 19:50:53
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for account_info
-- ----------------------------
DROP TABLE IF EXISTS `account_info`;
CREATE TABLE `account_info`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_info_id` int(10) NOT NULL COMMENT 'user_info - id ',
  `identify` varchar(25) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '登录凭证 - 帐号',
  `mobile` char(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '登陆凭证 - 手机号',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '密码（8-16位, 数字加大小写组合）',
  `last_login_time` bigint(20) NULL DEFAULT NULL COMMENT '最后登入时间戳',
  `status` tinyint(1) NULL DEFAULT NULL COMMENT '账户状态 - 0/正常 - 1/冻结',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除状态  已删/未删 -> (1/0)',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `update_time` datetime(0) NULL DEFAULT NULL,
  `version` int(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_user_info_id`(`user_info_id`) USING BTREE,
  UNIQUE INDEX `idx_mobile`(`mobile`) USING BTREE,
  INDEX `idx_lk_identify_password`(`identify`, `password`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for account_logon_record
-- ----------------------------
DROP TABLE IF EXISTS `account_logon_record`;
CREATE TABLE `account_logon_record`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `account_info_id` int(11) NOT NULL COMMENT 'account_info - id',
  `terminal_type` tinyint(1) NULL DEFAULT NULL COMMENT '终端类型',
  `logon_time` bigint(13) NULL DEFAULT NULL COMMENT '登陆时间戳(ms级)13位',
  `token` char(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登陆时凭证',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除状态  已删/未删 -> (1/0)',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `update_time` datetime(0) NULL DEFAULT NULL,
  `version` int(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '账户登入记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order_info
-- ----------------------------
DROP TABLE IF EXISTS `order_info`;
CREATE TABLE `order_info`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `num` bigint(19) NOT NULL COMMENT '订单号',
  `time` bigint(13) NULL DEFAULT NULL COMMENT '下单时间',
  `amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '订单总金额',
  `discount_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '优惠金额',
  `freight_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '运费金额',
  `handling_fee` decimal(6, 2) NULL DEFAULT NULL COMMENT '手续费',
  `pay_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '支付金额',
  `status` smallint(2) NULL DEFAULT NULL COMMENT '订单状态',
  `pay_status` smallint(2) NULL DEFAULT NULL COMMENT '支付状态',
  `account_info_id` int(11) NOT NULL,
  `shipping_method` smallint(2) NULL DEFAULT NULL COMMENT '配送方式',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除状态  已删/未删 -> (1/0)',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `update_time` datetime(0) NULL DEFAULT NULL,
  `version` int(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order_logistics_flow
-- ----------------------------
DROP TABLE IF EXISTS `order_logistics_flow`;
CREATE TABLE `order_logistics_flow`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_info_id` int(11) NULL DEFAULT NULL,
  `order_logistics_info_id` int(11) NULL DEFAULT NULL,
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '物流信息(Json格式)',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除状态  已删/未删 -> (1/0)',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `update_time` datetime(0) NULL DEFAULT NULL,
  `version` int(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order_logistics_info
-- ----------------------------
DROP TABLE IF EXISTS `order_logistics_info`;
CREATE TABLE `order_logistics_info`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_info_id` int(11) NOT NULL,
  `logistics_code` int(5) NULL DEFAULT NULL COMMENT '物流公司编号',
  `logistics_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '物流公司名称',
  `tracking_num` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '快递单号',
  `delivery_time` bigint(13) NULL DEFAULT NULL COMMENT '发货时间戳',
  `receipt time` bigint(13) NULL DEFAULT NULL COMMENT '收货时间戳',
  `consignee` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '',
  `consignee_mobile` char(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '',
  `consignee_address` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除状态  已删/未删 -> (1/0)',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `update_time` datetime(0) NULL DEFAULT NULL,
  `version` int(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order_pay_info
-- ----------------------------
DROP TABLE IF EXISTS `order_pay_info`;
CREATE TABLE `order_pay_info`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_info_id` int(11) NULL DEFAULT NULL,
  `pay_num_id` int(11) NULL DEFAULT NULL,
  `out_pay_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `amount` decimal(10, 2) NULL DEFAULT NULL,
  `pay_method` tinyint(1) NULL DEFAULT NULL COMMENT '支付方式 1/支付宝，2/微信，3/银联',
  `pay_time` bigint(13) NULL DEFAULT NULL COMMENT '支付时间戳',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除状态  已删/未删 -> (1/0)',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `update_time` datetime(0) NULL DEFAULT NULL,
  `version` int(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order_product_snapshot_info
-- ----------------------------
DROP TABLE IF EXISTS `order_product_snapshot_info`;
CREATE TABLE `order_product_snapshot_info`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_info_id` int(11) NULL DEFAULT NULL,
  `product_brand_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '品牌名称',
  `product_brand_logo_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '品牌logo图片地址',
  `product_spu_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'spu名称',
  `product_spu_main_url` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '商品主图 多个图片逗号分隔',
  `product_sku_main_url` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '商品介绍主图 多个图片逗号分隔',
  `product_sku_price` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '售价，金额对应的小数位数',
  `item_quantity` int(5) NULL DEFAULT NULL COMMENT '单个商品购买数量',
  `item_total_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '单个商品购买数量 * 商品单价 = 单商品购买总价',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除状态  已删/未删 -> (1/0)',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `update_time` datetime(0) NULL DEFAULT NULL,
  `version` int(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for product_attr
-- ----------------------------
DROP TABLE IF EXISTS `product_attr`;
CREATE TABLE `product_attr`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '销售属性ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '销售属性名称',
  `desc` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '销售属性描述',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除状态  已删/未删 -> (1/0)',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `update_time` datetime(0) NULL DEFAULT NULL,
  `version` int(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '销售属性表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for product_attr_properties
-- ----------------------------
DROP TABLE IF EXISTS `product_attr_properties`;
CREATE TABLE `product_attr_properties`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '销售属性值ID',
  `product_attr_id` int(11) NOT NULL DEFAULT 0 COMMENT 'product_attr - id',
  `value` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '销售属性值',
  `desc` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '销售属性值描述',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除状态  已删/未删 -> (1/0)',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `update_time` datetime(0) NULL DEFAULT NULL,
  `version` int(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '销售属性值' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for product_brand
-- ----------------------------
DROP TABLE IF EXISTS `product_brand`;
CREATE TABLE `product_brand`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '品牌ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '品牌名称',
  `desc` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '品牌描述',
  `logo_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '品牌logo图片地址',
  `logo_banner_urk` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品牌logo banner图片地址',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除状态  已删/未删 -> (1/0)',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `update_time` datetime(0) NULL DEFAULT NULL,
  `version` int(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '品牌表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for product_category
-- ----------------------------
DROP TABLE IF EXISTS `product_category`;
CREATE TABLE `product_category`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `parent_id` int(11) NOT NULL DEFAULT 0 COMMENT '父ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '分类名称',
  `desc` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '分类描述',
  `pic_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '分类图片',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '分类地址',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除状态  已删/未删 -> (1/0)',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `update_time` datetime(0) NULL DEFAULT NULL,
  `version` int(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '类别表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for product_sku
-- ----------------------------
DROP TABLE IF EXISTS `product_sku`;
CREATE TABLE `product_sku`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'SKU ID',
  `product_spu_id` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT 'product_spu - id',
  `product_attr_ids` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '销售属性值,多个销售属性值逗号分隔',
  `banner_url` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'banner图片 多个图片逗号分隔',
  `main_url` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '商品介绍主图 多个图片逗号分隔',
  `price` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '售价，金额对应的小数位数',
  `market_price` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '市场价，金额对应的小数位数',
  `status` tinyint(1) NOT NULL COMMENT 'SKU状态 0-未上架/1-待审核/2-已上架',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除状态  已删/未删 -> (1/0)',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `update_time` datetime(0) NULL DEFAULT NULL,
  `version` int(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'sku表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for product_sku_stock
-- ----------------------------
DROP TABLE IF EXISTS `product_sku_stock`;
CREATE TABLE `product_sku_stock`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `product_sku_id` int(11) NOT NULL DEFAULT 0 COMMENT 'product_sku - id',
  `quantity` int(11) NOT NULL DEFAULT 0 COMMENT '库存',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除状态  已删/未删 -> (1/0)',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `update_time` datetime(0) NULL DEFAULT NULL,
  `version` int(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'sku库存表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for product_spu
-- ----------------------------
DROP TABLE IF EXISTS `product_spu`;
CREATE TABLE `product_spu`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'SPU ID',
  `product_brand_id` int(11) NOT NULL DEFAULT 0 COMMENT '品牌ID',
  `category_id` int(11) NOT NULL DEFAULT 0 COMMENT '分类ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'spu名称',
  `desc` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'spu描述',
  `selling_point` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '卖点',
  `unit` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT 'spu单位',
  `banner_url` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'banner图片 多个图片逗号分隔',
  `main_url` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '商品主图 多个图片逗号分隔',
  `price` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '售价，金额对应的小数位数',
  `market_price` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '市场价，金额对应的小数位数',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除状态  已删/未删 -> (1/0)',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `update_time` datetime(0) NULL DEFAULT NULL,
  `version` int(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'spu表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for product_spu_sku_attr_map
-- ----------------------------
DROP TABLE IF EXISTS `product_spu_sku_attr_map`;
CREATE TABLE `product_spu_sku_attr_map`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `product_spu_id` int(11) NOT NULL DEFAULT 0 COMMENT 'SPU - ID',
  `product_sku_id` int(11) NOT NULL DEFAULT 0 COMMENT 'SKU - ID',
  `product_attr_id` int(11) NOT NULL DEFAULT 0 COMMENT '销售属性ID',
  `product_attr_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '销售属性名称',
  `product_attr_properties_id` int(11) NOT NULL DEFAULT 0 COMMENT '销售属性值ID',
  `product_attr_properties_value` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '销售属性值',
  `status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '状态 -> 1/有效 0/无效',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除状态  已删/未删 -> (1/0)',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `update_time` datetime(0) NULL DEFAULT NULL,
  `version` int(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '关联关系冗余表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_info
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uid` bigint(13) NOT NULL COMMENT '用户平台唯一UID',
  `ture_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户姓名',
  `nick_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户昵称',
  `certificate_no` varchar(48) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '证件号',
  `certificate_type` smallint(2) NULL DEFAULT NULL COMMENT '证件类型',
  `sex_type` tinyint(1) NULL DEFAULT NULL COMMENT '性别',
  `birth_date` date NULL DEFAULT NULL COMMENT '生日',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '删除状态  已删/未删 -> (1/0)',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `update_time` datetime(0) NULL DEFAULT NULL,
  `version` int(10) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
