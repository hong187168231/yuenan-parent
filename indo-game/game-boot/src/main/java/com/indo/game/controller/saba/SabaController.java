package com.indo.game.controller.saba;

import com.alibaba.fastjson.JSONObject;
import com.indo.common.annotation.LoginUser;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.common.result.ResultCode;
import com.indo.common.utils.i18n.MessageUtils;
import com.indo.game.common.util.IpUtil;
import com.indo.game.service.saba.SabaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/v1/games/saba")
@Slf4j
@Api(tags = "saba游戏登录并初始化用户游戏账号")
public class SabaController {
    @Autowired
    private SabaService sabaService;

    @Autowired
    private RedissonClient redissonClient;

    /**
     * saba游戏登录并初始化用户游戏账号
     */
    @ApiOperation(value = "saba游戏登录并初始化用户游戏账号", httpMethod = "POST")
    @PostMapping("/initGame")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "platform", value = "第三方游戏平台 ", paramType = "query", dataType = "string", required = true)
    })
    public Result initGame(@LoginUser LoginInfo loginUser,@RequestParam("platform") String platform,
                           HttpServletRequest request) throws InterruptedException {

        String params = "";
        if (loginUser == null || StringUtils.isBlank(loginUser.getAccount())) {
            return Result.failed(MessageUtils.get("ParameterError"));
        }
        log.info("sabalog {} initGame 进入游戏。。。loginUser:{}", platform, loginUser);
        RLock lock = redissonClient.getLock("SABA_GAME_" + loginUser.getId());
        boolean res = lock.tryLock(5, TimeUnit.SECONDS);
        try {
            if (res) {
                String ip = IpUtil.getIpAddr(request);
                Result resultInfo = sabaService.sabaGame(loginUser, ip, platform);
                if (resultInfo == null) {
                    log.info("sabalog {} initGame result is null. params:{},ip:{}", loginUser.getId(), params, ip);
                    return Result.failed(MessageUtils.get("networktimeout"));
                } else {
                    if (!resultInfo.getCode().equals(ResultCode.SUCCESS)) {
                        return resultInfo;
                    }
                }
                log.info("sabalog {} initGame resultInfo:{}, params:{}", loginUser.getId(), JSONObject.toJSONString(resultInfo), params);
                return resultInfo;
            } else {
                log.info("sabalog {} initGame lock  repeat request. error");
                String aeInitGame3 = MessageUtils.get("networktimeout");
                return Result.failed(aeInitGame3);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("sabalog {} initGame occur error:{}. params:{}", loginUser.getId(), e.getMessage(), params);
            return Result.failed(MessageUtils.get("networktimeout"));
        } finally {
            lock.unlock();
        }
    }


    /**
     * saba游戏 强迫登出玩家
     */
    @ApiOperation(value = "saba游戏 强迫登出玩家", httpMethod = "POST")
    @PostMapping("/logout")
    public Result logout(@LoginUser LoginInfo loginUser, HttpServletRequest request) throws InterruptedException {

        String params = "";
        if (loginUser == null) {
            return Result.failed(MessageUtils.get("ParameterError"));
        }
        log.info("sabalog {} logout 进入游戏。。。loginUser:{}", loginUser.getId(), loginUser);
        try {
            String ip = IpUtil.getIpAddr(request);
            Result resultInfo = sabaService.logout(loginUser, ip);
            if (resultInfo == null) {
                log.info("sabalog {} initGame result is null. params:{},ip:{}", loginUser.getId(), params, ip);
                return Result.failed(MessageUtils.get("networktimeout"));
            } else {
                if (!resultInfo.getCode().equals(ResultCode.SUCCESS)) {
                    return resultInfo;
                }
            }
            log.info("sabalog {} initGame resultInfo:{}, params:{}", loginUser.getId(), JSONObject.toJSONString(resultInfo), params);
            return resultInfo;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("sabalog {} logout occur error:{}. params:{}", loginUser.getId(), e.getMessage(), params);
            return Result.failed(MessageUtils.get("networktimeout"));
        }
    }
}
