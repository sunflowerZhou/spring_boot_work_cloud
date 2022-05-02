package com.zbb.dao.mapper;

import com.zbb.entity.Chat;
import com.zbb.entity.UserFriend;
import com.zbb.entity.UserInfo;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

public interface UserFriendMapper extends Mapper<UserFriend>, MySqlMapper<UserFriend> {

}