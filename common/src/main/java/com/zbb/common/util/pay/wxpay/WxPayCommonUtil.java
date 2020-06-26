package com.zbb.common.util.pay.wxpay;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.*;

/**
 * 功能描述:
 *
 * @author HHC
 * @date 2019/10/31
 * ————————————————————————
 * 微信支付工具包
 */
@Slf4j
public class WxPayCommonUtil {

    private static final String SYMBOLS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final Random RANDOM = new SecureRandom();

    /**
     * 基础参数
     *
     * @param params void
     */
    public static void commonParams(SortedMap<Object, Object> params) {
        // 账号信息
        // appid
        String appId = WxConfigUtil.APP_ID;
        // 商户号
        String mchId = WxConfigUtil.MCH_ID;
        // 生成随机字符串
        String nonceStr = geneNonceStr();
        // 公众账号ID
        params.put("appid", appId);
        // 商户号
        params.put("mch_id", mchId);
        // 随机字符串
        params.put("nonce_str", nonceStr);
    }

    /**
     * 业务参数
     *
     * @param packageParams void
     * @param o             业务实体
     */
    public static void businessParams(SortedMap<Object, Object> packageParams, Object o) {
        // 秘钥
        String key = WxConfigUtil.API_KEY;
        // 交易类型 原生扫码支付
        String tradeType = WxConstant.NATIVE;
        // 商品ID
        packageParams.put("product_id", "");
        // 商品描述
        packageParams.put("body", "");
        // 商户订单号
        packageParams.put("out_trade_no", "");
        String totalFee = "";
        totalFee = WxPayCommonUtil.subZeroAndDot(totalFee);
        // 总金额
        packageParams.put("total_fee", totalFee);
        // 发起人IP地址
        packageParams.put("spbill_create_ip", "");
        // 回调地址
        packageParams.put("notify_url", "");
        // 交易类型
        packageParams.put("trade_type", tradeType);
        String sign = WxPayCommonUtil.createSign(packageParams, key);
        // 签名
        packageParams.put("sign", sign);
    }


    /**
     * 获取随机字符串 Nonce_Str
     *
     * @return String 随机字符串
     */
    private static String geneNonceStr() {
        char[] nonceChars = new char[32];
        for (int index = 0; index < nonceChars.length; ++index) {
            nonceChars[index] = SYMBOLS.charAt(RANDOM.nextInt(SYMBOLS.length()));
        }
        return new String(nonceChars);
    }


    /**
     * 生成签名
     *
     * @param packageParams 待签名数据
     * @param apiKey        API_KEY
     * @return 签名
     */
    private static String createSign(SortedMap<Object, Object> packageParams, String apiKey) {
        StringBuffer sb = new StringBuffer();
        Iterator<Map.Entry<Object, Object>> iterator = packageParams.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Object, Object> obj = iterator.next();
            String k = (String) obj.getKey();
            String v = (String) obj.getValue();
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + apiKey);
        return md5(sb.toString()).toUpperCase();
    }

    /**
     * 校验签名 签名是否正确,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。
     *
     * @param packageParams MAP
     * @param apiKey        API_KEY
     * @return boolean
     */
    @SuppressWarnings({"rawtypes"})
    public static boolean isTenpaySign(SortedMap<Object, Object> packageParams, String apiKey) {
        StringBuffer sb = new StringBuffer();
        Iterator<Map.Entry<Object, Object>> iterator = packageParams.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Object, Object> obj = iterator.next();
            String k = (String) obj.getKey();
            String v = (String) obj.getValue();
            if (!"sign".equals(k) && null != v && !"".equals(v)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + apiKey);
        //算出摘要
        String mysign = md5(sb.toString()).toLowerCase();
        String tenpaySign = ((String) packageParams.get("sign")).toLowerCase();
        return tenpaySign.equals(mysign);
    }

    /**
     * 生成 MD5
     *
     * @param data 待处理数据
     * @return MD5结果
     */
    private static String md5(String data) {
        try {
            java.security.MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(data.getBytes(WxConstant.ENCODING));
            return getStringBuilder(array);
        } catch (Exception e) {
            System.out.println("生成MD5失败>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        }
        return "";
    }

    /**
     * 生成 HMACSHA256
     *
     * @param data 待处理数据
     * @param key  密钥
     * @return 加密结果
     * @throws Exception e
     */
    private static String hmacSha256(String data, String key) {
        try {
            Mac sha256Hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(WxConstant.ENCODING), "HmacSHA256");
            sha256Hmac.init(secretKey);
            byte[] array = sha256Hmac.doFinal(data.getBytes(WxConstant.ENCODING));
            return getStringBuilder(array);
        } catch (Exception e) {
            System.out.println("生成HMACSHA256失败>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        }
        return "";
    }

    private static String getStringBuilder(byte[] array) {
        StringBuilder sb = new StringBuilder();
        for (byte item : array) {
            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1));
        }
        return sb.toString();

    }

    /**
     * 请求参数转化为xml格式字符串
     *
     * @param parameters param
     * @return java.lang.String
     */
    public static String getRequestXml(SortedMap<Object, Object> parameters) {
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        Iterator<Map.Entry<Object, Object>> iterator = parameters.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Object, Object> obj = iterator.next();
            String k = (String) obj.getKey();
            String v = (String) obj.getValue();
            if ("attach".equalsIgnoreCase(k) || "body".equalsIgnoreCase(k) || "sign".equalsIgnoreCase(k)) {
                sb.append("<" + k + ">" + "<![CDATA[" + v + "]]></" + k + ">");
            } else {
                sb.append("<" + k + ">" + v + "</" + k + ">");
            }
        }
        sb.append("</xml>");
        return sb.toString();
    }

    /**
     * 使用java正则表达式去掉多余的.与0
     *
     * @param s S
     * @return string
     */
    private static String subZeroAndDot(String s) {
        if (s.indexOf(".") > 0) {
            // 去掉多余的0
            s = s.replaceAll("0+?$", "");
            // 如最后一位是.则去掉
            s = s.replaceAll("[.]$", "");
        }
        return s;
    }

}
