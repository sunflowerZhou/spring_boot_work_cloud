package com.zbb.api.config;

import com.zbb.bean.Result;
import com.zbb.exception.GlobalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

/**
 * 全局异常处理
 *
 * @author sunflower
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 系统异常处理
     *
     * @param e 异常
     * @return String
     */
    @ExceptionHandler(value = Exception.class)
    public String defaultErrorHandler(Exception e) {

        if (e instanceof GlobalException) {
            return Result.failResult(e.getMessage());
        }
        if (e instanceof MethodArgumentNotValidException) {
            return Result.failResult(((MethodArgumentNotValidException) e).getBindingResult().getAllErrors().get(0).getDefaultMessage());
        } else if (e instanceof SQLException || e instanceof DataIntegrityViolationException) {
            return Result.failResult("数据库异常");
        } else if (e instanceof IllegalArgumentException) {
            e.printStackTrace();
            return Result.failResult("非法数据类型异常");
        } else if (e instanceof BindException) {
            BindingResult bindingResult = ((BindException) e).getBindingResult();
            StringBuilder errorMesssage = new StringBuilder("Invalid Request:");
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                errorMesssage.append(fieldError.getField()).append(":").append(fieldError.getDefaultMessage()).append("/");
            }
            return Result.failResult(errorMesssage.toString());
        } else if (e instanceof HttpMessageNotReadableException) {
            return Result.failResult("HTTP请求JSON参数解析异常");
        } else {
            return Result.failResult("服务器异常");
        }
    }
}
