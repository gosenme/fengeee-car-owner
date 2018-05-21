package com.fengeee.car.owner.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 项目启动主类
 *
 * @author : wanggaoxiang@cvte.com
 * Date: 2017-11-30
 * @version 1.0
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.fengeee.car","com.seewo.mis"})
/*@MapperScan("com.seewo.mis.frame.dal.mapper")*/
public class CarOwnerApplication {

    /**
     * 主类启动方法
     *
     * @param args args
     */
    public static void main(String[] args) {
        SpringApplication.run(CarOwnerApplication.class, args);
    }

}
