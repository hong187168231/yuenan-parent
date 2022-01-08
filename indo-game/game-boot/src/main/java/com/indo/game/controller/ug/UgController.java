package com.indo.game.controller.ug;

import com.alibaba.fastjson.JSONObject;
import com.indo.common.annotation.LoginUser;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.common.result.ResultCode;
import com.indo.common.utils.IPAddressUtil;
import com.indo.common.utils.i18n.MessageUtils;
import com.indo.game.service.ug.UgService;
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
@RequestMapping("/api/v1/games/ug")
@Slf4j
@Api(tags = "UG Sports登录并初始化用户游戏账号")
public class UgController {

    @Autowired
    private UgService ugService;

    @Autowired
    private RedissonClient redissonClient;

    /**
     * UG Sports登录并初始化用户游戏账号
     */
    @ApiOperation(value = "UG Sports登录并初始化用户游戏账号", httpMethod = "POST")
    @PostMapping("/initGame")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "platform", value = "平台 ", paramType = "query", dataType = "string", required = true),
                @ApiImplicitParam(name = "WebType", value = "登录类型: PC,Smart,Wap", paramType = "query", dataType = "string", required = true)
    })
    public Result initGame(@LoginUser LoginInfo loginUser, @RequestParam("platform") String platform,String WebType,
                           HttpServletRequest request) throws InterruptedException {

        String params = "";
        if (loginUser == null || StringUtils.isBlank(loginUser.getAccount())) {
            return Result.failed(MessageUtils.get("youarenotloggedin"));
        }
        log.info("uglog {} initGame 进入游戏。。。loginUser:{}", platform, loginUser);
        RLock lock = redissonClient.getLock("UG_GAME_" + loginUser.getId());
        boolean res = lock.tryLock(5, TimeUnit.SECONDS);
        try {
            if (res) {
                String ip = IPAddressUtil.getIpAddress(request);
                Result resultInfo = ugService.ugGame(loginUser, ip, platform,WebType);
                if (resultInfo == null) {
                    log.info("uglog {} initGame result is null. params:{},ip:{}", loginUser.getId(), params, ip);
                    return Result.failed(MessageUtils.get("networktimeout"));
                } else {
                    if (!resultInfo.getCode().equals(ResultCode.SUCCESS)) {
                        return resultInfo;
                    }
                }
                log.info("uglog {} initGame resultInfo:{}, params:{}", loginUser.getId(), JSONObject.toJSONString(resultInfo), params);
                return resultInfo;
            } else {
                log.info("uglog {} initGame lock  repeat request. error");
                String ugInitGame3 = MessageUtils.get("networktimeout");
                return Result.failed(ugInitGame3);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("uglog {} initGame occur error:{}. params:{}", loginUser.getId(), e.getMessage(), params);
            return Result.failed(MessageUtils.get("networktimeout"));
        } finally {
            lock.unlock();
        }
    }
    /**
     * ug登出玩家
     */
    @ApiOperation(value = "ug登出玩家", httpMethod = "POST")
    @PostMapping("/logout")
    public Result logout(@LoginUser LoginInfo loginUser, HttpServletRequest request) throws InterruptedException {

        String params = "";
        if (loginUser == null) {
            return Result.failed(MessageUtils.get("youarenotloggedin"));
        }
        log.info("uglog {} logout 进入游戏。。。loginUser:{}", loginUser.getId(), loginUser);
        try {
            String ip = IPAddressUtil.getIpAddress(request);
            Result resultInfo = ugService.logout(loginUser, ip);
            if (resultInfo == null) {
                log.info("uglog {} initGame result is null. params:{},ip:{}", loginUser.getId(), params, ip);
                return Result.failed(MessageUtils.get("networktimeout"));
            } else {
                if (!resultInfo.getCode().equals(ResultCode.SUCCESS)) {
                    return resultInfo;
                }
            }
            log.info("uglog {} initGame resultInfo:{}, params:{}", loginUser.getId(), JSONObject.toJSONString(resultInfo), params);
            return resultInfo;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("uglog {} logout occur error:{}. params:{}", loginUser.getId(), e.getMessage(), params);
            return Result.failed(MessageUtils.get("networktimeout"));
        }
    }

}
