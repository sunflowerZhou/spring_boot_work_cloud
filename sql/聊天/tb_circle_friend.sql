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

 Date: 04/05/2022 15:31:42
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_circle_friend
-- ----------------------------
DROP TABLE IF EXISTS `tb_circle_friend`;
CREATE TABLE `tb_circle_friend`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `circle_friend_script` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '朋友圈文字内容',
  `circle_friend_picture_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '朋友圈图片地址',
  `circle_firend_state` int NULL DEFAULT NULL COMMENT '朋友圈状态{0发布 1未发布（草稿）}',
  `circle_friend_like` bigint NULL DEFAULT NULL COMMENT '朋友圈发布的内容的点赞',
  `circle_friend_comment` bigint NULL DEFAULT NULL COMMENT '朋友圈发布的内容的评论',
  `created_by` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `created_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `circle_friend_like_id_fk`(`circle_friend_like` ASC) USING BTREE,
  INDEX `circle_friend_comment_id`(`circle_friend_comment` ASC) USING BTREE,
  CONSTRAINT `circle_friend_like_id_fk` FOREIGN KEY (`circle_friend_like`) REFERENCES `tb_like` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `circle_friend_comment_id` FOREIGN KEY (`circle_friend_comment`) REFERENCES `tb_comment` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '朋友圈表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
