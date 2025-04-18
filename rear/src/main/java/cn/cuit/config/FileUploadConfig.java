package cn.cuit.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class FileUploadConfig implements CommandLineRunner {

    @Value("${spring.upload.image-path}")
    private String uploadPath;

    @Override
    public void run(String... args) {
        try {
            // 获取项目根目录
            String projectRoot = new File("").getAbsolutePath();
            System.out.println("Project root directory: " + projectRoot);
            
            // 创建完整的上传路径
            File uploadDir = new File(projectRoot, uploadPath);
            System.out.println("Full upload path: " + uploadDir.getAbsolutePath());

            // 创建目录
            if (!uploadDir.exists()) {
                if (uploadDir.mkdirs()) {
                    System.out.println("Successfully created directory: " + uploadDir.getAbsolutePath());
                } else {
                    System.err.println("Failed to create directory: " + uploadDir.getAbsolutePath());
                }
            }

            // 验证目录权限
            System.out.println("Directory exists: " + uploadDir.exists());
            System.out.println("Can read: " + uploadDir.canRead());
            System.out.println("Can write: " + uploadDir.canWrite());

            // 检查默认头像
            File defaultAvatar = new File(uploadDir, "default-avatar.jpg");
            if (!defaultAvatar.exists()) {
                System.err.println("Warning: Default avatar not found at: " + defaultAvatar.getAbsolutePath());
            } else {
                System.out.println("Default avatar exists at: " + defaultAvatar.getAbsolutePath());
            }
        } catch (Exception e) {
            System.err.println("Error in FileUploadConfig: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 