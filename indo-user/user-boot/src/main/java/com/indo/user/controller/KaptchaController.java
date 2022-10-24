package com.indo.user.controller;

import cn.hutool.core.util.IdUtil;
import com.indo.common.annotation.AllowAccess;
import com.indo.common.redis.utils.RedisUtils;
import com.indo.common.result.Result;
import com.indo.user.common.kaptcha.LoginCodeEnum;
import com.indo.user.common.kaptcha.LoginProperties;
import com.wf.captcha.base.Captcha;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Api(tags = "验证码接口")
@RestController
@RequestMapping("/api/v1/kaptcha")
@Slf4j
@AllArgsConstructor
public class KaptchaController {

    @Resource
    private LoginProperties loginProperties;
    @Resource
    private RedisUtils redisUtils;


    @ApiOperation(value = "获取验证码接口", httpMethod = "GET")
    @AllowAccess
    @GetMapping("kaptcha")
    public Result kaptcha(HttpServletRequest request) {
        // 获取运算的结果
        Captcha captcha = loginProperties.getCaptcha(request);
        String uuid = IdUtil.simpleUUID();
        //当验证码类型为 arithmetic时且长度 >= 2 时，captcha.text()的结果有几率为浮点型
        String captchaValue = captcha.text();
        if (captcha.getCharType() - 1 == LoginCodeEnum.arithmetic.ordinal() && captchaValue.contains(".")) {
            captchaValue = captchaValue.split("\\.")[0];
        }
        // 保存
        redisUtils.set(uuid, captchaValue, loginProperties.getLoginCode().getExpiration(), TimeUnit.MINUTES);
        // 验证码信息
        Map<String, Object> imgResult = new HashMap<String, Object>(2) {{
            put("img", captcha.toBase64());
            put("uuid", uuid);
        }};

        return Result.success(imgResult);

    }
}
