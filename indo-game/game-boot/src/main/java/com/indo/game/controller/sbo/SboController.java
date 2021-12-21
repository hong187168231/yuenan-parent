package com.indo.game.controller.sbo;

import com.alibaba.fastjson.JSONObject;
import com.indo.common.annotation.AllowAccess;
import com.indo.common.annotation.LoginUser;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.common.result.ResultCode;
import com.indo.common.utils.i18n.MessageUtils;
import com.indo.game.service.sbo.SboService;
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

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/sbo")
@Slf4j
@AllArgsConstructor
@Api(tags = "SBO Sports游戏登录并初始化用户游戏账号")
public class SboController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SboService sboSportsService;

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 进入游戏
     */
    @ApiOperation(value = "sbo进入游戏", httpMethod = "POST")
    @PostMapping("/initGame")
    @AllowAccess
    public Result initGame(@LoginUser LoginInfo loginUser, String platform) throws InterruptedException {
        logger.info("sbolog {} initGame 进入游戏。。。loginUser:{}", loginUser.getId(), loginUser);
        String params = "";
        if (loginUser == null || StringUtils.isBlank(loginUser.getNickName())) {
            return Result.failed(MessageUtils.get("ParameterError"));
        }
        RLock lock = redissonClient.getLock("SBO_GAME_" + loginUser.getId());
        boolean res = lock.tryLock(5, TimeUnit.SECONDS);
        try {
            if (res) {
                String ip = "";
                Result resultInfo = sboSportsService.sboGame(loginUser, ip, platform);
                if (resultInfo == null) {
                    logger.info("sbolog {} initGame result is null. params:{},ip:{}", loginUser.getId(), params, ip);
                    return Result.failed(MessageUtils.get("networktimeout"));
                } else {
                    if (!resultInfo.getCode().equals(ResultCode.SUCCESS)) {
                        return resultInfo;
                    }
                }
                logger.info("sbolog {} initGame resultInfo:{}, params:{}", loginUser.getId(), JSONObject.toJSONString(resultInfo), params);
                return resultInfo;
            } else {
                logger.info("sbolog {} initGame lock  repeat request. error");
                String sboInitGame3 = MessageUtils.get("networktimeout");
                return Result.failed(sboInitGame3);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("sbolog {} initGame occur error:{}. params:{}", loginUser.getId(), e.getMessage(), params);
            return Result.failed(MessageUtils.get("networktimeout"));
        } finally {
            lock.unlock();
        }
    }
}
