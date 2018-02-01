package com.seewo.mis.frame;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.io.IOException;

/**
 * 项目启动主类
 *
 * @author : wanggaoxiang@cvte.com
 * Date: 2017-11-30
 * @version 1.0
 */
@Slf4j
@SpringBootApplication
@ComponentScan(basePackages = {"com.seewo.mis"})
@MapperScan("com.seewo.mis.frame.dal.mapper")
public class MisApplication {

    /**
     * 主类启动方法
     *
     * @param args args
     */
    public static void main(String[] args) {
        SpringApplication.run(MisApplication.class, args);
        try {
            System.in.read();
            log.info("project Startup success ");
        } catch (IOException e) {
            log.error("project Startup failed ");
        }
    }

}
