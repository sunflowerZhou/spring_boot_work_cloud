package com.zbb.common.util.file;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.ClassUtils;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author sunflower
 * @className FileUtil
 * @description 文件夹
 * @date 2020/7/6 17:19
 */
@Slf4j
public class FileUtil {
    private static final BASE64Decoder DECODER = new BASE64Decoder();

    public static String getMineType(String fileName) {
        String minType = StringUtils.EMPTY;
        if (fileName.endsWith(".tif")) {
            minType = "image/tiff";
        } else if (fileName.endsWith(".fax")) {
            minType = "image/fax";
        } else if (fileName.endsWith(".gif")) {
            minType = "image/gif";
        } else if (fileName.endsWith(".ico")) {
            minType = "image/x-icon";
        } else if (fileName.endsWith(".jfif")) {
            minType = "image/jpeg";
        } else if (fileName.endsWith(".jpe")) {
            minType = "image/jpeg";
        } else if (fileName.endsWith(".jpeg")) {
            minType = "image/jpeg";
        } else if (fileName.endsWith(".jpg")) {
            minType = "image/jpeg";
        } else if (fileName.endsWith(".net")) {
            minType = "image/pnetvue";
        } else if (fileName.endsWith(".png")) {
            minType = "image/png";
        } else if (fileName.endsWith(".rp")) {
            minType = "image/vnd.rn-realpix";
        } else if (fileName.endsWith(".tif")) {
            minType = "image/tiff";
        } else if (fileName.endsWith(".tiff")) {
            minType = "image/tiff";
        } else if (fileName.endsWith(".wbmp")) {
            minType = "image/vnd.wap.wbmp";
        } else if (fileName.endsWith(".pdf")) {
            minType = "application/pdf";
        } else if (fileName.endsWith(".doc") || fileName.endsWith(".docx")) {
            minType = "application/msword";
        } else if (fileName.endsWith(".doc") || fileName.endsWith(".docx")) {
            minType = "application/msword";
        } else if (fileName.endsWith(".mp4")) {
            minType = "audio/mp4";
        } else if (fileName.endsWith(".mp3")) {
            minType = "audio/mp3";
        } else if (fileName.endsWith(".wav")) {
            minType = "audio/wav";
        } else if (fileName.endsWith(".wma")) {
            minType = "audio/x-ms-wma";
        } else if (fileName.endsWith(".rmvb")) {
            minType = "application/vnd.rn-realmedia-vbr";
        } else if (fileName.endsWith(".txt") || fileName.endsWith(".log") || fileName.endsWith(".out")) {
            minType = "text/plain";
        } else if (fileName.endsWith(".go") || fileName.endsWith(".java")) {
            minType = "text/plain";
        }
        return minType;
    }

    /**
     * 文件上传返回base64编码后字符串
     *
     * @param file 文件
     * @return java.lang.String
     */
    public static String upload(MultipartFile file) {
        String base64Picture = "";
        try {
            byte[] bytes = file.getBytes();
            base64Picture = new BASE64Encoder().encodeBuffer(bytes).trim();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return base64Picture;
    }

    /**
     * 解码并且生成图片
     *
     * @param base64String s
     */
    public static Integer base64StringToImage(String base64String) {
        int result = 0;
        try {
            byte[] bytes1 = DECODER.decodeBuffer(base64String);
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes1);
            BufferedImage bi1 = ImageIO.read(bais);
            // 可以是jpg,png,gif格式
            File w2 = new File("f://filehhc.jpg");
            // 不管输出什么格式图片，此处不需改动
            ImageIO.write(bi1, "jpg", w2);
        } catch (IOException e) {
            result = 1;
            e.printStackTrace();
        }
        return result;
    }

    public static String getJarPath() throws Exception {
        String jarPath = URLDecoder.decode(
                Objects.requireNonNull(
                        Objects.requireNonNull(ClassUtils.getDefaultClassLoader()).getResource("")).getPath(), "utf-8");
        String colon = ":";
        if (jarPath.indexOf(colon) > 0) {
            jarPath = jarPath.replaceFirst("file:", "");
            if (jarPath.indexOf(colon) > 0) {
                //windows盘
                jarPath = jarPath.replaceFirst("/", "");
            }
        }
        //去除classes路径
        int indexOfExclamationMark = jarPath.indexOf('!') == -1 ? jarPath.length() : jarPath.indexOf('!');
        //去除"!"
        jarPath = jarPath.substring(0, indexOfExclamationMark);
        //使用jar同级目录
        jarPath = jarPath.substring(0, jarPath.lastIndexOf("/"));
        return jarPath;
    }

    public static String getJarPath1() throws Exception {
        String jarPath = getJarPath();
        return jarPath.replace("/api/target/classes","");
    }

    public static void compressToZip(String sourceFilePath, String zipFilePath, String zipFilename, List<String> needCompressedFileNameList, boolean isDeleteFile) {
        File sourceFile = new File(sourceFilePath);
        File zipPath = new File(zipFilePath);
        if (!zipPath.exists()) {
            boolean mkdirs = zipPath.mkdirs();
            System.out.println(mkdirs);
        }
        File zipFile = new File(zipPath + File.separator + zipFilename);
        try {
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile));
            for (String fileName : needCompressedFileNameList) {
                writeZip(new File(sourceFile.getPath() + File.separator + fileName + ".png"), zos);
            }
            zos.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e.getCause());
        } finally {
            //文件压缩完成后，删除被压缩文件
            if (isDeleteFile) {
                long p = System.currentTimeMillis();
                boolean flag = deleteDirByCmd(sourceFile);
                long n = System.currentTimeMillis();
                System.out.println("删除耗时" + (n - p) + "ms");
                System.out.println("删除被压缩文件[" + sourceFile + "]标志：" + flag);
            }
        }
    }

    /**
     * 压缩
     *
     * @param sourceFile 源文件目录
     * @param zos        文件流
     */
    public static void writeZip(File sourceFile, ZipOutputStream zos) {
        //文件
        try {
            //指定zip文件夹
            ZipEntry zipEntry = new ZipEntry(sourceFile.getName());
            zos.putNextEntry(zipEntry);
            // copy文件到zip输出流中
            Files.copy(sourceFile.toPath(), zos);
            zos.closeEntry();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
    }

    /**
     * 删除文件夹
     *
     * @param dir 文件
     * @return 是否成功
     */

    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < Objects.requireNonNull(children).length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        //删除空文件夹
        return dir.delete();
    }

    public static boolean deleteDirByCmd(File dir) {
        String os = System.getProperty("os.name");


        if (dir == null) {
            return true;
        }
        if (!dir.exists()) {
            return true;
        }
        String command = "bash -c rm -rf " + dir.getPath();

        String win = "win";
        if (os.toLowerCase().startsWith(win)) {
            command = "cmd /c rd /s/q " + dir.getPath();
        }
        try {
            command += "";
            System.out.println("删除命令:" + command);
            Runtime r = Runtime.getRuntime();
            Process p = r.exec(command);

            InputStream error = p.getErrorStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(error, "GBK"));
            StringBuilder buffer = new StringBuilder();
            String s;
            while ((s = bufferedReader.readLine()) != null) {
                buffer.append(s);
            }
            bufferedReader.close();
            p.waitFor();
            System.out.println(buffer.toString());


            System.out.println("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


}