package com.zbb.dao.mapper;

import com.zbb.entity.BhInvitationUser;
import com.zbb.vo.ObjectInviteTimeDataVo;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

import java.util.List;

/**
 * @author sunflower
 */
public interface BhInvitationUserMapper  extends Mapper<BhInvitationUser>, MySqlMapper<BhInvitationUser> {

    /**
     * 获取拉新数据
     *
     * @param userId todo
     * @return List<BhInvitationUser>
     */
    List<ObjectInviteTimeDataVo> inviteList(String userId);
}