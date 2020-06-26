package com.zbb.common.util.qrcode;

import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * 功能描述:
 * 二维码生成工具类
 *
 * @author sunflower
 * @date 2020/6/25
 * ————————————————————————
 */
public class QrCode extends QrCodeUtil {
    private static BASE64Decoder decoder = new sun.misc.BASE64Decoder();
    //private static String content = "https://www.zbbledger.com/";

    /**
     * 生成样式一
     */
    private static BufferedImage generateQrCodeV1(String content) {
        return QrCodeUtil.generate(content,
                30, 30);
    }

    /**
     * 生成样式二
     */
    private static BufferedImage generateQrCodeV2(String content) {

        return generate(content, generateQrConfigs());
    }

    /**
     * 生成样式三
     */
    private static BufferedImage generateQrCodeV3(String content) {

        return generate(content, generateQrConfig());
    }

    /**
     * 生成 BASE64Encoder 编码
     *
     * @param content,type 网址 生成类型
     * @return java.lang.String
     */
    public static byte[] getBufferedImage(String content, String type) {
        BufferedImage bufferedImage;
        if ("1".equals(type)) {
            //样式一
            bufferedImage = generateQrCodeV1(content);
        } else if ("2".equals(type)) {
            //样式二
            bufferedImage = generateQrCodeV2(content);
        } else {
            //样式三
            bufferedImage = generateQrCodeV3(content);
        }
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        // 写图片
        try {
            ImageIO.write(bufferedImage, "png", bao);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bao.toByteArray();

    }

    /**
     * QrConfig
     *
     * @return cn.hutool.extra.qrcode.QrConfig
     */
    private static QrConfig generateQrConfigs() {
        QrConfig config = new QrConfig(30, 30);
        // 设置边距，既二维码和背景之间的边距
        config.setMargin(3);
        // 设置前景色，既二维码颜色（青色）
        config.setForeColor(Color.CYAN.getRGB());
        // 设置背景色（灰色）
        config.setBackColor(Color.GRAY.getRGB());
        return config;
    }

    /**
     * QrConfig
     *
     * @return cn.hutool.extra.qrcode.QrConfig
     */
    private static QrConfig generateQrConfig() {
        QrConfig config = new QrConfig(30, 30);
        config.setImg(new File("f://hhc.jpg"));
        return config;
    }

    /**
     * 解码并且生成二维码
     *
     * @param base64String s
     */
    private static void base64StringToImage(String base64String) {
        try {
            byte[] bytes1 = decoder.decodeBuffer(base64String);

            ByteArrayInputStream bais = new ByteArrayInputStream(bytes1);
            BufferedImage bi1 = ImageIO.read(bais);
            // 可以是jpg,png,gif格式
            File w2 = new File("f://hhc3.jpg");
            // 不管输出什么格式图片，此处不需改动
            ImageIO.write(bi1, "jpg", w2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
       /* String content = "https://www.zbbledger.com/";
        String s = getBufferedImage(content, "3");
        System.out.println(s);
        base64StringToImage(s);*/
    }
}
