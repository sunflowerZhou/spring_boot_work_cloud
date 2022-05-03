package com.zbb.service;

import com.zbb.dao.mapper.UserInfoMapper;
import com.zbb.entity.UserInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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

    public Integer insertUser(UserInfo userInfo){
        return userInfoMapper.insert(userInfo);
    }

    public UserInfo queryUser(UserInfo userInfo){
        return userInfoMapper.selectOne(userInfo);
    }
}
