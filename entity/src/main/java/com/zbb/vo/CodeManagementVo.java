package com.zbb.vo;

import com.google.common.collect.Lists;
import com.zbb.entity.BhGroup;
import com.zbb.entity.CodeManagement;
import lombok.Data;

import java.util.List;

/**
 * @author zhaohui
 */
@Data
public class CodeManagementVo extends CodeManagement {

    private List<BhGroup> bhGroups = Lists.newArrayList();
}
