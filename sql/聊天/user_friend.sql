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

 Date: 04/05/2022 16:03:52
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user_friend
-- ----------------------------
DROP TABLE IF EXISTS `user_friend`;
CREATE TABLE `user_friend`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` bigint NULL DEFAULT NULL COMMENT '用户的ID',
  `friend_id` bigint NULL DEFAULT NULL COMMENT '好友的ID',
  `state` int NULL DEFAULT NULL COMMENT '好友状态{0/待通过 1/通过 2/拒绝 3/已拉黑 4/已删除}',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '好友备注',
  `alias` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '好友别名',
  `source` int NULL DEFAULT NULL COMMENT '好友来源{0/微信 1/扫码 2/群聊}',
  `star` int NULL DEFAULT NULL COMMENT '好友星标{1/星标好友  0/普通好友}',
  `contact_number` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '好友联系电话',
  `creat_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '修改时间',
  `is_deleted` int NULL DEFAULT NULL COMMENT '是否删除{0/是1/否}',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id_index`(`user_id` ASC) USING BTREE COMMENT '用户id',
  INDEX `friend_id_index`(`friend_id` ASC) USING BTREE COMMENT '好友id'
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户好友表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
