//package com.indo.user.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.MessageSource;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.validation.Validator;
//import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
//
//
//@Configuration
//public class ValidationConfig extends com.indo.common.web.config.WebMvcConfig {
//
//    @Autowired
//    private MessageSource messageSource;
//
//    @Override
//    public Validator getValidator() {
//        return validator();
//    }
//
//    @Bean
//    public Validator validator() {
//        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
//        validator.setValidationMessageSource(messageSource);
//        return validator;
//    }
//
//}