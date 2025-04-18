package cn.cuit.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.io.File;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Value("${spring.upload.image-path}")
    private String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String projectRoot = new File("").getAbsolutePath();
        registry.addResourceHandler("/api/student/images/**")
                .addResourceLocations("file:" + projectRoot + "/" + uploadPath + "/")
                .setCachePeriod(3600)
                .resourceChain(true);
    }
} 