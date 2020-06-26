package com.zbb.api.interceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author sunflower
 * @className IngoreAuth
 * @description TODO
 * @date 2019/9/25 15:04
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface IngoreAuth {
}
