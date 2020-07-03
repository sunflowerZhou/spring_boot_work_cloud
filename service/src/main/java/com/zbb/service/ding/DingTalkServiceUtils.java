package com.zbb.service.ding;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.request.OapiServiceGetCorpTokenRequest;
import com.dingtalk.api.response.OapiServiceGetCorpTokenResponse;
import com.taobao.api.ApiException;
import com.zbb.vo.EnterpriseAuthVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author sunflower
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DingTalkServiceUtils {

    /** 钉钉域名*/
    private static final String IP = "https://oapi.dingtalk.com";

    public EnterpriseAuthVo getCorpToken(){
        EnterpriseAuthVo enterpriseAuthVo = new EnterpriseAuthVo();
        try {
            DefaultDingTalkClient client = new DefaultDingTalkClient(IP+"/service/get_corp_token");
            OapiServiceGetCorpTokenRequest req = new OapiServiceGetCorpTokenRequest();
            req.setAuthCorpid("dingc365fcabbf733c3535c2f4657eb6378f");
            OapiServiceGetCorpTokenResponse execute = client.execute(req,"suiteKey","suiteSecrect", "suiteTicket");
            enterpriseAuthVo.setAccessToken(execute.getAccessToken());
            enterpriseAuthVo.setExpiresIn(execute.getExpiresIn());
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return enterpriseAuthVo;
    }
}
