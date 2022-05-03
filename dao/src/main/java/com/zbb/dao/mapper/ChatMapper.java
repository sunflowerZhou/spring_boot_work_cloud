package com.zbb.dao.mapper;

import com.zbb.entity.Chat;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @author zhoubobing
 */
public interface ChatMapper  extends Mapper<Chat>, MySqlMapper<Chat> {
}