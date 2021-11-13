package com.indo.game.controller;

import com.indo.common.annotation.LoginUser;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.game.game.RedisLock;
import com.indo.common.result.Result;
import com.indo.common.result.ResultCode;
import com.indo.common.utils.i18n.MessageUtils;
import com.indo.game.pojo.vo.MgInfoVo;
import com.indo.game.services.mg.MgService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author
 */
@RestController
@RequestMapping("/mg")
public class MgController{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MgService mgService;

    /**
     * 进去开源棋牌并初始化用户游戏账号
     *
     * @param
     * @return
     * @throws Exception
     */
    @PostMapping("/mgGame.json")
    public Result<String> game(@RequestBody MgInfoVo vo, @LoginUser LoginInfo loginUser) {
        if (loginUser == null) {
            return Result.failed(MessageUtils.get("ParameterError"));
        }
//        if (LoginUserTypeEnum.isTrailAccount(loginUser.getLogintype())) {
//        	String mgGame1 = MessageUtils.get("mgGame1");
//            return Result.error(mgGame1);
//        }
//        if (LoginUserTypeEnum.isAnchor(loginUser.getLogintype())) {
//        	String mgGame2 = MessageUtils.get("mgGame2");
//            return Result.error(mgGame2);
//        }
        RedisLock lock = RedisLock.newRequestLock("MG_GAME_" + loginUser.getId(), 5000);
        try {
            if (lock.lock()) {
                String ip = "";
                Result<String> resultInfo = mgService.mgGame(loginUser, vo.getGameCode());
                if (resultInfo == null) {
                    return Result.failed(MessageUtils.get("networktimeout"));
                } else {
                    if (!resultInfo.getCode().equals(ResultCode.SUCCESS)) {
                        return resultInfo;
                    }
                }
                logger.info("mglog {} game mg游戏连接成功.  GameCode:{},ip:{}", loginUser.getId(), vo.getGameCode(), ip);
                // 异步初始化用户游戏账号
                mgService.initAccountInfo(loginUser, vo.getGameCode());
                return resultInfo;
            } else {
                logger.info("mglog {} game repeat request", loginUser.getId());
                return Result.success(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("mglog {} game occur error. params:{}", loginUser.getId(), e.getMessage());
            return Result.failed(MessageUtils.get("networktimeout"));
        } finally {
            lock.unlockWhenHoldTime();
        }
    }

}
