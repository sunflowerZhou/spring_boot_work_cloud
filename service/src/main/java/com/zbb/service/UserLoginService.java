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
 * @auther： hmq
 * @version： 1.0
 * @date 2022/5/2
 * @desc：
 **/
@Service
public class UserLoginService {

    @Resource
    private UserInfoMapper userInfoMapper;

    public Integer insertUser(UserInfo userInfo) {
        String password = Md5Util.md5(userInfo.getLoginPwd());
        userInfo.setLoginPwd(password);
        userInfo.setCreatTime(new Date());
        userInfo.setIsDeleted(1);
        synchronized (UserInfo.class){
            Example userInfoPo = new Example(UserInfo.class);
            userInfoPo.createCriteria().andEqualTo("mail", userInfo.getMail());
            int i = userInfoMapper.selectCountByExample(userInfoPo);
            if (i > 0){
                throw new BusinessException("当前账号已经注册过！");
            }
            return userInfoMapper.insertSelective(userInfo);
        }
    }

    public UserInfo queryUser(UserInfo userInfo){
        return userInfoMapper.selectOne(userInfo);
    }
}
