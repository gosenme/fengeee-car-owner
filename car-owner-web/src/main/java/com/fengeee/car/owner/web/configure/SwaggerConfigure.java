package com.seewo.mis.attendance.web.configure;

import com.seewo.mis.attendance.common.constant.Constant;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 *  Swagger configure
 *
 * @author lijunrong@cvte.com
 * @date 2018/4/12
 * @since 1.0
 */
@EnableSwagger2
@Configuration
@Profile({"test","dev","local"})
public class SwaggerConfigure {

    @Bean
    public Docket buildDocket() {
        List<Parameter> params = new ArrayList<>();
        params.add(new ParameterBuilder().name(Constant.X_AUTH_TRACEID).description("traceid,通用")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false).build());
        params.add(new ParameterBuilder().name(Constant.X_AUTH_TOKEN).description("token,WEB专用")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false).build());

        params.add(new ParameterBuilder().name(Constant.AUTH_TIME).description("auth-time,C++专用")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false).build());
        params.add(new ParameterBuilder().name(Constant.AUTH_SIGN).description("auth-sign,C++专用")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false).build());

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(buildApiInf())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.seewo.mis.attendance.web.controller"))
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
        return new ApiInfoBuilder().title("考勤服务API接口文档")
                .version("1.0")
                .description("考勤服务API接口文档,只在test,dev,local环境生成；"+ errCodeMDesc)
                .build();
    }
}
