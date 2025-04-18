package cn.cuit.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import javax.annotation.PostConstruct;
import java.io.File;

@Configuration
public class UploadConfig {
    
    @Value("${spring.upload.image-path}")
    private String uploadPath;
    
    @PostConstruct
    public void init() {
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
    }
} 