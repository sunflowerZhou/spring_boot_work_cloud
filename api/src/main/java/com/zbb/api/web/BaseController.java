package com.zbb.api.web;

import com.google.common.collect.Maps;
import com.zbb.exception.GlobalException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

/**
 * @author yingyongzhi
 */
@Slf4j
public class BaseController {

    @Resource
    private HttpServletRequest request;
    @Resource
    private HttpServletResponse response;
    @Resource
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 获取当前登陆用户的userToken
     *
     * @return userToken
     */
    public String getUserToken() {
        return request.getParameter("userToken");
    }

    /**
     * 获取开放API用户的token
     *
     * @return token
     */
    public String getToken() {
        String token = null;
        Enumeration<String> enumeration = request.getHeaders("token");
        if (enumeration != null && enumeration.hasMoreElements()) {
            token = enumeration.nextElement();
        }
        if (StringUtils.isBlank(token)) {
            throw new GlobalException("缺少token!");
        }
        return token;
    }

    /**
     * 获取当前登陆用户的uId
     *
     * @return uses_id
     */
    public String getUid() {
        return request.getParameter("uId");
    }

    /**
     * 获取当前登陆用户的companyId
     *
     * @return 公司id
     */
    public String getCompanyId() {
        return request.getParameter("companyId");
    }

    /**
     * 从redis中获取token值
     *
     * @return token
     */
    public String getTokenValue(String token) {
        Object o = redisTemplate.opsForValue().get(token);
        if (o != null) {
            return o.toString();
        } else {
            throw new GlobalException("登录已过期，请重新登录");
        }
    }

    public String getIpAddress() {
        String ip = request.getHeader("x-forwarded-for");
        String unknown = "unknown";
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        String s = ",";
        if (ip.contains(s)) {
            return ip.split(s)[0];
        } else {
            return ip;
        }
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public RedisTemplate<String, String> getRedisTemplate() {
        return redisTemplate;
    }

    public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Map<Object, Object> getParamsMap() {
        HttpServletRequest request = this.getRequest();
        Map properties = request.getParameterMap();
        Map<Object, Object> returnMap = Maps.newHashMap();
        Iterator<Object> entries = properties.entrySet().iterator();
        Map.Entry<Object, Object> entry;
        String name;
        String value;
        while (entries.hasNext()) {
            entry = (Map.Entry<Object, Object>) entries.next();
            name = (String) entry.getKey();
            Object valueObj = entry.getValue();
            if (null == valueObj) {
                value = "";
                returnMap.put(name, value);
            } else if (valueObj instanceof String[]) {
                String[] values = (String[]) valueObj;
                returnMap.put(name, values);
            } else {
                value = valueObj.toString();
                returnMap.put(name, value);
            }
        }
        return returnMap;
    }
}
