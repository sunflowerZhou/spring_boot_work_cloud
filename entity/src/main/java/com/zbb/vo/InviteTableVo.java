package com.zbb.vo;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

/**
 * @author sunflower
 */
@Data
public class InviteTableVo {

    private List<InviteListVo> data = Lists.newArrayList();
}
