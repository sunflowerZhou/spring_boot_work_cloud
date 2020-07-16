package com.zbb.api.web;

import com.taobao.api.ApiException;
import com.zbb.bean.Result;
import com.zbb.service.ding.BhInvitationUserService;
import com.zbb.service.ding.DingTalkServiceUtils;
import com.zbb.vo.AccessTokenVo;
import com.zbb.vo.EnterpriseAuthVo;
import com.zbb.vo.InviteTableVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @author sunflower
 */
@Api("用户")
@Controller
@RequestMapping(name = "/ding/user")
public class UserInfoController{

    @Resource
    private DingTalkServiceUtils dingTalkServiceUtils;

    @Resource
    private BhInvitationUserService bhInvitationUserService;

    @RequestMapping(value = "/passwordFreeLogin",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "免密登录(获取用户信息)", httpMethod = "POST")
    public String passwordFreeLogin(String requestAuthCode) {
        AccessTokenVo userInfo = dingTalkServiceUtils.getUserInfo(requestAuthCode);
        return Result.succResult(userInfo);
    }

    @RequestMapping(value = "/inviteList",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "用户邀请人数", httpMethod = "POST")
    public String inviteInfo(String circleId){
        InviteTableVo inviteTableVo = bhInvitationUserService.inviteList(circleId);
        return Result.succResult(inviteTableVo);
    }

    @RequestMapping(value = "/getToken",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "获取token", httpMethod = "POST")
    public String getToken() throws ApiException {
        EnterpriseAuthVo corpToken = dingTalkServiceUtils.getCorpToken();
        return Result.succResult(corpToken.getAccessToken());
    }

}
