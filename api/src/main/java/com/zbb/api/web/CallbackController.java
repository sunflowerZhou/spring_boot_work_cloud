package com.zbb.api.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.response.OapiServiceGetSuiteTokenResponse;
import com.dingtalk.oapi.lib.aes.DingTalkEncryptor;
import com.zbb.common.util.ding.Constant;
import com.zbb.entity.BhCodeValue;
import com.zbb.service.ding.DingTalkServiceUtils;
import com.zbb.vo.PermanentAuthCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * ISV 小程序回调信息处理
 * @author sunflower
 */
@Api("test")
@RestController
@RequestMapping("/ding/receive")
public class CallbackController {

    @Resource
    private DingTalkServiceUtils dingTalkServiceUtils;

    private final Logger bizLogger = LoggerFactory.getLogger("BIZ_CALLBACKCONTROLLER");
    private final Logger mainLogger = LoggerFactory.getLogger(CallbackController.class);

    /**
     * 创建应用，验证回调URL创建有效事件（第一次保存回调URL之前）
     */
    private static final String EVENT_CHECK_CREATE_SUITE_URL = "check_create_suite_url";

    /**
     * 创建应用，验证回调URL变更有效事件（第一次保存回调URL之后）
     */
    private static final String EVENT_CHECK_UPDATE_SUITE_URL = "check_update_suite_url";

    /**
     * suite_ticket推送事件
     */
    private static final String EVENT_SUITE_TICKET = "suite_ticket";

    /**
     * 企业授权开通应用事件
     */
    private static final String EVENT_TMP_AUTH_CODE = "tmp_auth_code";

    @ApiOperation(value = "回调", httpMethod = "POST")
    @RequestMapping(value = "dingCallback", method = RequestMethod.POST)
    public Object dingCallback(
        @RequestParam(value = "signature") String signature,
        @RequestParam(value = "timestamp") Long timestamp,
        @RequestParam(value = "nonce") String nonce,
        @RequestBody(required = false) JSONObject json
    ) {
        String params = " signature:" + signature + " timestamp:" + timestamp + " nonce:" + nonce + " json:" + json;
        try {
            bizLogger.info("begin callback:" + params);
            DingTalkEncryptor dingTalkEncryptor = new DingTalkEncryptor(Constant.TOKEN, Constant.ENCODING_AES_KEY, Constant.SUITE_KEY);

            // 从post请求的body中获取回调信息的加密数据进行解密处理
            String encrypt = json.getString("encrypt");
            String plainText = dingTalkEncryptor.getDecryptMsg(signature, timestamp.toString(), nonce, encrypt);
            JSONObject callBackContent = JSON.parseObject(plainText);
            // 临时授权码
            String authCode = callBackContent.getString("AuthCode");
            // 应用suite_ticket
            String suiteTicketService = callBackContent.getString("SuiteTicket");
            // 根据回调事件类型做不同的业务处理
            String eventType = callBackContent.getString("EventType");
            if (EVENT_CHECK_CREATE_SUITE_URL.equals(eventType)) {
                bizLogger.info("验证新创建的回调URL有效性: " + plainText);
            } else if (EVENT_CHECK_UPDATE_SUITE_URL.equals(eventType)) {
                bizLogger.info("验证更新回调URL有效性: " + plainText);
            } else if (EVENT_SUITE_TICKET.equals(eventType)) {
                // suite_ticket用于用签名形式生成accessToken(访问钉钉服务端的凭证)，需要保存到应用的db。
                // 钉钉会定期向本callback url推送suite_ticket新值用以提升安全性。
                // 应用在获取到新的时值时，保存db成功后，返回给钉钉success加密串（如本demo的return）
                bizLogger.info("应用suite_ticket数据推送: " + plainText);
                // 保存suite_ticket
                dingTalkServiceUtils.saveBhCodeValue("suiteTicket",suiteTicketService);
                
            } else if (EVENT_TMP_AUTH_CODE.equals(eventType)) {
                // 本事件应用应该异步进行授权开通企业的初始化，目的是尽最大努力快速返回给钉钉服务端。用以提升企业管理员开通应用体验
                // 即使本接口没有收到数据或者收到事件后处理初始化失败都可以后续再用户试用应用时从前端获取到corpId并拉取授权企业信息，进而初始化开通及企业。
                bizLogger.info("企业授权开通应用事件: " + plainText);
                // 获取suiteTicket
                BhCodeValue suiteTicket = dingTalkServiceUtils.getBhCodeValue("suiteTicket");
                // 获取第三方应用凭证
                OapiServiceGetSuiteTokenResponse sunflower = dingTalkServiceUtils.getSuiteToken(Constant.SUITE_KEY, Constant.SUITE_SECRET, suiteTicket.getValue());
                // 获取企业永久授权码
                PermanentAuthCode permanentCode = dingTalkServiceUtils.getPermanentCode(authCode,sunflower.getSuiteAccessToken());
                // 激活应用
                String suite = dingTalkServiceUtils.activateSuite(Constant.SUITE_KEY, permanentCode.getCorpId(), permanentCode.getPermanentCode(), sunflower.getSuiteAccessToken());
                System.out.println(suite);
            } else {
                System.out.println("其他类型事件处理");
            }

            // 返回success的加密信息表示回调处理成功
            return dingTalkEncryptor.getEncryptedMap("success", timestamp, nonce);
        } catch (Exception e) {
            //失败的情况，应用的开发者应该通过告警感知，并干预修复
            mainLogger.error("process callback fail." + params, e);
            return "fail";
        }
    }
}
