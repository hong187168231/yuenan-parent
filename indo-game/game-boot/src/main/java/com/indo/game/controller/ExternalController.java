package com.indo.game.controller;

import com.indo.common.annotation.LoginUser;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.common.utils.i18n.MessageUtils;
import com.indo.game.services.ExternalGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/external")
public class ExternalController{

    @Autowired
    private ExternalGameService externalGameService;

    /**
     * 第三方游戏下分公共接口
     */
    @PostMapping("/exit.json")
    public Result<BigDecimal> exit(@LoginUser LoginInfo loginUser) {

        if (loginUser == null) {
            return Result.failed(MessageUtils.get("ParameterError"));
        }
        String ip = "";

        return externalGameService.exit(loginUser, ip);
    }
}

//package com.caipiao.live.modules.admin.external;
//
//
//import com.caipiao.live.common.BaseController;
//import com.caipiao.live.common.enums.GoldchangeEnum;
//import com.caipiao.live.common.model.LoginUser;
//import com.caipiao.live.common.model.common.ResultInfo;
//import com.caipiao.live.common.mybatis.entity.MemBaseinfo;
//import com.caipiao.live.common.mybatis.mapperbean.OrderMapper;
//import com.caipiao.live.common.rest.write.external.AeWriteServiceRest;
//import com.caipiao.live.common.rest.write.external.AgWriteServiceRest;
//import com.caipiao.live.common.rest.write.external.DbWriteServiceRest;
//import com.caipiao.live.common.rest.write.external.ESGameWriteServiceRest;
//import com.caipiao.live.common.rest.write.external.KYWriteServiceRest;
//import com.caipiao.live.common.rest.write.external.MgWriteServiceRest;
//import com.caipiao.live.common.service.member.MemBaseinfoService;
//import com.caipiao.live.common.util.redis.RedisBusinessUtil;
//import com.caipiao.live.common.util.redis.RedisLock;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.math.BigDecimal;
//
//@RestController
//@RequestMapping("/external")
//public class ExternalController extends BaseController {
//    private final Logger logger = LoggerFactory.getLogger(this.getClass());
//    @Autowired
//    private AeWriteServiceRest aeWriteServiceRest;
//    @Autowired
//    private ESGameWriteServiceRest esGameWriteServiceRest;
//    @Autowired
//    private KYWriteServiceRest kyWriteServiceRest;
//    @Autowired
//    private OrderMapper orderMapper;
//    @Autowired
//    private MemBaseinfoService memBaseinfoService;
//    @Autowired
//    private AgWriteServiceRest agWriteServiceRest;
//    @Autowired
//    private MgWriteServiceRest mgWriteServiceRest;
//    @Autowired
//    private DbWriteServiceRest dbWriteServiceRest;
//
//    /**
//     * 第三方游戏下分公共接口
//     *
//     * @return
//     * @throws Exception
//     */
//    @PostMapping("/exit.json")
//    public ResultInfo<BigDecimal> exit() {
//        LoginUser loginUser = getAppLoginUser();
//        if (loginUser == null) {
//            return ResultInfo.paramsError();
//        }
//        String ip = getIpAddress();
//        RedisLock lock = RedisLock.newRequestLock("EXTERNAL_EXIT_" + loginUser.getMemid());
//        try {
//            if (lock.lock()) {
//                /** 42 -- 电竞  需要修改
//                 * 38-- AG
//                 * 40 -- 棋牌
//                 * 51 -- AE*/
//                long start = System.currentTimeMillis();
//                logger.info("exit{} 开始毫秒", start);
//                RedisBusinessUtil.deleteAppMember(loginUser.getMemid());
//                Integer lastInGameType = orderMapper.getLastInExternalGameType(loginUser.getAccno());
//                if (lastInGameType != null) {
//                    // ag 下分
//                    if (lastInGameType.equals(GoldchangeEnum.AG_OUT.getValue())) {
//                        ResultInfo<String> resultInfoAG = agWriteServiceRest.exit(loginUser, ip);
//                        logger.info("exit api resultInfoAG {}", resultInfoAG);
//                        logger.info("exit agWriteServiceRest{}毫秒", (System.currentTimeMillis() - start));
//                    }
//                    // 开元棋牌下分
//                    if (lastInGameType.equals(GoldchangeEnum.KY_OUT.getValue())) {
//                        ResultInfo<String> resultInfoKY = kyWriteServiceRest.exit(loginUser, ip);
//                        logger.info("exit api resultInfoKY {}", resultInfoKY);
//                        logger.info("exit kyWriteServiceRest{}毫秒", (System.currentTimeMillis() - start));
//                    }
//                    // 电竞棋牌下分
//                    if (lastInGameType.equals(GoldchangeEnum.ES_OUT.getValue())) {
//                        ResultInfo<String> resultInfoES = esGameWriteServiceRest.exit(loginUser, ip);
//                        logger.info("exit api resultInfoES {}", resultInfoES);
//                        logger.info("exit esGameWriteServiceRest{}毫秒", (System.currentTimeMillis() - start));
//                    }
//
//                    // AE棋牌下分
//                    if (lastInGameType.equals(GoldchangeEnum.AE_OUT.getValue())) {
//                        ResultInfo<String> resultInfoES = aeWriteServiceRest.aeSignOut(loginUser, ip);
//                        logger.info("exit api resultInfoES {}", resultInfoES);
//                        logger.info("exit aeWriteServiceRest{}毫秒", (System.currentTimeMillis() - start));
//                    }
//
//                    if (lastInGameType.equals(GoldchangeEnum.MG_OUT.getValue())) {
//                        ResultInfo<String> resultInfoES = mgWriteServiceRest.mgExit(loginUser, ip);
//                        logger.info("exit api resultInfoES {}", resultInfoES);
//                        logger.info("mgWriteServiceRest{}毫秒", (System.currentTimeMillis() - start));
//                    }
//
//                    if (lastInGameType.equals(GoldchangeEnum.DB_OUT.getValue())) {
//                        ResultInfo<String> resultInfoES = dbWriteServiceRest.dbExit(loginUser, ip);
//                        logger.info("exit api resultInfoES {}", resultInfoES);
//                        logger.info("exit dbWriteServiceRest{}毫秒", (System.currentTimeMillis() - start));
//                    }
//                }
//                MemBaseinfo memBaseinfo = memBaseinfoService.selectById(loginUser.getMemid());
//                BigDecimal mebBalance = BigDecimal.ZERO;
//                if (null != memBaseinfo) {
//                    mebBalance = memBaseinfo.getGoldnum();
//                }
//                logger.info("exit{} 结束毫秒", (System.currentTimeMillis() - start));
//                return ResultInfo.ok(mebBalance);
//            } else {
//                logger.info("external exit repeat request");
//                return ResultInfo.ok(null);
//            }
//        } catch (Exception e) {
//            logger.error("external.exit occur error. params:{}", e);
//            return ResultInfo.timeoutError();
//        } finally {
//            lock.unlockWhenHoldTime();
//        }
//    }
//}
