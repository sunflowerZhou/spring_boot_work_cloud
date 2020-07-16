package com.zbb.vo;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

/**
 * @author sunflower
 */
@Data
public class InviteListVo {

    private List<ObjectInviteTimeDataVo> objs = Lists.newArrayList();

    private Integer total;

    private String typeName;
}
