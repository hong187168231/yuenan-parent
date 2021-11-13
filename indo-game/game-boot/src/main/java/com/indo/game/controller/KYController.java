package com.indo.game.controller;

import com.indo.common.annotation.LoginUser;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.game.game.RedisLock;
import com.indo.common.result.Result;
import com.indo.common.result.ResultCode;
import com.indo.common.utils.i18n.MessageUtils;
import com.indo.game.pojo.vo.KYInfoVo;
import com.indo.game.services.ky.KYService;
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
@RequestMapping("/ky")
public class KYController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private KYService kyService;

    /**
     * 进去开源棋牌并初始化用户游戏账号
     *
     * @param
     * @return
     * @throws Exception
     */
    @PostMapping("/game.json")
    public Result<String> game(@RequestBody KYInfoVo vo,@LoginUser LoginInfo loginUser) {
        if (loginUser == null) {
            return Result.failed(MessageUtils.get("ParameterError"));
        }
//        if (LoginUserTypeEnum.isTrailAccount(loginUser.getLogintype())) {
//        	String kyGame1 = MessageUtils.get("kyGame1");
//            return Result.failed(kyGame1);
//        }
//        if (LoginUserTypeEnum.isAnchor(loginUser.getLogintype())) {
//        	String kyGame2 = MessageUtils.get("kyGame2");
//            return Result.failed(kyGame2);
//        }
        RedisLock lock = RedisLock.newRequestLock("KY_GAME_" + loginUser.getId(), 5000);
        try {
            if (lock.lock()) {
                String ip = "";
                Result<String> resultInfo = kyService.kyGame(loginUser, vo.getKindId(), ip);
                if (resultInfo == null) {
                    return Result.failed(MessageUtils.get("networktimeout"));
                } else {
                    if (!resultInfo.getCode().equals(ResultCode.SUCCESS)) {
                        return resultInfo;
                    }
                }

                logger.info("kylog {} game 开元游戏连接成功.  kindId:{},ip:{}", loginUser.getId(), vo.getKindId(), ip);
                // 异步初始化用户游戏账号
                kyService.initAccountInfo(loginUser, ip, vo.getKindId());
                return resultInfo;
            } else {
                logger.info("kylog {} game repeat request", loginUser.getId());
                return Result.success(null);
            }
        } catch (Exception e) {
            //这边获取得是HystrixRuntimeException
            e.printStackTrace();
            logger.error("kylog {} game occur error. params:{}", loginUser.getId(), e.getMessage());
            return Result.failed(MessageUtils.get("networktimeout"));
        } finally {
            lock.unlockWhenHoldTime();
        }
    }

}
