package com.zbb.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author sunflower
 *
 * 企业凭证
 */
@Data
public class EnterpriseAuthVo {

    @ApiModelProperty(name = "授权方")
    private String accessToken;

    @ApiModelProperty(name = "有效时间，单位秒")
    private Long expiresIn;
}
