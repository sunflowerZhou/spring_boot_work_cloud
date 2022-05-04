/*
 Navicat Premium Data Transfer

 Source Server         : 82.156.188.45
 Source Server Type    : MySQL
 Source Server Version : 80027
 Source Host           : 82.156.188.45:3306
 Source Schema         : spring_boot_work_cloud

 Target Server Type    : MySQL
 Target Server Version : 80027
 File Encoding         : 65001

 Date: 04/05/2022 12:21:02
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_flock
-- ----------------------------
DROP TABLE IF EXISTS `tb_flock`;
CREATE TABLE `tb_flock`  (
  `  id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `flock_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '群名称',
  `flock_qr_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '群二维码',
  `flock_head_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '群头像',
  `flock_notice` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '群公告',
  `flock_remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '群备注',
  `created_by` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `created_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '修改时间',
  `memeber_id` bigint NULL DEFAULT NULL COMMENT '外键 群成员表id',
  PRIMARY KEY (`  id`) USING BTREE,
  INDEX `flock_member_fk`(`memeber_id` ASC) USING BTREE,
  INDEX `flock_name_index`(`flock_name` ASC) USING BTREE COMMENT '群名称',
  CONSTRAINT `flock_member_fk` FOREIGN KEY (`memeber_id`) REFERENCES `tb_member` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '群表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
