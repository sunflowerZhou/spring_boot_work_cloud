package com.zbb.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author sunflower
 */
@Data
public class AccessTokenVo {

    @ApiModelProperty(name = "用户id")
    private String userId;

    @ApiModelProperty(name = "是否是管理员，true：是，false：不是")
    private boolean isSys;

    @ApiModelProperty(name = "级别，1：主管理员，2：子管理员，100：老板，0：其他（如普通员工）")
    private String sysLevel;
}
