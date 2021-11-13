package com.indo.game.services.impl;

import com.indo.common.pojo.bo.LoginInfo;
import com.indo.game.game.RedisBusinessUtil;
import com.indo.game.game.RedisLock;
import com.indo.common.result.Result;
import com.indo.common.utils.i18n.MessageUtils;
import com.indo.game.common.enums.GoldchangeEnum;
import com.indo.game.mapper.OrderMapper;
import com.indo.game.services.ExternalGameService;
import com.indo.game.services.MemBaseinfoService;
import com.indo.game.services.ae.AeService;
import com.indo.game.services.ag.AgService;
import com.indo.game.services.db.DbService;
import com.indo.game.services.es.ESGameService;
import com.indo.game.services.ky.KYService;
import com.indo.game.services.mg.MgService;
import com.indo.user.pojo.entity.MemBaseinfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ExternalGameServiceImpl implements ExternalGameService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AeService aeService;
    @Autowired
    private ESGameService esGameService;
    @Autowired
    private KYService kyService;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private MemBaseinfoService memBaseinfoService;
    @Autowired
    private AgService agService;
    @Autowired
    private MgService mgService;
    @Autowired
    private DbService dbService;

    /**
     * 第三方游戏下分公共接口
     */
    @Override
    public Result<BigDecimal> exit(LoginInfo loginUser, String ip) {

        RedisLock lock = RedisLock.newRequestLock("EXTERNAL_EXIT_" + loginUser.getId());
        try {
            if (lock.lock()) {
                /** 42 -- 电竞  需要修改
                 * 38-- AG
                 * 40 -- 棋牌
                 * 51 -- AE*/
                long start = System.currentTimeMillis();
                logger.info("exit{} 开始毫秒", start);
                RedisBusinessUtil.deleteAppMember(loginUser.getId());
                Integer lastInGameType = orderMapper.getLastInExternalGameType(loginUser.getAccount());
                if (lastInGameType != null) {
                    // ag 下分
                    if (lastInGameType.equals(GoldchangeEnum.AG_OUT.getValue())) {
                        Result<String> resultInfoAG = agService.exit(loginUser, ip);
                        logger.info("exit api resultInfoAG {}", resultInfoAG);
                        logger.info("exit agService{}毫秒", (System.currentTimeMillis() - start));
                    }
                    // 开元棋牌下分
                    if (lastInGameType.equals(GoldchangeEnum.KY_OUT.getValue())) {
                        Result<String> resultInfoKY = kyService.exit(loginUser, ip);
                        logger.info("exit api resultInfoKY {}", resultInfoKY);
                        logger.info("exit kyService{}毫秒", (System.currentTimeMillis() - start));
                    }
                    // 电竞棋牌下分
                    if (lastInGameType.equals(GoldchangeEnum.ES_OUT.getValue())) {
                        Result<String> resultInfoES = esGameService.exit(loginUser, ip);
                        logger.info("exit api resultInfoES {}", resultInfoES);
                        logger.info("exit esGameService{}毫秒", (System.currentTimeMillis() - start));
                    }

                    // AE棋牌下分
                    if (lastInGameType.equals(GoldchangeEnum.AE_OUT.getValue())) {
                        Result<String> resultInfoES = aeService.aeSignOut(loginUser, ip);
                        logger.info("exit api resultInfoES {}", resultInfoES);
                        logger.info("exit aeService{}毫秒", (System.currentTimeMillis() - start));
                    }

                    if (lastInGameType.equals(GoldchangeEnum.MG_OUT.getValue())) {
                        Result<String> resultInfoES = mgService.mgExit(loginUser, ip);
                        logger.info("exit api resultInfoES {}", resultInfoES);
                        logger.info("mgService{}毫秒", (System.currentTimeMillis() - start));
                    }

                    if (lastInGameType.equals(GoldchangeEnum.DB_OUT.getValue())) {
                        Result<String> resultInfoES = dbService.dbExit(loginUser, ip);
                        logger.info("exit api resultInfoES {}", resultInfoES);
                        logger.info("exit dbService{}毫秒", (System.currentTimeMillis() - start));
                    }
                }
                MemBaseinfo memBaseinfo = memBaseinfoService.selectById(loginUser.getId());
                BigDecimal mebBalance = BigDecimal.ZERO;
                if (null != memBaseinfo) {
                    mebBalance = BigDecimal.valueOf(memBaseinfo.getBalance());
                }
                logger.info("exit{} 结束毫秒", (System.currentTimeMillis() - start));
                return Result.success(mebBalance);
            } else {
                logger.info("external exit repeat request");
                return Result.success();
            }
        } catch (Exception e) {
            logger.error("external.exit occur error. params:{}", e);
            return Result.failed(MessageUtils.get("networktimeout"));
        } finally {
            lock.unlockWhenHoldTime();
        }
    }

}
