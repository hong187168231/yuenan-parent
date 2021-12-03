package com.indo.game.controller;

import com.alibaba.fastjson.JSONObject;
import com.indo.common.annotation.AllowAccess;
import com.indo.common.annotation.LoginUser;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.common.result.ResultCode;
import com.indo.common.utils.i18n.MessageUtils;
import com.indo.game.game.RedisLock;
import com.indo.game.service.awc.AwcService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/awc")
@Slf4j
@AllArgsConstructor
@Api(tags = "AE真人、SV388斗鸡游戏登录并初始化用户游戏账号")
public class AwcController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AwcService awcAeSexybcrtService;


    /**
     * AE真人、SV388斗鸡游戏登录并初始化用户游戏账号
     */
    @ApiOperation(value = "AE真人、SV388斗鸡游戏登录并初始化用户游戏账号", httpMethod = "POST")
    @PostMapping("/initGame")
    @AllowAccess
    public Result<String> initGame( @LoginUser LoginInfo loginUser, String isMobileLogin,String gameCode,String platform) {
        logger.info("aelog {} initGame 进入游戏。。。loginUser:{}", loginUser.getId(), loginUser);
        String params = "";
        if (loginUser == null || StringUtils.isBlank(loginUser.getNickName())) {
            return Result.failed(MessageUtils.get("ParameterError"));
        }
        RedisLock lock = RedisLock.newRequestLock("AWC_GAME_" + loginUser.getId(), 5000);
        try {
            if (lock.lock()) {
                String ip = "";
                Result<String> resultInfo = awcAeSexybcrtService.awcGame(loginUser,isMobileLogin,gameCode, ip,platform);
                if (resultInfo == null) {
                    logger.info("aelog {} initGame result is null. params:{},ip:{}", loginUser.getId(), params, ip);
                    return Result.failed(MessageUtils.get("networktimeout"));
                } else {
                    if (!resultInfo.getCode().equals(ResultCode.SUCCESS)) {
                        return resultInfo;
                    }
                }
                logger.info("aelog {} initGame resultInfo:{}, params:{}", loginUser.getId(), JSONObject.toJSONString(resultInfo), params);
                return resultInfo;
            } else {
                logger.info("aelog {} initGame lock  repeat request. error");
                String aeInitGame3 = MessageUtils.get("networktimeout");
                return Result.failed(aeInitGame3);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("aelog {} initGame occur error:{}. params:{}", loginUser.getId(), e.getMessage(), params);
            return Result.failed(MessageUtils.get("networktimeout"));
        } finally {
            //lock.unlockByHoldTime(3000);
            lock.unlockWhenHoldTime();
        }
    }
}
