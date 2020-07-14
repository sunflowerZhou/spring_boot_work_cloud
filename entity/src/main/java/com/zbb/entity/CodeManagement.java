package com.zbb.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author sunflower
 */
@Data
@ApiModel(value = "二维码模版配置")
@Table(name = "bh_code_management_t")
public class CodeManagement implements Serializable {

    @Id
    @ApiModelProperty(name = "ID")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty(name = "二维码名称")
    private String codeName;

    @ApiModelProperty(name = "二维码类型")
    private Integer codeType;

    @ApiModelProperty(name = "关联群聊ID")
    private String groupChatId;

    @ApiModelProperty(name = "是否自动建群{0:自动建群}{1:不自动建群}")
    private Integer autoGroupBuild;

    @ApiModelProperty(name = "群名称")
    private String groupName;

    @ApiModelProperty(name = "群名称序号")
    private Integer groupNo;

    @ApiModelProperty(name = "群主名称")
    private String groupMain;

    @ApiModelProperty(name = "群主id")
    private String groupMainId;

    @ApiModelProperty(name = "管理员名称")
    private String groupAdminName;

    @ApiModelProperty(name = "管理员id")
    private String groupAdminId;

    @ApiModelProperty(name = "生效时间")
    private Date startTime;

    @ApiModelProperty(name = "创建时间")
    private Date createTime;

    @ApiModelProperty(name = "创建人")
    private String createBy;

    @ApiModelProperty(name = "修改时间")
    private Date updateTime;

    @ApiModelProperty(name = "修改人")
    private String updateBy;

    @ApiModelProperty(name = "二维码地址")
    private String codeUrl;

    @ApiModelProperty(name = "二维码状态{0:启用}{1:禁用}{2:已失效}")
    private Integer codeState;

    @ApiModelProperty(name = "规则名称")
    private String ruleName;

    private static final long serialVersionUID = 1L;
}