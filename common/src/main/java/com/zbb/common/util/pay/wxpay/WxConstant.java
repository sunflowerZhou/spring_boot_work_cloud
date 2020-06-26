package com.zbb.common.util.pay.wxpay;

/**
 * 功能描述:
 * 微信常量
 *
 * @author HHC
 * @date 2019/10/31
 * ————————————————————————
 */
public class WxConstant {
    /**
     * 业务返回值
     */

    public static final String SUCCESS = "SUCCESS";
    public static final String FAIL = "FAIL";
    public static final String ENCODING = "UTF-8";


    //微信请求值

    /**
     * 交易类型
     */
    public static final String NATIVE = "NATIVE";

    //微信返回值


    /**
     * 返回状态码 SUCCESS/FAIL
     */
    public static final String RETURN_CODE = "return_code";
    /**
     * 业务结果 SUCCESS/FAIL
     */

    public static final String RESULT_CODE = "result_code";
    /**
     * 返回信息 OK/其他失败信息
     */

    public static final String RETURN_MSG = "return_msg";
    /**
     * 错误代码
     */
    public static final String ERR_CODE = "err_code";
    /**
     * 订单金额
     */
    public static final String TOTAL_FEE = "total_fee";
    /**
     * 订单号
     */

    public static final String OUT_TRADE_NO = "out_trade_no";
    /**
     * 二维码链接
     */
    public static final String CODE_URL = "code_url";
}
