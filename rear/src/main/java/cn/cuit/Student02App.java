package cn.cuit;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("cn.cuit.mapper")
public class Student02App {
    public static void main(String[] args) {
        SpringApplication.run(Student02App.class);
    }
}
