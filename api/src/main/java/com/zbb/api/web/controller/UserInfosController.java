package com.zbb.api.web.controller;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zbb.bean.Result;
import com.zbb.entity.UserInfo;
import com.zbb.service.UserInfosService;
import com.zbb.vo.UserInfoVo;
import org.apache.commons.codec.language.bm.Lang;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
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
public class UserInfosController {
    private static final Log log = LogFactory.get();

    @Resource
    private UserInfosService userInfosService;
    @Resource
    private HttpServletResponse response;

    /**
     * 用户登录
     */
    @ResponseBody
    @RequestMapping(value = "/user/{mail}/{userpwd}", method = RequestMethod.GET)
    public String userLogin(@PathVariable("mail") String mail, @PathVariable("userpwd") String pwd) {
        try {
            UserInfo userInfo = userInfosService.loginUser(mail, pwd);

            if (userInfo != null) {
                Cookie cookie = new Cookie("userId", userInfo.getId().toString());
                Cookie cookie1 = new Cookie("userMail", userInfo.getMail());
                response.addCookie(cookie);
                response.addCookie(cookie1);
                return Result.succResult(userInfo);
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
    @RequestMapping(value = "/user", method = RequestMethod.POST)
    @ResponseBody
    public String userInsert(@RequestParam("mail") String mail, @RequestParam("userpwd") String pwd) {
        try {
            Integer integer = userInfosService.insertUser(mail, pwd);
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
    public String userUpdate(@RequestBody JSONObject jsonObject) {
        log.info("strJSON{}",jsonObject);
        UserInfoVo userInfoVo = JSONObject.toJavaObject(jsonObject, UserInfoVo.class);

        log.info("jsonUSER{}",userInfoVo.toString());
        String s = "";
        try {

            s = userInfosService.updateUser(userInfoVo);

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
    public String deletedUser(@PathVariable("id") Long id) {
        String s = "";
        try {
            s = userInfosService.deletedUser(id);
            return Result.succResult(s);
        } catch (Exception e) {
            e.printStackTrace();
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

        List<UserInfo> userInfos = userInfosService.queryUser(username);
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
