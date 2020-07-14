package com.zbb.service.ding;

import com.dingtalk.api.response.OapiProjectInviteDataQueryResponse;
import com.google.common.collect.Lists;
import com.zbb.dao.mapper.BhInvitationUserMapper;
import com.zbb.entity.BhCodeValue;
import com.zbb.entity.BhInvitationUser;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户拉新
 *
 * @author zhaohui
 */
@Service
public class BhInvitationUserService {

    @Resource
    private BhInvitationUserMapper bhInvitationUserMapper;

    @Resource
    private DingTalkServiceUtils dingTalkServiceUtils;

    public void insert(BhInvitationUser bhInvitationUser) {
        bhInvitationUserMapper.insertSelective(bhInvitationUser);
    }

    /**
     * 同步拉新用户
     */
    public void inviteInfo() {
        long thisMethodTime = System.currentTimeMillis();
        // 当前毫秒值
        BhCodeValue bhCodeValue = dingTalkServiceUtils.getBhCodeValue(DingTalkConfig.INVITE_INFO_KEY);
        long timeMillis;
        if (bhCodeValue == null) {
            timeMillis = System.currentTimeMillis();
            dingTalkServiceUtils.saveBhCodeValue(DingTalkConfig.INVITE_INFO_KEY, String.valueOf(timeMillis));
        } else {
            timeMillis = Long.parseLong(bhCodeValue.getValue());
        }
        // 获取当前token
        String token = dingTalkServiceUtils.getToken();
        List<BhInvitationUser> list = Lists.newArrayList();
        // 获取拉新
        cycle(token, timeMillis, list, thisMethodTime);
        // 持久化数据
        if (CollectionUtils.isNotEmpty(list)){
            bhInvitationUserMapper.insertList(list);
        }
    }

    /**
     * 添加拉新集合
     *
     * @param token      token
     * @param timeMillis 时间戳
     * @param list       拉新集合
     */
    public void cycle(String token, long timeMillis, List<BhInvitationUser> list, long thisMethodTime) {
        OapiProjectInviteDataQueryResponse invite = dingTalkServiceUtils.invite(token, timeMillis, null, 100L);
        List<OapiProjectInviteDataQueryResponse.InviteDataModel> data = invite.getResult().getData();
        if (Long.parseLong(invite.getResult().getNextCursor()) > thisMethodTime) {
            return;
        }
        if (CollectionUtils.isNotEmpty(data)) {
            // 说明当前的数据大于100 需要记录下标
            for (OapiProjectInviteDataQueryResponse.InviteDataModel datum : data) {
                BhInvitationUser bhInvitationUser = new BhInvitationUser();
                bhInvitationUser.setExtension(datum.getExtension());
                bhInvitationUser.setChannel(datum.getChannel());
                bhInvitationUser.setStatus(datum.getStatus());
                bhInvitationUser.setJoinAt(datum.getJoinAt());
                bhInvitationUser.setCorpId(datum.getCorpId());
                bhInvitationUser.setInviteUserid(datum.getInviteUserid());
                bhInvitationUser.setGmtModified(datum.getGmtModified());
                bhInvitationUser.setUserId(datum.getUserid());
                list.add(bhInvitationUser);
                // 递归调用
                cycle(token, Long.parseLong(invite.getResult().getNextCursor()), list, thisMethodTime);
            }
        }
    }
}
