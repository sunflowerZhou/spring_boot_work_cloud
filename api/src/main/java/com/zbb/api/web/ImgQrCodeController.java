package com.zbb.api.web;

import com.zbb.bean.Result;
import com.zbb.common.util.file.FileUtil;
import com.zbb.common.util.qrcode.QrCode;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * @author sunflower
 */
@Log4j2
@Controller
@RequestMapping("/img")
public class ImgQrCodeController {

    @Value("${address}")
    public String address;

    @RequestMapping(value = "/qr_code/{imageName}",method = RequestMethod.GET)
    public String qrCode(@PathVariable("imageName") String imageName,HttpServletResponse response) throws Exception {
        String jarPath1 = FileUtil.getJarPath1();

        File file = new File(jarPath1 + QrCode.CODE_URL + imageName);
        response.setCharacterEncoding("utf-8");
        response.setContentType(FileUtil.getMineType(imageName));

        OutputStream out = response.getOutputStream();
        response.setContentLength((int) file.length());

        try (InputStream is = new FileInputStream(file)) {
            IOUtils.copy(is, out);
            out.flush();
            out.close();
        }
        return null;
    }

    /**
     * 处理文件上传
     */
    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "处理文件上传", httpMethod = "POST")
    public String uploading(@RequestParam("file") MultipartFile file) throws Exception {
        String jarPath1 = FileUtil.getJarPath1();
        File targetFile = new File(jarPath1 + QrCode.CODE_URL);
        if (!targetFile.exists()) {
            boolean mkdirs = targetFile.mkdirs();
            System.out.println(mkdirs);
        }
        try (FileOutputStream out = new FileOutputStream(jarPath1 + QrCode.CODE_URL + file.getOriginalFilename())){
            out.write(file.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("文件上传失败!");
            return Result.failResult("文件上传失败");
        }
        log.info("文件上传成功!");
        return Result.succResult(address + QrCode.CODE_URL + file.getOriginalFilename());
    }
}
