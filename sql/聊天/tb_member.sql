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

 Date: 04/05/2022 12:23:26
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_member
-- ----------------------------
DROP TABLE IF EXISTS `tb_member`;
CREATE TABLE `tb_member`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `is_member_group` int NULL DEFAULT NULL COMMENT '是否群主{0不是，1是}',
  `member_user_id` bigint NULL DEFAULT NULL COMMENT '用户表主键id',
  `is_member_manager` int NULL DEFAULT NULL COMMENT '是否群管理员{0不是，1是}',
  `member_nice_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户在本群的昵称',
  `created_by` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `created_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '修改时间',
  `chat_id` bigint NULL DEFAULT NULL COMMENT '聊天记录表id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `member_chat_fk`(`chat_id` ASC) USING BTREE,
  CONSTRAINT `member_chat_fk` FOREIGN KEY (`chat_id`) REFERENCES `t_chat` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '群成员表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
