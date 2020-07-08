package com.zbb.api.web;

import com.taobao.api.ApiException;
import com.zbb.bean.Result;
import com.zbb.service.ding.DingTalkServiceUtils;
import com.zbb.vo.AccessTokenVo;
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
@Api(value = "用户")
@Controller
@RequestMapping(name = "/ding/user")
public class UserInfoController extends BaseController{

    @Resource
    private DingTalkServiceUtils dingTalkServiceUtils;

    @RequestMapping(value = "/passwordFreeLogin",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "免密登录(获取用户信息)", httpMethod = "POST")
    public String passwordFreeLogin(String requestAuthCode) throws ApiException {
        AccessTokenVo userInfo = dingTalkServiceUtils.getUserInfo(requestAuthCode);
        return Result.succResult(userInfo);
    }
}
