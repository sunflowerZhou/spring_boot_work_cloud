package com.zbb.bo;

import com.zbb.dto.GroupInfoDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author sunflower
 */
@Data
public class CodeManagementBo implements Serializable {

    @ApiModelProperty(name = "ID")
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

    @ApiModelProperty(name = "二维码状态{0:启用}{1:禁用}{2:已失效}")
    private Integer codeState;

    @ApiModelProperty(name = "关联群")
    private List<GroupInfoDto> groupInfoDto;

    private String codeUrl;

    @ApiModelProperty(name = "规则名称")
    private String ruleName;

    private static final long serialVersionUID = 1L;
}