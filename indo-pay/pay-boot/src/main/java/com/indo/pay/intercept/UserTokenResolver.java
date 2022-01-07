package com.indo.pay.intercept;

import com.alibaba.fastjson.JSONObject;
import com.indo.common.annotation.LoginUser;
import com.indo.common.constant.AppConstants;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.redis.utils.RedisUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Component
public class UserTokenResolver implements HandlerMethodArgumentResolver {


    @Resource
    private RedisUtils redisUtils;


    //使用自定义的注解
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterType().isAssignableFrom(LoginInfo.class) && methodParameter.hasParameterAnnotation(LoginUser.class);
//        return methodParameter.hasParameterAnnotation(LoginUser.class);
    }

    //将值注入参数
    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        HttpServletRequest servletRequest = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        String accessToken = servletRequest.getHeader("Authorization");
        if (StringUtils.isBlank(accessToken)) {
            return null;
        }
        Object accObj = redisUtils.get(AppConstants.USER_LOGIN_ACCTOKEN + accessToken);
        if (ObjectUtils.isEmpty(accObj)) {
            return null;
        }
        LoginInfo loginInfo = JSONObject.parseObject((String) accObj, LoginInfo.class);
        return loginInfo;
    }
}
