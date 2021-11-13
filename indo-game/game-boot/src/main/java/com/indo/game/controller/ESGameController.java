package com.indo.game.controller;

import com.indo.common.annotation.LoginUser;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.game.game.RedisLock;
import com.indo.common.result.Result;
import com.indo.common.result.ResultCode;
import com.indo.common.utils.i18n.MessageUtils;
import com.indo.game.services.es.ESGameService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 电竞游戏
 *
 * @author
 */
@RestController
@RequestMapping("/esgame")
public class ESGameController{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ESGameService esGameService;

    /**
     * 电竞游戏初始化用户游戏账号，冰
     *
     * @param
     * @return
     * @throws Exception
     */
    //@PostMapping("/go.json")
    public Result<String> go(@LoginUser LoginInfo loginUser) {
        if (loginUser == null || StringUtils.isBlank(loginUser.getNickName())) {
            return Result.failed(MessageUtils.get("ParameterError"));
        }
//        if (LoginUserTypeEnum.isTrailAccount(loginUser.getLogintype())) {
//        	String esGo1 = MessageUtils.get("esGo1");
//            return ResultInfo.error(esGo1);
//        }
//        if (LoginUserTypeEnum.isAnchor(loginUser.getLogintype())) {
//        	String esGo2 = MessageUtils.get("esGo2");
//            return ResultInfo.error(esGo2);
//        }

        RedisLock lock = RedisLock.newRequestLock("ES_GAME_GO_" + loginUser.getId(), 5000);
        try {
            if (lock.lock()) {
                String ip = "";
                Result<String> resultInfo = esGameService.go(loginUser, ip);
                if (resultInfo == null) {
                    logger.info("eslog {} go es game result is null. ip:{},resultInfo:{}", loginUser.getId(), ip, resultInfo);
                    return Result.failed(MessageUtils.get("networktimeout"));
                } else {
                    if (!resultInfo.getCode().equals(ResultCode.SUCCESS)) {
                        return resultInfo;
                    }
                }
                // 异步初始化用户游戏账号
                esGameService.initAccountInfo(loginUser, ip);
                return resultInfo;
            } else {
                logger.info("eslog {} go redis look is error ", loginUser.getId());
                return Result.success();
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("eslog {} .go occur error. exception:{}", loginUser.getId(), e.getMessage());
            return Result.failed(MessageUtils.get("networktimeout"));
        } finally {
            lock.unlockWhenHoldTime();
        }

    }
}
