package com.zbb.service;

import com.zbb.common.util.encrypt.Md5Util;
import com.zbb.dao.mapper.UserInfoMapper;
import com.zbb.entity.UserInfo;
import com.zbb.exception.BusinessException;
import com.zbb.exception.GlobalException;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author hmq
 * @version： 1.0
 * @date 2022/5/2
 * @desc：
 **/
@Service
public class UserLoginService {

    @Resource
    private UserInfoMapper userInfoMapper;

    /**
     * 添加用户
     * */
    public Integer insertUser(String pwd,String mail) {
        String password = Md5Util.md5(pwd);
        UserInfo userInfo = new UserInfo();
        userInfo.setLoginPwd(password);
        userInfo.setMail(mail);
        userInfo.setCreatTime(new Date());
        userInfo.setIsDeleted(1);
        userInfo.setPermission(-1);
        userInfo.setAvatar("src/main/webapp/static/img/logo.png");
        synchronized (UserInfo.class){
            Example userInfoPo = new Example(UserInfo.class);
            userInfoPo.createCriteria().andEqualTo("mail", mail);
            int i = userInfoMapper.selectCountByExample(userInfoPo);
            if (i > 0){
                throw new BusinessException("当前账号已经注册过！");
            }
            return userInfoMapper.insertSelective(userInfo);
        }
        // 生成二维码
    }
    /**
     * 用户登录
     * */
    public UserInfo loginUser(String mail,String pwd){
        Example userInfoPo = new Example(UserInfo.class);
        userInfoPo.createCriteria().andEqualTo("mail",mail);
        userInfoPo.createCriteria().andEqualTo("login_pwd",Md5Util.md5(pwd));
        return userInfoMapper.selectOneByExample(userInfoPo);

    }

    /**
     * 查询用户
     * */
    public UserInfo queryUser(UserInfo userInfo){
        return userInfoMapper.selectOne(userInfo);
    }

    /**
     * 修改用户
     * */

    public String updateUser(UserInfo userInfo){
        Example userInfoPo = new Example(UserInfo.class);
        userInfoPo.createCriteria().andEqualTo("id",userInfo.getId());
        userInfo.setUpdateTime(new Date());
        synchronized (UserInfo.class){
            int i = userInfoMapper.updateByExample(userInfo, userInfoPo);
            if (i>0){
                return "修改成功";
            }
        }
        return "修改失败";
    }

}
