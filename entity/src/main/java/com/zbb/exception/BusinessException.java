package com.zbb.exception;


/**
 * Function: 业务异常<br/>
 * date: 2018年7月11日 下午2:36:12 <br/>
 *
 * @author sunflower
 * @since JDK 1.8
 */
public class BusinessException extends GlobalException {
    private static final long serialVersionUID = 6021390821349937519L;

    public BusinessException(String msg) {
        super(msg);
    }

}
