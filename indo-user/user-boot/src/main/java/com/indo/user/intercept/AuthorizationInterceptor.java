package com.indo.user.intercept;

import com.alibaba.fastjson.JSONObject;
import com.indo.common.annotation.AllowAccess;
import com.indo.common.constant.AppConstants;
import com.indo.common.constant.AuthConstants;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.redis.utils.RedisUtils;
import com.indo.common.result.ResultCode;
import com.indo.common.utils.BaseUtil;
import com.indo.common.utils.DeviceInfoUtil;
import com.indo.common.utils.IPAddressUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {

    @Resource
    private RedisUtils redisUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取用户凭证
        String token = request.getHeader(AuthConstants.AUTHORIZATION_KEY);

        //获取前端信息
        DeviceInfoUtil.setIp(IPAddressUtil.getIpAddress(request));
        DeviceInfoUtil.setArea(IPAddressUtil.getClientArea(request));
        DeviceInfoUtil.setType(IPAddressUtil.getMobileDevice(request));
        String source = request.getHeader("source");
        log.info("hq_source:{}",source);
        if(StringUtils.isNotBlank(source)){
            DeviceInfoUtil.setSource(source);
        }
        String deviceId = request.getHeader("deviceId");
        log.info("hq_deviceId:{}",deviceId);
        if(StringUtils.isNotBlank(deviceId)){
            DeviceInfoUtil.setDeviceId(deviceId);
        }

        AllowAccess annotation;
        if (handler instanceof HandlerMethod) {
            annotation = ((HandlerMethod) handler).getMethodAnnotation(AllowAccess.class);
        } else {
            return true;
        }
        Object userObj = redisUtils.get(AppConstants.USER_LOGIN_ACCTOKEN + token);
        if (annotation != null) {
            return super.preHandle(request, response, handler);
        } else if (StringUtils.isEmpty(token)) {
            BaseUtil.writer401Response(response, ResultCode.LIVE_ERROR_401);
            return false;
        } else if (null == userObj) {
            BaseUtil.writer401Response(response, ResultCode.LIVE_ERROR_401);
            return false;
        } else {
            LoginInfo loginInfo = JSONObject.parseObject((String) userObj, LoginInfo.class);
            if (loginInfo != null) {
                String newToken = (String) redisUtils.get(AppConstants.USER_LOGIN_INFO_KEY + loginInfo.getAccount());
                if (StringUtils.isEmpty(newToken)) {
                    BaseUtil.writer401Response(response, ResultCode.LIVE_ERROR_401);
                    return false;
                }
                if (!token.equals(newToken)) {
                    BaseUtil.writer401Response(response, ResultCode.RESOURCE_NOT_FOUND2);
                    redisUtils.del(token);
                    return false;
                }
                return super.preHandle(request, response, handler);
            }
        }
        return false;
    }
}
