package com.indo.game.controller.awc;

import com.alibaba.fastjson.JSONObject;
import com.indo.common.annotation.AllowAccess;
import com.indo.common.annotation.LoginUser;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.common.result.ResultCode;
import com.indo.common.utils.i18n.MessageUtils;
import com.indo.game.common.util.IpUtil;
import com.indo.game.service.awc.AwcService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/awc")
@Slf4j
@Api(tags = "AE真人、SV388斗鸡游戏登录并初始化用户游戏账号")
public class AwcController {
    @Autowired
    private AwcService awcService;

    @Autowired
    private RedissonClient redissonClient;

    /**
     * AE真人、SV388斗鸡游戏登录并初始化用户游戏账号
     */
    @ApiOperation(value = "AE真人、SV388斗鸡游戏登录并初始化用户游戏账号", httpMethod = "POST")
    @PostMapping("/initGame")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "isMobileLogin", value = "是否手机登录 ", paramType = "query", dataType = "int", required = true),
            @ApiImplicitParam(name = "gameCode", value = "游戏编码 ", paramType = "query", dataType = "string", required = true),
            @ApiImplicitParam(name = "platform", value = "平台 ", paramType = "query", dataType = "string", required = true)
    })
    public Result initGame(@LoginUser LoginInfo loginUser, @RequestParam("isMobileLogin") String isMobileLogin,
                           @RequestParam("gameCode") String gameCode, @RequestParam("gameCode") String platform,
                           HttpServletRequest request) throws InterruptedException {
        log.info("AE真人、SV388斗鸡log {} initGame 进入游戏。。。loginUser:{}", loginUser.getId(), loginUser);
        String params = "";
        if (loginUser == null || StringUtils.isBlank(loginUser.getNickName())) {
            return Result.failed(MessageUtils.get("ParameterError"));
        }
        RLock lock = redissonClient.getLock("AWC_GAME_" + loginUser.getId());
        boolean res = lock.tryLock(5, TimeUnit.SECONDS);
        try {
            if (res) {
                String ip = IpUtil.getIpAddr(request);
                Result resultInfo = awcService.awcGame(loginUser, isMobileLogin, gameCode, ip, platform);
                if (resultInfo == null) {
                    log.info("AE真人、SV388斗鸡log {} initGame result is null. params:{},ip:{}", loginUser.getId(), params, ip);
                    return Result.failed(MessageUtils.get("networktimeout"));
                } else {
                    if (!resultInfo.getCode().equals(ResultCode.SUCCESS)) {
                        return resultInfo;
                    }
                }
                log.info("AE真人、SV388斗鸡log {} initGame resultInfo:{}, params:{}", loginUser.getId(), JSONObject.toJSONString(resultInfo), params);
                return resultInfo;
            } else {
                log.info("AE真人、SV388斗鸡log {} initGame lock  repeat request. error");
                String aeInitGame3 = MessageUtils.get("networktimeout");
                return Result.failed(aeInitGame3);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("AE真人、SV388斗鸡log {} initGame occur error:{}. params:{}", loginUser.getId(), e.getMessage(), params);
            return Result.failed(MessageUtils.get("networktimeout"));
        } finally {
            lock.unlock();
        }
    }


    /**
     * AE真人、SV388斗鸡游戏 强迫登出玩家
     */
    @ApiOperation(value = "AE真人、SV388斗鸡游戏 强迫登出玩家", httpMethod = "POST")
    @PostMapping("/logout")
    @AllowAccess
    public Result logout(@LoginUser LoginInfo loginUser, String userIds, HttpServletRequest request) throws InterruptedException {
        log.info("AE真人、SV388斗鸡log {} logout 进入游戏。。。loginUser:{}", loginUser.getId(), loginUser);
        String params = "";
        if (loginUser == null) {
            return Result.failed(MessageUtils.get("ParameterError"));
        }
        try {
            String ip = IpUtil.getIpAddr(request);
            Result resultInfo = awcService.logout(loginUser, ip, userIds);
            if (resultInfo == null) {
                log.info("AE真人、SV388斗鸡log {} initGame result is null. params:{},ip:{}", loginUser.getId(), params, ip);
                return Result.failed(MessageUtils.get("networktimeout"));
            } else {
                if (!resultInfo.getCode().equals(ResultCode.SUCCESS)) {
                    return resultInfo;
                }
            }
            log.info("AE真人、SV388斗鸡log {} initGame resultInfo:{}, params:{}", loginUser.getId(), JSONObject.toJSONString(resultInfo), params);
            return resultInfo;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("AE真人、SV388斗鸡log {} logout occur error:{}. params:{}", loginUser.getId(), e.getMessage(), params);
            return Result.failed(MessageUtils.get("networktimeout"));
        }
    }
}
