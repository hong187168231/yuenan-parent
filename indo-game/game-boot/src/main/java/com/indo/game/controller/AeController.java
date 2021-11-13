package com.indo.game.controller;

import com.alibaba.fastjson.JSONObject;
import com.indo.common.annotation.LoginUser;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.game.game.RedisLock;
import com.indo.common.result.Result;
import com.indo.common.result.ResultCode;
import com.indo.common.utils.i18n.MessageUtils;
import com.indo.game.pojo.vo.AeInfoVo;
import com.indo.game.services.ae.AeService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ae")
public class AeController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AeService aeService;


    /**
     * 进去AE棋牌并初始化用户游戏账号
     */
    @PostMapping("/initGame.json")
    public Result<String> initGame(@RequestBody AeInfoVo vo,@LoginUser LoginInfo loginUser) {
        logger.info("aelog {} initGame 进入游戏。。。loginUser:{}", loginUser.getId(), loginUser);
        String params = "";
        if (loginUser == null || StringUtils.isBlank(loginUser.getNickName())) {
            return Result.failed(MessageUtils.get("ParameterError"));
        }
//        if (LoginUserTypeEnum.isTrailAccount(loginUser.getLogintype())) {
//        	String aeInitGame1 = MessageUtils.get("aeInitGame1");
//            return Result.error(aeInitGame1);
//        }
//        if (LoginUserTypeEnum.isAnchor(loginUser.getLogintype())) {
//        	String aeInitGame2 = MessageUtils.get("aeInitGame2");
//            return Result.error(aeInitGame2);
//        }
        //RedisLock lock = new RedisLock("AE_GAME_" + loginUser.getId(), 0, Constants.AE_TIMEOUT_MSECS);
        RedisLock lock = RedisLock.newRequestLock("AE_GAME_" + loginUser.getId(), 5000);
        try {
            if (lock.lock()) {
                String ip = "";
                Result<String> resultInfo = aeService.aeGame(loginUser, vo.getAeCodeId(), ip);
                if (resultInfo == null) {
                    logger.info("aelog {} initGame result is null. params:{},ip:{}", loginUser.getId(), params, ip);
                    return Result.failed(MessageUtils.get("networktimeout"));
                } else {
                    if (!resultInfo.getCode().equals(ResultCode.SUCCESS)) {
                        return resultInfo;
                    }
                }
                aeService.initAccountInfo(loginUser, vo.getAeCodeId(), ip);
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
