package com.zbb.api.web;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.zbb.bean.Result;
import com.zbb.entity.UserInfo;
import com.zbb.service.UserLoginService;
import com.zbb.service.ding.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

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

    @ResponseBody
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public String userLogin(@RequestBody UserInfo userInfo){
        try {

            UserInfo userInfo1 = userLoginService.queryUser(userInfo);
            if (userInfo1!=null){
                HttpSession session = request.getSession();
                session.setAttribute("userLogin",userInfo1);
                return Result.succResult("登录成功，用户名为>>>"+userInfo1.getLoginName());
            }

        } catch (Exception e) {
            log.error("异常信息{}",e.getMessage());
            return Result.exceptionResult(e);
        }
        return Result.failResult("登录失败");
    }

    @RequestMapping(value = "/register",method = RequestMethod.POST)
    @ResponseBody
    public String userInsert(@RequestBody UserInfo userInfo){
        try {
            Integer integer = userLoginService.insertUser(userInfo);
            if (integer>0){
                return Result.succResult("用户注册成功");
            }

        } catch (Exception e) {
            log.error("异常信息{}", e.getMessage());
            return Result.exceptionResult(e);
        }
        return Result.failResult("注册失败");
    }
}
