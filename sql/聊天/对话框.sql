-- 聊天 聊天暂时只建一个表   后续做拆分
CREATE TABLE `spring_boot_work_cloud`.`t_chat` (
  `id` bigint(20) NOT NULL COMMENT '' auto_increment,
  `user_friend_id` bigint(20) DEFAULT NULL COMMENT 'user_friend表主键',
  `user_id` bigint(20) DEFAULT '0' COMMENT 'userId',
  `friend_id` bigint(20) DEFAULT NULL COMMENT '朋友userId',
  `message_type` int(1) DEFAULT '0' COMMENT '消息类型 {0:本人发送}{1:朋友发送}',
  `data_type` int(10) DEFAULT '0' COMMENT '数据类型 {0:普通}{1:群}{2:公众号}',
  `msg_context` text DEFAULT null COMMENT '消息内容',
  `msg_type` int(10) DEFAULT null COMMENT '消息类型{0:文本}{1:图片}{2:语音}{3:视频}{4:红包}{5:小程序}{6：表情} 。。。',
  `idRead` int(10) DEFAULT null COMMENT '是否已读{0:}{1：未读}',
  `is_deleted` int(11) DEFAULT '0' COMMENT '是否逻辑删除:默认0未删除;1已删除',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `create_username` varchar(100)  DEFAULT NULL COMMENT '创建人姓名',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间-应用操作时间',
  `create_time_db` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间-数据库操作时间',
  `server_ip` varchar(60) DEFAULT NULL COMMENT '服务器IP',
  `update_username` varchar(100)  DEFAULT NULL COMMENT '最后修改人姓名',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  KEY `user_friend_id_index` (`user_friend_id`) COMMENT 'user_friend表主键',
  KEY `user_id_index` (`user_id`) COMMENT 'userId',
  KEY `friend_id_index` (`friend_id`) COMMENT 'user_friend表主键'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='聊天主表';