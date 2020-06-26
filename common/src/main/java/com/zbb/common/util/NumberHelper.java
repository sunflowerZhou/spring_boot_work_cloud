package com.zbb.common.util;

/**
 * @author sunflower
 */
public class NumberHelper {

    /**
     * 获取编号
     *
     * @param prefix 前缀
     * @param length 编号长度 不能超过13
     * @return
     */
    public synchronized static String getNumber(String prefix, int length) {
        if (length > 13) {
            return null;
        }
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String body = String.valueOf(System.currentTimeMillis());
        return new StringBuilder(prefix).append(body.substring(13 - length)).toString();
    }
}
