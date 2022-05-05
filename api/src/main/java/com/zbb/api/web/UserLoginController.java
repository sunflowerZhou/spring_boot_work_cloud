package com.zbb.api.web;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.alibaba.fastjson.JSONObject;
import com.zbb.bean.Result;
import com.zbb.entity.UserInfo;
import com.zbb.service.UserInfoService;
import org.apache.commons.codec.language.bm.Lang;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * @auther： hmq
 * @version： 1.0
 * @date 2022/5/2
 * @desc：
 **/
@Controller
@RequestMapping(value = "/userInfo")
public class UserLoginController {
    private static final Log log = LogFactory.get();

    @Resource
    private UserInfoService userInfoService;
    @Resource
    private HttpServletResponse response;

    /**
     * 用户登录
     */
    @ResponseBody
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String userLogin(@RequestParam("mail") String mail, @RequestParam("userpwd") String pwd) {
        try {
            UserInfo userInfo = userInfoService.loginUser(mail, pwd);
            if (userInfo != null) {
                String s = JSONObject.toJSONString(userInfo);
                Cookie cookie = new Cookie("userLogin", s);
                response.addCookie(cookie);
                return Result.succResult("登录成功");
            }
        } catch (Exception e) {
            log.error("异常信息{}", e.getMessage());
            return Result.exceptionResult(e);
        }
        return Result.failResult("登录失败");
    }

    /**
     * 用户注册
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public String userInsert(@RequestParam("mail") String mail, @RequestParam("userpwd") String pwd) {
        try {
            Integer integer = userInfoService.insertUser(mail, pwd);
            if (integer > 0) {
                return Result.succResult("注册成功");
            }
        } catch (Exception e) {
            log.error("异常信息{}", e.getMessage());
            return Result.failResult("注册失败");
        }
        return Result.failResult("注册失败");
    }

    /**
     * 用户修改
     */
    @RequestMapping(value = "/user", method = RequestMethod.PUT)
    @ResponseBody
    public String userUpdate(@RequestBody UserInfo userInfo) {

        String s = "";
        try {

            s = userInfoService.updateUser(userInfo);

            return Result.failResult(s);
        } catch (Exception e) {
            log.error("异常信息{}", e.getMessage());
            return Result.failResult(s);
        }
    }

    /**
     * 用户删除
     */
    @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public String deletedUser(@PathVariable("id") Lang id) {
        String s = "";
        try {
            s = userInfoService.deletedUser(id);
            return Result.succResult(s);
        } catch (Exception e) {
            log.error("异常信息{}", e.getMessage());
            return Result.failResult(s);
        }
    }

    /**
     * 查询用户
     */
    @RequestMapping(value = "/user/{username}", method = RequestMethod.GET)
    @ResponseBody
    public String queryUser(@PathVariable("username") String username) {

        List<UserInfo> userInfos = userInfoService.queryUser(username);
        try {
            if (userInfos.size() != 0 && userInfos != null) {
                return Result.succResult(userInfos);
            }
        } catch (Exception e) {
            log.error("异常信息{}", e.getMessage());
            return Result.exceptionResult(e);
        }
        return Result.failResult("没有匹配的用户");
    }
}
