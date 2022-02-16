package com.indo.game.controller.app;

import com.alibaba.fastjson.JSONObject;
import com.indo.common.annotation.LoginUser;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.common.result.ResultCode;
import com.indo.common.utils.IPAddressUtil;
import com.indo.common.utils.i18n.MessageUtils;
import com.indo.game.service.awc.AwcService;
import com.indo.game.service.saba.SabaService;
import com.indo.game.service.sbo.SboService;
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
@RequestMapping("/api/v1/games/platform")
@Slf4j
@Api(tags = "登录、退出平台")
public class GameController {
    @Autowired
    private AwcService awcService;
    @Autowired
    private UgService ugService;
    @Autowired
    private SboService sboSportsService;
    @Autowired
    private SabaService sabaService;
    @Autowired
    private RedissonClient redissonClient;

    @ApiOperation(value = "登录平台", httpMethod = "POST")
    @PostMapping("/loginPlatform")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "platform", value = "第三方游戏平台代码(AWC,UG,SBO,SABA) ", paramType = "query", dataType = "string", required = true)
    })
    public Result loginPlatform(@LoginUser LoginInfo loginUser, @RequestParam("platform") String platform,
                           HttpServletRequest request) throws InterruptedException {

        String params = "";
        if (loginUser == null || StringUtils.isBlank(loginUser.getAccount())) {
            return Result.failed(MessageUtils.get("youarenotloggedin"));
        }
        log.info("登录、退出平台log {} loginPlatform 进入游戏。。。loginUser:{}",platform, loginUser);

        RLock lock = redissonClient.getLock("AWC_GAME_" + loginUser.getId());
        boolean res = lock.tryLock(5, TimeUnit.SECONDS);
        try {
            if (res) {
                String ip = IPAddressUtil.getIpAddress(request);
                Result resultInfo = null;
                if("AWC".equals(platform)){
                    resultInfo = awcService.awcGame(loginUser, "1", ip, platform,platform);
                }
                if("UG".equals(platform)){
                    resultInfo = ugService.ugGame(loginUser, ip, platform,"Smart",platform);
                }
                if("SBO".equals(platform)){
                    resultInfo = sboSportsService.sboGame(loginUser, ip, platform,platform);
                }
                if("SABA".equals(platform)){
                    resultInfo = sabaService.sabaGame(loginUser, ip, platform,platform);
                }
                if (resultInfo == null) {
                    log.info("登录、退出平台log {} loginPlatform result is null. params:{},ip:{}", loginUser.getId(), params, ip);
                    return Result.failed(MessageUtils.get("networktimeout"));
                }
                log.info("登录、退出平台log {} loginPlatform resultInfo:{}, params:{}", loginUser.getId(), JSONObject.toJSONString(resultInfo), params);
                return resultInfo;
            } else {
                log.info("登录、退出平台log {} loginPlatform lock  repeat request. error");
                String aeInitGame3 = MessageUtils.get("networktimeout");
                return Result.failed(aeInitGame3);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("登录、退出平台log {} loginPlatform occur error:{}. params:{}", loginUser.getId(), e.getMessage(), params);
            return Result.failed(MessageUtils.get("networktimeout"));
        } finally {
            try {
                lock.unlock();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @ApiOperation(value = "强迫登出玩家", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "platform", value = "第三方游戏平台代码 ", paramType = "query", dataType = "string", required = true)
    })
    @PostMapping("/logoutPlatform")
    public Result logout(@LoginUser LoginInfo loginUser,@RequestParam("platform") String platform, HttpServletRequest request) throws InterruptedException {

        String params = "";
        if (loginUser == null) {
            return Result.failed(MessageUtils.get("youarenotloggedin"));
        }
        log.info("登录、退出平台log {} logout 进入游戏。。。loginUser:{}", loginUser.getId(), loginUser);

        try {
            Result resultInfo = new Result();
            String ip = IPAddressUtil.getIpAddress(request);
            if("AWC".equals(platform)){
                resultInfo = awcService.logout(loginUser, ip);
            }
            if("UG".equals(platform)){
                resultInfo = ugService.logout(loginUser, ip);
            }
            if("SBO".equals(platform)){
                resultInfo = sboSportsService.logout(loginUser, ip);
            }
            if("SABA".equals(platform)){
                resultInfo = sabaService.logout(loginUser, ip);
            }
            if (resultInfo == null) {
                log.info("登录、退出平台log {} loginPlatform result is null. params:{},ip:{}", loginUser.getId(), params, ip);
                return Result.failed(MessageUtils.get("networktimeout"));
            } else {
                if (!resultInfo.getCode().equals(ResultCode.SUCCESS)) {
                    return resultInfo;
                }
            }
            log.info("登录、退出平台log {} loginPlatform resultInfo:{}, params:{}", loginUser.getId(), JSONObject.toJSONString(resultInfo), params);
            return resultInfo;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("登录、退出平台log {} logout occur error:{}. params:{}", loginUser.getId(), e.getMessage(), params);
            return Result.failed(MessageUtils.get("networktimeout"));
        }
    }
}
