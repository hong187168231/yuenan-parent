package com.indo.user.config;

import com.indo.user.intercept.AuthorizationInterceptor;
import com.indo.user.intercept.UserTokenResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig extends com.indo.common.web.config.WebMvcConfig {

    @Autowired
    private AuthorizationInterceptor authorizationInterceptor;
    @Autowired
    private UserTokenResolver userTokenResolver;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authorizationInterceptor).addPathPatterns("/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(userTokenResolver);
    }
}
