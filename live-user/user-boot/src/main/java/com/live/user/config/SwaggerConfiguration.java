package com.live.user.config;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.*;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author puff
 * @Date 2021-02-25 15:36
 * @Version 1.0.0
 */
@Configuration
@EnableSwagger2WebMvc
@Import(BeanValidatorPluginsConfiguration.class)
@Slf4j
public class SwaggerConfiguration {


    @Bean
    public Docket createRestApi() {

        ParameterBuilder ticketPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<>();
        ticketPar.name("dd").description("登录TOKEN")
                .modelRef(new ModelRef("string")).parameterType("header")
                .required(false).defaultValue("").build();
        pars.add(ticketPar.build());
        
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.live"))  //设置扫描 swagger的注解包路径 所有的需要用swagger显示的
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build().globalOperationParameters(pars).enable(true);
    }


    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("系统管理")
                .description("<div style='font-size:14px;color:red;'>用户、角色、部门、菜单、权限、字典、客户端接口</div>")
                .termsOfServiceUrl("https://www.live.tech")
                .contact(new Contact("泡芙技术团队", "https://github.com/ui", "1490493387@qq.com"))
                .license("Open Source")
                .licenseUrl("https://www.apache.org/licenses/LICENSE-2.0")
                .version("1.0.0")
                .build();
    }

}
