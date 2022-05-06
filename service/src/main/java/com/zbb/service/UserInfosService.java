package com.zbb.service;

import com.zbb.common.util.encrypt.Md5Util;
import com.zbb.dao.mapper.UserInfoMapper;
import com.zbb.entity.UserInfo;
import com.zbb.exception.BusinessException;
import com.zbb.exception.GlobalException;
import org.apache.commons.codec.language.bm.Lang;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author hmq
 * @version： 1.0
 * @date 2022/5/2
 * @desc：
 **/
@Service
public class UserInfosService {

    @Resource
    private UserInfoMapper userInfoMapper;

    /**
     * 添加用户
     * */
    public Integer insertUser(String mail,String pwd) {
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
        userInfoPo.createCriteria().andEqualTo("mail",mail)
                .andEqualTo("loginPwd",Md5Util.md5(pwd))
                .andEqualTo("isDeleted",1);
        return userInfoMapper.selectOneByExample(userInfoPo);

    }

    /**
     * 查询用户
     * */
    public List<UserInfo> queryUser(String name){
        Example example = new Example(UserInfo.class);
        example.createCriteria().andEqualTo("isDeleted",1)
        //根据用户昵称查询(可能多个)
        .andEqualTo("loginName",name);
        List<UserInfo> userInfos = userInfoMapper.selectByExample(example);
        return userInfos;
    }

    /**
     * 修改用户
     * */

    public String updateUser(UserInfo userInfo){
        Example userInfoPo = new Example(UserInfo.class);
        userInfoPo.createCriteria().andEqualTo("id",userInfo.getId())
                .andEqualTo("isDeleted",1);
        userInfo.setUpdateTime(new Date());
        synchronized (this){
            int i = userInfoMapper.updateByExample(userInfo, userInfoPo);
            if (i>0){
                return "修改成功";
            }
        }
        return "修改失败";
    }

    /**
     * 删除用户
     * */
    public String deletedUser(Long id){
        synchronized (this){
            Example example = new Example(UserInfo.class);
            example.createCriteria().andEqualTo("id",id);
            UserInfo userInfo = new UserInfo();
            userInfo.setIsDeleted(0);
            userInfo.setUpdateTime(new Date());
            int i = userInfoMapper.updateByExample(userInfo, example);
            if (i>0){
                return "删除成功";
            }
        }
        return "删除失败";
    }

}
