package com.zbb.entity;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name = "user_info")
@ToString
@Accessors(chain = true)
@Data
public class UserInfo implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
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

    private Integer isDeleted;

    private String adminRecord;

    private Integer permission;

    private String signature;

    private String avatar;

    private String qrcode;

    private Date creatTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;


}