package com.zbb.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zhaohui
 */
@Data
public class PermanentAuthCode implements Serializable {

    @ApiModelProperty("corp_name")
    private String corpName;

    @ApiModelProperty("corpid")
    private String corpId;

    @ApiModelProperty("permanent_code")
    private String permanentCode;
}
