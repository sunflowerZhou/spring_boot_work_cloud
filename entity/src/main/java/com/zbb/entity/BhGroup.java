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
@Table(name = "bh_group_t")
@ApiModel(value = "群聊信息")
public class BhGroup implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty("钉钉群id")
    private Long groupId;

    @ApiModelProperty("钉钉群名称")
    private String groupName;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("创建人")
    private String createBy;

    @ApiModelProperty("群主名称")
    private String groupMainName;

    @ApiModelProperty("群主id")
    private Long groupMainId;

    @ApiModelProperty("当前群人数")
    private Integer groupNum;

    @ApiModelProperty("最大群容量")
    private Integer groupMaxNum;

    @ApiModelProperty("本地群id")
    private Long bhGroupId;

    private static final long serialVersionUID = 1L;

}