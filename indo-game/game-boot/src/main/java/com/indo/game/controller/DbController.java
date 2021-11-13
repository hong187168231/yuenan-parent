package com.indo.game.controller;

import com.indo.common.annotation.LoginUser;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.game.game.RedisLock;
import com.indo.common.result.Result;
import com.indo.common.result.ResultCode;
import com.indo.common.utils.i18n.MessageUtils;
import com.indo.game.pojo.vo.DbInfoVo;
import com.indo.game.services.db.DbService;
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
@RequestMapping("/jdb")
public class DbController{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DbService dbService;

    /**
     * 进去MG电子并初始化用户游戏账号
     *
     * @param
     * @return
     * @throws Exception
     */
    @PostMapping("/dbGame.json")
    public Result<String> dbGame(@RequestBody DbInfoVo vo,@LoginUser LoginInfo loginUser) {
        if (loginUser == null) {
            return Result.failed(MessageUtils.get("ParameterError"));
        }
//        if (LoginUserTypeEnum.isTrailAccount(loginUser.getLogintype())) {
//        	String dbGame1 = MessageUtils.get("dbGame1");
//            return Result.error(dbGame1);
//        }
//        if (LoginUserTypeEnum.isAnchor(loginUser.getLogintype())) {
//        	String dbGame2 = MessageUtils.get("dbGame2");
//            return Result.error(dbGame2);
//        }
        RedisLock lock = RedisLock.newRequestLock("DB_GAME_" + loginUser.getId(), 5000);
        try {
            if (lock.lock()) {
                String ip = "";
                Result<String> resultInfo = dbService.dbGame(loginUser, vo.getGameCode(), vo.getGameType());
                if (resultInfo == null) {
                    return Result.failed(MessageUtils.get("networktimeout"));
                } else {
                    if (!resultInfo.getCode().equals(ResultCode.SUCCESS)) {
                        return resultInfo;
                    }
                }
                logger.info("dblog {} game 游戏连接成功.  GameCode:{},ip:{}", loginUser.getId(), vo.getGameCode(), ip);
                // 异步初始化用户游戏账号
                dbService.initAccountInfo(loginUser, vo.getGameCode());
                return resultInfo;
            } else {
                logger.info("dblog {} game repeat request", loginUser.getId());
                return Result.success();
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
