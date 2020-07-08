package com.zbb.service.ding;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.*;
import com.dingtalk.api.response.*;
import com.taobao.api.ApiException;
import com.zbb.exception.BusinessException;
import com.zbb.vo.AccessTokenVo;
import com.zbb.vo.EnterpriseAuthVo;
import com.zbb.vo.PermanentAuthCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author sunflower
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DingTalkServiceUtils {

    /**钉钉域名*/
    private static final String IP = "https://oapi.dingtalk.com";

    /**临时授权码*/
    private static final String TEM_AUTH_CODE = "";

    /**套件令牌Token*/
    private static final String SUITE_ACCESS_TOKEN = "";

    /**套件key*/
    private static final String SUITE_KEY = "";


    /**
     * 获取企业永久授权码
     *
     * @return PermanentAuthCode
     */
    public PermanentAuthCode getPermanentCode() {
        PermanentAuthCode permanentAuthCode = new PermanentAuthCode();
        try {
            DingTalkClient client = new DefaultDingTalkClient(IP + "/service/get_permanent_code?suite_access_token=" + SUITE_ACCESS_TOKEN);
            OapiServiceGetPermanentCodeRequest req = new OapiServiceGetPermanentCodeRequest();
            // 临时授权码
            req.setTmpAuthCode(TEM_AUTH_CODE);
            OapiServiceGetPermanentCodeResponse rsp = client.execute(req);
            System.out.println(rsp.getBody());
            permanentAuthCode.setCorpName(rsp.getAuthCorpInfo().getCorpName());
            permanentAuthCode.setCorpId(rsp.getAuthCorpInfo().getCorpid());
            permanentAuthCode.setPermanentCode(rsp.getPermanentCode());
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return permanentAuthCode;
    }

    /**
     * 激活应用
     */
    public String activateSuite() throws ApiException {
        PermanentAuthCode corpToken = getPermanentCode();

        DingTalkClient client = new DefaultDingTalkClient(IP + "/service/activate_suite?suite_access_token=" + SUITE_ACCESS_TOKEN);
        OapiServiceActivateSuiteRequest req = new OapiServiceActivateSuiteRequest();
        req.setSuiteKey(SUITE_KEY);
        req.setAuthCorpid(corpToken.getCorpId());
        req.setPermanentCode(corpToken.getPermanentCode());
        OapiServiceActivateSuiteResponse rsp = client.execute(req);
        System.out.println(rsp.getBody());
        if (rsp.getErrcode() == 0){
            return corpToken.getCorpId();
        }
        throw new BusinessException("激活应用失败：" + rsp.getErrmsg());
    }

    /**
     * 获取企业凭证
     * @throws ApiException  ApiException
     */
    public EnterpriseAuthVo getCorpToken() throws ApiException {
        PermanentAuthCode corpToken = getPermanentCode();
        DefaultDingTalkClient client = new DefaultDingTalkClient(IP + "/service/get_corp_token");
        OapiServiceGetCorpTokenRequest req = new OapiServiceGetCorpTokenRequest();
        req.setAuthCorpid(corpToken.getCorpId());
        // suiteTicketURL参数钉钉推送的suiteTicket。测试应用可以随意填写。
        OapiServiceGetCorpTokenResponse execute = client.execute(req,SUITE_KEY,"suiteSecrect", "suiteTicket");
        EnterpriseAuthVo enterpriseAuthVo = new EnterpriseAuthVo();
        enterpriseAuthVo.setAccessToken(execute.getAccessToken());
        enterpriseAuthVo.setExpiresIn(execute.getExpiresIn());
        return enterpriseAuthVo;
    }

    /**
     * 获取企业授权信息
     *
     * @throws ApiException ApiException
     */
    public OapiServiceGetAuthInfoResponse getAuthInfo() throws ApiException {
        // 获取企业永久授权码
        PermanentAuthCode corpToken = getPermanentCode();
        DingTalkClient client = new DefaultDingTalkClient(IP + "/service/get_auth_info");
        OapiServiceGetAuthInfoRequest req = new OapiServiceGetAuthInfoRequest();
        req.setAuthCorpid(corpToken.getCorpId());
        return client.execute(req,SUITE_KEY,"suiteSecrect", "suiteTicket");
    }

    /**
     * 获取用户信息
     *
     * @param requestAuthCode 认证码
     * @return 用户信息
     * @throws ApiException ApiException
     */
    public AccessTokenVo getUserInfo(String requestAuthCode) throws ApiException {
        EnterpriseAuthVo corpToken = getCorpToken();
        DingTalkClient client = new DefaultDingTalkClient(IP + "/user/getuserinfo");
        OapiUserGetuserinfoRequest request = new OapiUserGetuserinfoRequest();
        request.setCode(requestAuthCode);
        request.setHttpMethod("GET");
        OapiUserGetuserinfoResponse execute = client.execute(request, corpToken.getAccessToken());
        AccessTokenVo accessTokenVo = new AccessTokenVo();
        accessTokenVo.setSysLevel(execute.getSysLevel());
        accessTokenVo.setSys(execute.getIsSys());
        accessTokenVo.setUserId(execute.getUserid());
        return accessTokenVo;
    }

}
