package com.zbb.api.web;

import com.zbb.common.util.file.FileUtil;
import com.zbb.common.util.qrcode.QrCode;
import org.springframework.stereotype.Controller;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author sunflower
 */
@Controller
@RequestMapping("/img")
public class ImgQrCodeController {

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
}
