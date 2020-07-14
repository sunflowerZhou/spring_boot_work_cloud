//package com.zbb.api.config;
//
//import com.zbb.common.util.file.FileUtil;
//import lombok.SneakyThrows;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
//
///**
// * @author sunflower
// */
//@Configuration
//public class WebMvcConfig extends WebMvcConfigurationSupport {
//
//    @SneakyThrows
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        // 上传路径映射 会使spring boot的自动配置失效
//        registry.addResourceHandler("/img/qr_code/**").addResourceLocations("file:" + FileUtil.getJarPath1() + "/img/qr_code/");
//        super.addResourceHandlers(registry);
//    }
//}
