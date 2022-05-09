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
            Example userInfoPo = new Example(UserInfo.class);
            userInfoPo.createCriteria().andEqualTo("mail", mail);
            int i = userInfoMapper.selectCountByExample(userInfoPo);
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
        Example userInfoPo = new Example(UserInfo.class);
        userInfoPo.createCriteria().andEqualTo("mail", mail)
                .andEqualTo("loginPwd", Md5Util.md5(pwd))
                .andEqualTo("isDeleted", 1);
        return userInfoMapper.selectOneByExample(userInfoPo);

    }

    /**
     * 查询用户
     */
    public List<UserInfo> queryUser(String name) {
        Example example = new Example(UserInfo.class);
        example.createCriteria().andEqualTo("isDeleted", 1)
                //根据用户昵称查询(可能多个)
                .andEqualTo("loginName", name);
        List<UserInfo> userInfos = userInfoMapper.selectByExample(example);
        return userInfos;
    }

    /**
     * 修改用户
     */

    public String updateUser(UserInfoVo userInfoVo) {
        Example userInfoPo = new Example(UserInfo.class);
        userInfoPo.createCriteria().andEqualTo("id", userInfoVo.getId())
                .andEqualTo("isDeleted", 1);
        UserInfo userInfo1 = userInfoMapper.selectOneByExample(userInfoPo);
        userInfo1.setName(userInfoVo.getName());
        userInfo1.setAge(userInfoVo.getAge());
        userInfo1.setBirthday(userInfoVo.getBirthday());
        userInfo1.setSex(userInfoVo.getSex());
        userInfo1.setMail(userInfoVo.getMail());
        userInfo1.setTelephone(userInfoVo.getTelephone());
        userInfo1.setHobby(userInfoVo.getHobby());
        userInfo1.setLoginName(userInfoVo.getLoginName());
        userInfo1.setLoginPwd(Md5Util.md5(userInfoVo.getLoginPwd()));
        userInfo1.setSignature(userInfoVo.getSignature());
        userInfo1.setUpdateTime(new Date());
        if (userInfo1==null){
            return "修改失败,没有这个用户";
        }
        synchronized (this) {

            int i = userInfoMapper.updateByExample(userInfo1, userInfoPo);
            if (i > 0) {
                return "修改成功";
            }
        }
        return "修改失败,没有这个用户";
    }

    /**
     * 删除用户
     */
    public String deletedUser(Long id) {
        Example example = new Example(UserInfo.class);
        example.createCriteria().andEqualTo("id", id)
                .andEqualTo("isDeleted",1);
        UserInfo userInfo = userInfoMapper.selectOneByExample(example);
        synchronized (this) {

            if (userInfo == null) {
                return "删除失败,没有这个用户";
            }
            userInfo.setIsDeleted(0);
            userInfo.setUpdateTime(new Date());
            int i = userInfoMapper.updateByExample(userInfo, example);
            if (i > 0) {
                return "删除成功";
            }
        }
        return "删除失败,没有这个用户";
    }

}
