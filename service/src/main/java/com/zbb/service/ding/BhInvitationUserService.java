package com.zbb.service.ding;

import com.dingtalk.api.response.OapiProjectInviteDataQueryResponse;
import com.google.common.collect.Lists;
import com.zbb.dao.mapper.BhInvitationUserMapper;
import com.zbb.entity.BhCodeValue;
import com.zbb.entity.BhInvitationUser;
import com.zbb.vo.EnterpriseAuthVo;
import com.zbb.vo.InviteListVo;
import com.zbb.vo.InviteTableVo;
import com.zbb.vo.ObjectInviteTimeDataVo;
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
        EnterpriseAuthVo corpToken = null;
        try {
            corpToken = dingTalkServiceUtils.getCorpToken();
        }catch (Exception e){
            e.printStackTrace();
        }
        if (corpToken != null){
            // 获取拉新
            cycle(corpToken.getAccessToken(), timeMillis, thisMethodTime);
        }
    }

    /**
     * 添加拉新集合
     *
     * @param token      token
     * @param timeMillis 时间戳
     */
    public void cycle(String token, long timeMillis, long thisMethodTime) {
        OapiProjectInviteDataQueryResponse invite = dingTalkServiceUtils.invite(token, timeMillis, null, 100L);
        if (invite.getErrcode() == 0){
            List<OapiProjectInviteDataQueryResponse.InviteDataModel> data = invite.getResult().getData();
            if (Long.parseLong(invite.getResult().getNextCursor()) > thisMethodTime) {
                return;
            }
            List<BhInvitationUser> list = Lists.newArrayList();
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
                }
                // 持久化数据
                if (CollectionUtils.isNotEmpty(list)){
                    bhInvitationUserMapper.insertList(list);
                }
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 递归调用
                cycle(token, Long.parseLong(invite.getResult().getNextCursor()), thisMethodTime);
            }
        }
        System.out.println(invite.getErrmsg());
    }

    public InviteTableVo inviteList(String circleId){
        List<ObjectInviteTimeDataVo> timeDataVos = bhInvitationUserMapper.inviteList(circleId);
        InviteTableVo inviteTableVo = new InviteTableVo();
        if (CollectionUtils.isNotEmpty(timeDataVos)){
            timeDataVos = Lists.newArrayList();
            List<InviteListVo> inviteListVos = Lists.newArrayList();
            InviteListVo inviteListVo = new InviteListVo();
            inviteListVo.setTotal(timeDataVos.size());
            inviteListVo.setObjs(timeDataVos);
            inviteListVo.setTypeName("邀请总数");
            inviteListVos.add(inviteListVo);
            inviteTableVo.setData(inviteListVos);
        }
        return inviteTableVo;
    }
}
