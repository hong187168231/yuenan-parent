package com.indo.game.controller.ug;

import com.alibaba.fastjson.JSONObject;
import com.indo.common.annotation.AllowAccess;
import com.indo.common.annotation.LoginUser;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.common.result.ResultCode;
import com.indo.common.utils.i18n.MessageUtils;
import com.indo.game.common.util.IpUtil;
import com.indo.game.service.ug.UgService;
import io.swagger.annotations.Api;
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
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/ug")
@Slf4j
@AllArgsConstructor
@Api(tags = "UG Sports登录并初始化用户游戏账号")
public class UgController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UgService ugService;

    @Autowired
    private RedissonClient redissonClient;

    /**
     * UG Sports登录并初始化用户游戏账号
     */
    @ApiOperation(value = "UG Sports登录并初始化用户游戏账号", httpMethod = "POST")
    @PostMapping("/initGame")
    @AllowAccess
    public Result initGame(@LoginUser LoginInfo loginUser, String platform, HttpServletRequest request) throws InterruptedException {
        logger.info("uglog {} initGame 进入游戏。。。loginUser:{}", loginUser.getId(), loginUser);
        String params = "";
        if (loginUser == null || StringUtils.isBlank(loginUser.getNickName())) {
            return Result.failed(MessageUtils.get("ParameterError"));
        }
        RLock lock = redissonClient.getLock("UG_GAME_" + loginUser.getId());
        boolean res = lock.tryLock(5, TimeUnit.SECONDS);
        try {
            if (res) {
                String ip = IpUtil.getIpAddr(request);
                Result resultInfo = ugService.ugGame(loginUser,  ip, platform);
                if (resultInfo == null) {
                    logger.info("uglog {} initGame result is null. params:{},ip:{}", loginUser.getId(), params, ip);
                    return Result.failed(MessageUtils.get("networktimeout"));
                } else {
                    if (!resultInfo.getCode().equals(ResultCode.SUCCESS)) {
                        return resultInfo;
                    }
                }
                logger.info("uglog {} initGame resultInfo:{}, params:{}", loginUser.getId(), JSONObject.toJSONString(resultInfo), params);
                return resultInfo;
            } else {
                logger.info("uglog {} initGame lock  repeat request. error");
                String ugInitGame3 = MessageUtils.get("networktimeout");
                return Result.failed(ugInitGame3);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("uglog {} initGame occur error:{}. params:{}", loginUser.getId(), e.getMessage(), params);
            return Result.failed(MessageUtils.get("networktimeout"));
        } finally {
            lock.unlock();
        }
    }


}
