package com.zbb.api.web;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiUserGetuserinfoRequest;
import com.dingtalk.api.response.OapiUserGetuserinfoResponse;
import com.taobao.api.ApiException;
import com.zbb.bean.Result;
import com.zbb.service.ding.DingTalkServiceUtils;
import com.zbb.vo.EnterpriseAuthVo;
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
@Api
@Controller
@RequestMapping(name = "/ding")
public class HelloController {

    @Resource
    private DingTalkServiceUtils dingTalkServiceUtils;

    @RequestMapping(value = "/getCorpToken",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "获取企业凭证", httpMethod = "POST")
    public String getCorpToken() {
        EnterpriseAuthVo corpToken = dingTalkServiceUtils.getCorpToken();
        return Result.succResult(corpToken);
    }
}
