package com.zbb.vo;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @auther： hmq
 * @version： 1.0
 * @date 2022/5/9
 * @desc：
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class UserInfoVo implements Serializable {

    private Long id;

    private String name;

    private Integer age;

    private Date birthday;

    private Integer sex;

    private String mail;

    private String telephone;

    private String hobby;

    private String loginName;

    private String loginPwd;

    private String signature;




    private static final long serialVersionUID = 1L;
}
