package com.zbb.api.web;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zbb.bean.Result;
import com.zbb.entity.UserInfo;
import com.zbb.exception.GlobalException;
import com.zbb.service.UserLoginService;
import com.zbb.service.ding.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 * @auther： hmq
 * @version： 1.0
 * @date 2022/5/2
 * @desc：
 **/
@Controller
@RequestMapping(value = "/user")
public class UserLoginController {
    private static final Log log = LogFactory.get();

    @Resource
    private UserLoginService userLoginService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;

    /**
     * 用户登录
     */
    @ResponseBody
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String userLogin(@RequestParam("mail") String mail, @RequestParam("userpwd") String pwd) {
        try {

            UserInfo userInfo = userLoginService.loginUser(mail, pwd);
            if (userInfo != null) {
                String s = JSONObject.toJSONString(userInfo);
                Cookie cookie = new Cookie("userLogin", s);
                response.addCookie(cookie);

                //登录成功 跳转到首页
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
            Integer integer = userLoginService.insertUser(mail, pwd);
            if (integer > 0) {
                //注册成功 跳转到登录页面
                return Result.succResult("注册成功");
            }

        } catch (Exception e) {
            log.error("异常信息{}", e.getMessage());
            return Result.exceptionResult(e);
        }
        return Result.failResult("注册失败");
    }

    /**
     * 用户修改
     */
    public String userUpdate(UserInfo userInfo) {

        String s = "";
        try {

            s = userLoginService.updateUser(userInfo);

            return Result.failResult(s);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failResult(s + e.getMessage());
        }
    }
}
