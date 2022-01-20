package com.indo.game.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * 配置国际化语言
 */
@Configuration
public class LocaleConfig   implements LocaleResolver{
    /**
     * 默认解析器 其中locale表示默认语言
     */
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver localeResolver = new SessionLocaleResolver();
        localeResolver.setDefaultLocale(Locale.CHINESE);
        return localeResolver;
    }

    /**
     * 默认拦截器 其中lang表示切换语言的参数名
     */
    @Bean
    public WebMvcConfigurer localeInterceptor() {
        return new WebMvcConfigurer() {
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                LocaleChangeInterceptor localeInterceptor = new LocaleChangeInterceptor();
                localeInterceptor.setParamName("locale");
                registry.addInterceptor(localeInterceptor);
            }
        };
    }

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        Locale locale = Locale.getDefault(); //获取默认的地区
        if(request.getParameter("lg")!=null) {  //如果能获取到 说明用户要指定语言
            String str = (String)request.getParameter("lg"); //获取用户选择的语言
            String[] temp = str.split("_");  //分割下字符串
            locale  = new Locale(temp[0],temp[1]);//产生新的Locale
            request.getSession().setAttribute("lg",locale ); //将Locale对象放入session 作为默认的语言地区
        }
        //如果用户没指定语言 从session获取默认的语言地区
        if(request.getSession().getAttribute("lg")!=null) {
            return (Locale)request.getSession().getAttribute("lg");
        }
        return locale ; //返回语言地区
    }

    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {

    }
}
