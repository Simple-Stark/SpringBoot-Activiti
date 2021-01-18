package com.wrh.basis;

import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 项目启动类
 *
 * @author Simple
 * @date 2020-11-11
 */
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class BasisApplication {

    public static void main(String[] args) {
        SpringApplication.run(BasisApplication.class, args);
    }

}
