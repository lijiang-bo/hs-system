package com.jsu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;


@ServletComponentScan// 扫描servlet、filter、listener
@SpringBootApplication
@MapperScan("com.jsu.mapper")
public class HsWebManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(HsWebManagementApplication.class, args);
    }

}
