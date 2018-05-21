package com.fengeee.car.owner.web.configure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * Swagger configure
 *
 * @author lijunrong@cvte.com
 * @date 2018/4/12
 * @since 1.0
 */
@EnableSwagger2
@Configuration
@Profile({"test", "dev", "local"})
public class SwaggerConfigure {

    @Bean
    public Docket buildDocket() {
        List<Parameter> params = new ArrayList<>();

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(buildApiInf())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.fengeee.car.owner.web.controller"))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(params)
                .apiInfo(buildApiInf())
                ;
    }

    private ApiInfo buildApiInf() {
        String errCodeMDesc = "\r\n返回码：\r\n" +
                "000000 成功;\r\n" +
                "090100 请求参数不正确;\r\n" +
                "090101 无数据，或查找资源不存在;\r\n" +
                "090200 数据库操作失败;\r\n" +
                "999999 服务器异常;\r\n";
        return new ApiInfoBuilder().title("API接口文档")
                .version("1.0")
                .description("API接口文档,只在test,dev,local环境生成；" + errCodeMDesc)
                .build();
    }
}
