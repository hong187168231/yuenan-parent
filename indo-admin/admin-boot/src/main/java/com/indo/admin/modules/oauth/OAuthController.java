package com.indo.admin.modules.oauth;

import cn.hutool.json.JSONObject;
import com.indo.admin.common.enums.OAuthClientEnum;
import com.indo.admin.modules.sys.service.ISysIpLimitService;
import com.indo.admin.pojo.entity.SysIpLimit;
import com.indo.common.constant.AuthConstants;
import com.indo.common.result.Result;
import com.indo.common.web.exception.BizException;
import com.indo.common.web.util.IPUtils;
import com.indo.common.web.util.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Api(tags = "认证中心")
@RestController
@RequestMapping("/oauth")
@AllArgsConstructor
@Slf4j
public class OAuthController {

    private TokenEndpoint tokenEndpoint;
    private RedisTemplate redisTemplate;
    @Resource
    private ISysIpLimitService sysIpLimitService;

    @ApiOperation(value = "OAuth2认证", notes = "login")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "grant_type", defaultValue = "password", value = "授权模式", required = true),
            @ApiImplicitParam(name = "client_id", value = "Oauth2客户端ID（新版本需放置请求头）", required = true),
            @ApiImplicitParam(name = "client_secret", value = "Oauth2客户端秘钥（新版本需放置请求头）", required = true),
            @ApiImplicitParam(name = "refresh_token", value = "刷新token"),
            @ApiImplicitParam(name = "username", defaultValue = "admin", value = "登录用户名"),
            @ApiImplicitParam(name = "password", defaultValue = "123456", value = "登录密码")
    })
    @PostMapping("/token")
    public Object postAccessToken(
            @ApiIgnore Principal principal,
            @ApiIgnore @RequestParam Map<String,
                    String> parameters) throws HttpRequestMethodNotSupportedException {
        //白名单校验
        List<SysIpLimit> list =sysIpLimitService.findSysIpLimitByType(2);
        if(list!=null||list.size()>0){
            // 获取请求信息
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            String clientIP = IPUtils.getIpAddr(request);
            Boolean status = false;
            for(SysIpLimit l :list){
                if(l.getIp().equals(clientIP)){
                    status=true;
                }
            }
            if(!status){
                log.error("非法的IP企图登录后台管理系统:{}",clientIP);
                throw new BizException("非法的IP登录");
            }
        }
        String clientId = JwtUtils.getOAuthClientId();
        log.info("=============================" + clientId);
        OAuthClientEnum client = OAuthClientEnum.getByClientId(clientId);
        switch (client) {
            case TEST: // knife4j接口测试文档使用 client_id/client_secret : client/123456
                return tokenEndpoint.postAccessToken(principal, parameters).getBody();
            default:
                return Result.success(tokenEndpoint.postAccessToken(principal, parameters).getBody());
        }
    }


    @ApiOperation(value = "注销", notes = "logout")
    @DeleteMapping("/logout")
    public Result logout() {
        JSONObject payload = JwtUtils.getJwtPayload();
        String jti = payload.getStr(AuthConstants.JWT_JTI); // JWT唯一标识
        Long expireTime = payload.getLong(AuthConstants.JWT_EXP); // JWT过期时间戳(单位：秒)
        if (expireTime != null) {
            long currentTime = System.currentTimeMillis() / 1000;// 当前时间（单位：秒）
            if (expireTime > currentTime) { // token未过期，添加至缓存作为黑名单限制访问，缓存时间为token过期剩余时间
                redisTemplate.opsForValue().set(AuthConstants.TOKEN_BLACKLIST_PREFIX + jti, null, (expireTime - currentTime), TimeUnit.SECONDS);
            }
        } else { // token 永不过期则永久加入黑名单
            redisTemplate.opsForValue().set(AuthConstants.TOKEN_BLACKLIST_PREFIX + jti, null);
        }
        return Result.success("注销成功");
    }


}
