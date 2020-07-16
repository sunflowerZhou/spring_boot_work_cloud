package com.zbb.service.ding;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.*;
import com.dingtalk.api.response.*;
import com.taobao.api.ApiException;
import com.zbb.common.util.ding.Constant;
import com.zbb.dao.mapper.BhCodeValueMapper;
import com.zbb.entity.BhCodeValue;
import com.zbb.exception.BusinessException;
import com.zbb.vo.AccessTokenVo;
import com.zbb.vo.EnterpriseAuthVo;
import com.zbb.vo.PermanentAuthCode;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author sunflower
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DingTalkServiceUtils {

    @Resource
    private BhCodeValueMapper bhCodeValueMapper;

    /**
     * 钉钉域名
     */
    private static final String IP = "https://oapi.dingtalk.com";


    /**
     * 获取value 值
     *
     * @param key key
     * @return BhCodeValue
     */
    public BhCodeValue getBhCodeValue(String key) {
        Example example = new Example(BhCodeValue.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("name", key);
        List<BhCodeValue> bhCodeValues = bhCodeValueMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(bhCodeValues)) {
            return bhCodeValues.get(0);

        }
        return null;
    }

    /**
     * 保存
     *
     * @param name key
     * @param value 值
     */
    public void saveBhCodeValue(String name, String value) {
        BhCodeValue bhCodeValue = getBhCodeValue(name);
        if (bhCodeValue != null) {
            bhCodeValue.setValue(value);
            bhCodeValueMapper.updateByPrimaryKeySelective(bhCodeValue);
        } else {
            bhCodeValue = new BhCodeValue();
            bhCodeValue.setName(name);
            bhCodeValue.setValue(value);
            bhCodeValueMapper.insertSelective(bhCodeValue);
        }

    }

    /**
     * 获取企业永久授权码
     *
     * @return PermanentAuthCode
     */
    public PermanentAuthCode getPermanentCode(String tmpAuthCode, String suiteAccessToken) {
        PermanentAuthCode permanentAuthCode = new PermanentAuthCode();
        try {
            DingTalkClient client = new DefaultDingTalkClient(IP + "/service/get_permanent_code?suite_access_token=" + suiteAccessToken);
            OapiServiceGetPermanentCodeRequest req = new OapiServiceGetPermanentCodeRequest();
            // 临时授权码
            req.setTmpAuthCode(tmpAuthCode);
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
    public String activateSuite(String suiteKey, String authCorpId, String permanentCode, String suiteAccessToken) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient(IP + "/service/activate_suite?suite_access_token=" + suiteAccessToken);
        OapiServiceActivateSuiteRequest req = new OapiServiceActivateSuiteRequest();
        req.setSuiteKey(suiteKey);
        req.setAuthCorpid(authCorpId);
        req.setPermanentCode(permanentCode);
        OapiServiceActivateSuiteResponse rsp = client.execute(req);
        System.out.println(rsp.getBody());
        if (rsp.getErrcode() == 0) {
            return "激活成功 ok";
        }
        throw new BusinessException("激活应用失败：" + rsp.getErrmsg());
    }

    /**
     * 获取企业凭证
     *
     * @throws ApiException ApiException
     */
    public EnterpriseAuthVo getCorpToken() throws ApiException {
        DefaultDingTalkClient client = new DefaultDingTalkClient(IP + "/service/get_corp_token");
        OapiServiceGetCorpTokenRequest req = new OapiServiceGetCorpTokenRequest();
        req.setAuthCorpid(Constant.CORP_ID);
        // 获取suiteTicket
        BhCodeValue suiteTicket = getBhCodeValue("suiteTicket");
        // suiteTicketURL参数钉钉推送的suiteTicket。测试应用可以随意填写。
        OapiServiceGetCorpTokenResponse execute = client.execute(req, Constant.SUITE_KEY, Constant.SUITE_SECRET, suiteTicket.getValue());
        EnterpriseAuthVo enterpriseAuthVo = new EnterpriseAuthVo();
        enterpriseAuthVo.setAccessToken(execute.getAccessToken());
        enterpriseAuthVo.setExpiresIn(execute.getExpiresIn());
        return enterpriseAuthVo;
    }


    /**
     * 获取第三方应用凭证
     *
     * @param suiteKey    套件key，开发者后台创建套件后获取
     * @param suiteSecret 套件secret，开发者后台创建套件后获取
     * @param suiteTicket 钉钉推送的suiteTicket。测试应用可以随意填写。
     * @throws ApiException ApiException
     */
    public OapiServiceGetSuiteTokenResponse getSuiteToken(String suiteKey, String suiteSecret, String suiteTicket) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient(IP + "/service/get_suite_token");
        OapiServiceGetSuiteTokenRequest request = new OapiServiceGetSuiteTokenRequest();
        request.setSuiteKey(suiteKey);
        request.setSuiteSecret(suiteSecret);
        request.setSuiteTicket(suiteTicket);
        return client.execute(request);
    }

    /**
     * 获取用户信息
     *
     * @param requestAuthCode 认证码
     * @return 用户信息
     */
    public AccessTokenVo getUserInfo(String requestAuthCode) {
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据用户token 获取邀请人数
     *
     * @param accessToken 当前用户token
     * @param cursor      修改时间，输入上次查询时间，初始传1970-01-01对应的毫秒值
     * @param status      状态0:无效（包括过程数据），1:有效(默认)
     * @param size        每次查询数据量，最大100
     */
    public OapiProjectInviteDataQueryResponse invite(String accessToken, Long cursor, Long status, Long size) {
        try {
            DingTalkClient client = new DefaultDingTalkClient(IP + "/topapi/project/invite/data/query");
            OapiProjectInviteDataQueryRequest req = new OapiProjectInviteDataQueryRequest();
            OapiProjectInviteDataQueryRequest.InviteDataQuery obj1 = new OapiProjectInviteDataQueryRequest.InviteDataQuery();
            obj1.setCursor(cursor);
            obj1.setStatus(status);
            obj1.setSize(size);
            req.setInviteDataQuery(obj1);
            OapiProjectInviteDataQueryResponse rsp = client.execute(req, accessToken);
            System.out.println(rsp.getBody());
            return rsp;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("拉新获取失败：" + e.getMessage());
        }
    }
}
