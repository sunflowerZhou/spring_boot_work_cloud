package com.zbb.bean;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author sunflower
 */
@Data
public class PageDataBo implements Serializable {

    private static final long serialVersionUID = 3502203227787353677L;

    @NotNull(message = "当前页参数不能为空")
    private String currentPage;

    @NotNull(message = "页码参数不能为空")
    private String showCount;
}