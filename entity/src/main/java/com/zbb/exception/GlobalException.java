package com.zbb.exception;


import com.zbb.bean.Result;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

/**
 * @author sunflower
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public class GlobalException extends RuntimeException {
    private static final long serialVersionUID = -5701182284190108797L;

    private String msg;

    public GlobalException(String msg) {
        this.msg = msg;
        Result.failResult(msg);
    }
}
