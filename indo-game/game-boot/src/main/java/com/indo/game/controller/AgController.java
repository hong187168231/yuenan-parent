package com.indo.game.controller;

import com.indo.common.annotation.LoginUser;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.game.game.RedisLock;
import com.indo.common.result.Result;
import com.indo.common.result.ResultCode;
import com.indo.common.utils.i18n.MessageUtils;
import com.indo.game.pojo.vo.AGInfoVo;
import com.indo.game.services.ag.AgService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dlucky
 */
@RestController
@RequestMapping("/ag")
public class AgController{

    private static final Logger logger = LoggerFactory.getLogger(AgController.class);

    @Autowired
    private AgService agService;

    /**
     * 跳转AG并初始化游戏账号信息
     *
     * @param agInfoVo
     * @return
     * @throws Exception
     */
    @PostMapping("/agJump.json")
    public Result<String> agJump(@RequestBody AGInfoVo agInfoVo,@LoginUser LoginInfo loginUser) {
        String actype = agInfoVo.getActype();
        String gameType = agInfoVo.getGameType();
        // 真钱账号参数校验
        if ("1".equals(actype) && (loginUser == null || StringUtils.isBlank(loginUser.getNickName()))) {
            return Result.failed(MessageUtils.get("ParameterError"));
        }
//        if (LoginUserTypeEnum.isTrailAccount(loginUser.getLogintype())) {
//        	String agJump1 = MessageUtils.get("agJump1");
//            return Result.error(agJump1);
//        }
//        if (LoginUserTypeEnum.isAnchor(loginUser.getLogintype())) {
//        	String agJump2 = MessageUtils.get("agJump2");
//            return Result.error(agJump2);
//        }
        RedisLock lock = RedisLock.newRequestLock("AG_JUMP_" + loginUser.getId(), 5000);
        try {
            if (lock.lock()) {
                String ip = "";
                Result<String> resultInfo = agService.agJump(loginUser, actype, gameType, ip);
                if (resultInfo == null) {
                    logger.info("ag game result is null");
                    return Result.failed(MessageUtils.get("networktimeout"));
                } else {
                    if (!resultInfo.getCode().equals(ResultCode.SUCCESS)) {
                        return resultInfo;
                    }
                }
                if ("1".equals(actype)) {
                    // 异步 初始化游戏账号
                    agService.initAndStartGanmeAG(loginUser, actype, gameType, ip);
                }
                return resultInfo;
            } else {
                logger.info("ag {} agJump repeat request. gameType:{}", loginUser.getId(), gameType);
                return Result.success(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("agJump {} occur error. exception:{}", loginUser.getId(), e.getMessage());
            return Result.failed(MessageUtils.get("networktimeout"));
        } finally {
            lock.unlockWhenHoldTime();
        }
    }

}
