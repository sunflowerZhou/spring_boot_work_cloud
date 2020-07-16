package com.zbb.common.util.qrcode;

import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import com.google.common.collect.Maps;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.zbb.common.util.file.FileUtil;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * 功能描述:
 * 二维码生成工具类
 *
 * @author sunflower
 * @date 2020/6/25
 * ————————————————————————
 */
public class QrCode extends QrCodeUtil {
    private static final BASE64Decoder DECODER = new sun.misc.BASE64Decoder();

    private static final ErrorCorrectionLevel E_RATE = ErrorCorrectionLevel.L;

    private static final String CHARSET = "utf-8";

    /**
     * img 目录下二维码包
     */
    public static final String CODE_URL = "/img/qr_code/";

    /**
     * /static
     */
    private static final String URL_STATIC = "/static";
    /**
     * 二维码宽
     */
    private static final int CODE_WIDTH = 200;
    /**
     * 二维码高
     */
    private static final int CODE_HEIGHT = 200;

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
        String s = "1";
        if (s.equals(type)) {
            //样式一
            bufferedImage = generateQrCodeV1(content);
        } else {
            String s1 = "2";
            if (s1.equals(type)) {
                //样式二
                bufferedImage = generateQrCodeV2(content);
            } else {
                //样式三
                bufferedImage = generateQrCodeV3(content);
            }
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
     * 创建二维码
     *
     * @param content      文字内容
     * @param qrCodeWidth  二维码宽
     * @param qrCodeHeight 二维码高
     * @return BufferedImage
     * @throws Exception Exception
     */
    public static BufferedImage createImage(String content,
                                            int qrCodeWidth, int qrCodeHeight) throws Exception {
        Map<EncodeHintType, Object> hints = Maps.newConcurrentMap();
        hints.put(EncodeHintType.ERROR_CORRECTION, E_RATE);
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content,
                BarcodeFormat.QR_CODE, qrCodeWidth, qrCodeHeight, hints);
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000
                        : 0xFFFFFFFF);
            }
        }
        return image;
    }

    /**
     * 获取二维码地址
     *
     * @param content 二维码内容
     * @param codeName ip + 端口
     * @param imgName 图片名称
     * @return 二维码访问路径
     * @throws Exception Exception
     */
    public static String generateImg(String content,String codeName, String imgName) throws Exception {
        // 项目路径
        String jarPath = FileUtil.getJarPath1() + CODE_URL;
        File file = new File(jarPath + imgName + ".png");
        if (!file.exists()) {
            boolean mkdir = file.mkdirs();
            System.out.println("文件夹创建 ：" + mkdir);
        }
        BufferedImage image = createImage(content, CODE_WIDTH, CODE_HEIGHT);
        ImageIO.write(image, "png", file);
        image.flush();
        System.out.println(FileUtil.getJarPath());
        System.out.println("产生的二维码图片:" + file.getPath());
        return codeName + CODE_URL + imgName + ".png";
    }

    public static void deleteImg(String url)throws Exception{
        String[] split = url.split(CODE_URL);
        String s = split[split.length - 1];
        File file = new File(FileUtil.getJarPath1() + CODE_URL +s);
        boolean delete = file.delete();
        if (delete){
            System.out.println("图片删除 ：" + delete);
        }
    }


    /**
     * 解码并且生成二维码
     *
     * @param base64String s
     */
    private static void base64StringToImage(String base64String) {
        try {
            byte[] bytes1 = DECODER.decodeBuffer(base64String);

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


    public static void main(String[] args) throws Exception {
        deleteImg("工牌.jpg");
    }
}
