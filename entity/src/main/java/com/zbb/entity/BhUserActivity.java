package com.zbb.entity;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author sunflower
 */
@Data
@Table(name = "bh_user_activity_t")
public class BhUserActivity implements Serializable {

    @Id
    private Long id;

    private String activityName;

    private String activityUrl;

    private Date lastTime;

    private String activityDescribe;

    private String activityLogo;

    private Date activityTime;

    private Date createTime;

    private Long userId;

    private Integer activityType;

    private static final long serialVersionUID = 1L;

}