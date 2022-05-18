package com.zbb.service;

import com.zbb.common.util.encrypt.Md5Util;
import com.zbb.dao.mapper.UserInfoMapper;
import com.zbb.entity.UserInfo;
import com.zbb.exception.BusinessException;
import com.zbb.vo.UserInfoVo;
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
     */
    public Integer insertUser(String mail, String pwd) {
        String password = Md5Util.md5(pwd);
        UserInfo userInfo = new UserInfo();
        userInfo.setLoginPwd(password);
        userInfo.setMail(mail);
        userInfo.setCreatTime(new Date());
        userInfo.setIsDeleted(1);//1 不删除
        userInfo.setPermission(-1);
        userInfo.setAvatar("src/main/webapp/static/img/logo.png");
        synchronized (UserInfo.class) {
            /*Example userInfoPo = new Example(UserInfo.class);
            userInfoPo.createCriteria().andEqualTo("mail", mail);
            int i = userInfoMapper.selectCountByExample(userInfoPo);*/
            int i = userInfoMapper.selectCount(new UserInfo().setMail(mail));
            if (i > 0) {
                throw new BusinessException("当前账号已经注册过！");
            }
            return userInfoMapper.insertSelective(userInfo);
        }
        // 生成二维码
    }

    /**
     * 用户登录
     */
    public UserInfo loginUser(String mail, String pwd) {
        /*Example userInfoPo = new Example(UserInfo.class);
        userInfoPo.createCriteria().andEqualTo("mail", mail)
                .andEqualTo("loginPwd", Md5Util.md5(pwd))
                .andEqualTo("isDeleted", 1);*/
        return userInfoMapper.selectOne(new UserInfo().setMail(mail).setLoginPwd(pwd).setIsDeleted(1));

    }

    /**
     * 查询用户
     */
    public List<UserInfo> queryUser(String name) {
        /*Example example = new Example(UserInfo.class);
        example.createCriteria().andEqualTo("isDeleted", 1)
                //根据用户昵称查询(可能多个)
                .andEqualTo("loginName", name);
        List<UserInfo> userInfos = userInfoMapper.selectByExample(example);*/
        List<UserInfo> userInfoList = userInfoMapper.select(new UserInfo().setLoginName(name).setIsDeleted(1));
        return userInfoList;
    }

    /**
     * 修改用户
     */

    public String updateUser(UserInfoVo userInfoVo) {
        /*Example userInfoPo = new Example(UserInfo.class);
        userInfoPo.createCriteria().andEqualTo("id", userInfoVo.getId())
                .andEqualTo("isDeleted", 1);*/
        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(userInfoVo.getId());
        if (userInfo==null||0 == userInfo.getIsDeleted()){
            return "F";
        }
        if (userInfoVo.getName() != null)
        userInfo.setName(userInfoVo.getName());
        if (userInfoVo.getAge() != null)
        userInfo.setAge(userInfoVo.getAge());
        if (userInfoVo.getBirthday() != null)
        userInfo.setBirthday(userInfoVo.getBirthday());
        if (userInfoVo.getSex() != null)
        userInfo.setSex(userInfoVo.getSex());
        if (userInfoVo.getTelephone() != null)
        userInfo.setTelephone(userInfoVo.getTelephone());
        if (userInfoVo.getHobby() != null)
        userInfo.setHobby(userInfoVo.getHobby());
        if (userInfoVo.getLoginName() != null)
        userInfo.setLoginName(userInfoVo.getLoginName());
        if (userInfoVo.getSignature() != null)
        userInfo.setSignature(userInfoVo.getSignature());
        userInfo.setUpdateTime(new Date());
        synchronized (this) {
            int i = userInfoMapper.updateByPrimaryKeySelective(userInfo);
            if (i > 0) {
                return "T";
            }
        }
        return "F";
    }

    /**
     * 删除用户
     */
    public String deletedUser(Long id) {
        /*Example example = new Example(UserInfo.class);
        example.createCriteria().andEqualTo("id", id)
                .andEqualTo("isDeleted",1);*/
        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(id);
        synchronized (this) {

            if (userInfo == null || userInfo.getIsDeleted() == 0) {
                return "F";
            }
            int i = userInfoMapper.updateByPrimaryKeySelective(new UserInfo().setId(id).setIsDeleted(0).setUpdateTime(new Date()));
            if (i > 0) {
                return "T";
            }
        }
        return "F";
    }

}
