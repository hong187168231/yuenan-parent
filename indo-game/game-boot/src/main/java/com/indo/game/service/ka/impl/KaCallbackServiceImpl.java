package com.indo.game.service.ka.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.enums.GoldchangeEnum;
import com.indo.common.enums.TradingEnum;
import com.indo.common.utils.DateUtils;
import com.indo.game.mapper.TxnsMapper;
import com.indo.game.pojo.dto.ka.KAApiResponseData;
import com.indo.game.pojo.dto.ka.KACallbackCommonReq;
import com.indo.game.pojo.dto.ka.KACallbackCreditReq;
import com.indo.game.pojo.dto.ka.KACallbackPlayReq;
import com.indo.game.pojo.dto.ka.KACallbackRevokeReq;
import com.indo.game.pojo.dto.ka.KACallbackStartResp;
import com.indo.game.pojo.entity.manage.GameCategory;
import com.indo.game.pojo.entity.manage.GameParentPlatform;
import com.indo.game.pojo.entity.manage.GamePlatform;
import com.indo.game.pojo.entity.manage.Txns;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.ka.KaCallbackService;
import com.indo.user.pojo.bo.MemTradingBO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

/**
 * KA 游戏回调服务
 */
@Service
public class KaCallbackServiceImpl implements KaCallbackService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private GameCommonService gameCommonService;
    @Autowired
    private TxnsMapper txnsMapper;

    // 开始游戏账号验证
    @Override
    public Object startGame(KACallbackCommonReq commonReq, String ip) {
        logger.info("ka_startGame kaGame paramJson:{}, ip:{}", JSONObject.toJSONString(commonReq), ip);
        try {
            GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.KA_PLATFORM_CODE);

            // 校验IP
            if (!ip.equals(commonReq.getPlayerIp()) || !checkIp(ip, gameParentPlatform)) {
                return initFailureResponse(4, "玩家 IP 地址不匹配或玩家不存在");
            }

            // 查询玩家是否存在
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(commonReq.getPartnerPlayerId());
            if (null == memBaseinfo) {
                return initFailureResponse(100, "玩家不存在");
            }

            // HASH 验证

            // 会员余额返回
            KACallbackStartResp kaCallbackStartResp = new KACallbackStartResp();
            kaCallbackStartResp.setStatusCode(0);
            kaCallbackStartResp.setStatus("success");
            kaCallbackStartResp.setBalance(getMultiplyBalance(memBaseinfo.getBalance()));
            kaCallbackStartResp.setCurrency(gameParentPlatform.getCurrencyType());
            kaCallbackStartResp.setPlayerId(memBaseinfo.getAccount());
            kaCallbackStartResp.setSessionId(commonReq.getSessionId());
            return JSONObject.toJSONString(kaCallbackStartResp);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return initFailureResponse(1, e.getMessage());
        }
    }

    // 下注
    @Override
    public Object palyGame(KACallbackPlayReq kaCallbackPlayReq, String ip) {
        logger.info("ka_palyGame kaGame paramJson:{}, ip:{}", JSONObject.toJSONString(kaCallbackPlayReq), ip);
        try {

            GameParentPlatform platformGameParent = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.KA_PLATFORM_CODE);

            // 校验IP
            if (!ip.equals(kaCallbackPlayReq.getPlayerIp()) || !checkIp(ip, platformGameParent)) {
                return initFailureResponse(4, "玩家 IP 地址不匹配或玩家不存在");
            }

            // 查询玩家是否存在
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(kaCallbackPlayReq.getPartnerPlayerId());
            if (null == memBaseinfo) {
                return initFailureResponse(100, "玩家不存在");
            }

            // 会员余额
            BigDecimal balance = memBaseinfo.getBalance();
            // 下注金额
            BigDecimal betAmount = getDivideAmount(kaCallbackPlayReq.getBetAmount());
            BigDecimal freeWinAmount = getDivideAmount(kaCallbackPlayReq.getWinAmount());
            // 免费游戏
            if (kaCallbackPlayReq.getFreeGames()) {
                kaCallbackPlayReq.setBetAmount(0L);
                betAmount = BigDecimal.ZERO;
                if (freeWinAmount.compareTo(betAmount) > 0) {
                    // 更新玩家余额
                    gameCommonService.updateUserBalance(memBaseinfo, freeWinAmount, GoldchangeEnum.FREE_SPIN, TradingEnum.INCOME);
                }
            } else {
                // 下注金额小于0
                if (betAmount.compareTo(BigDecimal.ZERO) < 0) {
                    return initFailureResponse(201, "下注金额不能小0");
                }

                if (balance.compareTo(betAmount) < 0) {
                    return initFailureResponse(200, "玩家没有足够余额以进行当前下注");
                }
                balance = balance.subtract(betAmount);

                // 更新玩家余额
                gameCommonService.updateUserBalance(memBaseinfo, freeWinAmount.subtract(betAmount),
                        GoldchangeEnum.BETNSETTLE, TradingEnum.SPENDING);

            }
            balance = balance.add(freeWinAmount);

            // 查询用户请求订单
            Txns oldTxns = getTxns(kaCallbackPlayReq.getTransactionId(), memBaseinfo.getId(), kaCallbackPlayReq.getRound());
            if (null != oldTxns) {
                return initFailureResponse(201, "交易已存在");
            }


            GamePlatform gamePlatform = gameCommonService.getGamePlatformByplatformCode(kaCallbackPlayReq.getGameId());
            GameCategory gameCategory = gameCommonService.getGameCategoryById(gamePlatform.getCategoryId());

            Txns txns = new Txns();
            //游戏商注单号
            txns.setPlatformTxId(kaCallbackPlayReq.getTransactionId());

            //玩家 ID
            txns.setUserId(memBaseinfo.getId().toString());
            //玩家货币代码
            txns.setCurrency(platformGameParent.getCurrencyType());
//            txns.setOdds(kaCallbackPlayReq.getBetPerSelection());
            txns.setRoundId(kaCallbackPlayReq.getRound().toString());
            //平台代码
            txns.setPlatform(platformGameParent.getPlatformCode());
            //平台名称
            txns.setPlatformEnName(platformGameParent.getPlatformEnName());
            txns.setPlatformCnName(platformGameParent.getPlatformCnName());
            //平台游戏类型
            txns.setGameType(gameCategory.getGameType());
            //游戏分类ID
            txns.setCategoryId(gameCategory.getId());
            //游戏分类名称
            txns.setCategoryName(gameCategory.getGameName());
            //平台游戏代码
            txns.setGameCode(gamePlatform.getPlatformCode());
            //游戏名称
            txns.setGameName(gamePlatform.getPlatformEnName());
            //下注金额
            txns.setBetAmount(betAmount);
            //游戏平台的下注项目
            txns.setBetType(kaCallbackPlayReq.getGameId());
            //中奖金额（赢为正数，亏为负数，和为0）或者总输赢
            txns.setWinningAmount(freeWinAmount);
            //玩家下注时间
            txns.setBetTime(DateUtils.formatByLong(kaCallbackPlayReq.getTimestamp(), DateUtils.newFormat));
            //真实下注金额,需增加在玩家的金额
            txns.setRealBetAmount(betAmount);
            //真实返还金额,游戏赢分
            txns.setRealWinAmount(freeWinAmount);
            //返还金额 (包含下注金额)
            txns.setWinAmount(freeWinAmount);
            if (!kaCallbackPlayReq.getFreeGames()) {
                //此交易是否是投注 true是投注 false 否
                txns.setBet(true);
                //赌注的结果 : 赢:0,输:1,平手:2
                int resultTyep;
                if (freeWinAmount.compareTo(BigDecimal.ZERO) == 0) {
                    resultTyep = 2;
                } else if (freeWinAmount.compareTo(BigDecimal.ZERO) == 1) {
                    resultTyep = 0;
                } else {
                    resultTyep = 1;
                }
                txns.setResultType(resultTyep);
            }
            //有效投注金额 或 投注面值
            txns.setTurnover(betAmount);
            //辨认交易时间依据
            txns.setTxTime(DateUtils.formatByLong(kaCallbackPlayReq.getTimestamp(), DateUtils.newFormat));
            //操作名称
            txns.setMethod("Settle");
            txns.setStatus("Running");
            //余额
            txns.setBalance(balance);
            //创建时间
            String dateStr = DateUtils.format(new Date(), DateUtils.newFormat);
            txns.setCreateTime(dateStr);
            //投注 IP
            txns.setBetIp(ip);//  string 是 投注 IP
            int num = txnsMapper.insert(txns);
            if (num <= 0) {
                return initFailureResponse(1, "订单写入失败");
            }

            KACallbackStartResp kaCallbackStartResp = new KACallbackStartResp();
            kaCallbackStartResp.setStatusCode(0);
            kaCallbackStartResp.setStatus("success");
            kaCallbackStartResp.setBalance(getMultiplyBalance(balance));
            kaCallbackStartResp.setCurrency(platformGameParent.getCurrencyType());
            kaCallbackStartResp.setPlayerId(memBaseinfo.getAccount());
            kaCallbackStartResp.setSessionId(kaCallbackPlayReq.getSessionId());
            return JSONObject.toJSONString(kaCallbackStartResp);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return initFailureResponse(1, e.getMessage());
        }
    }

    // 游戏派彩
    @Override
    public Object credit(KACallbackCreditReq kaCallbackCreditReq, String ip) {
        logger.info("ka_credit kaGame paramJson:{}, ip:{}", JSONObject.toJSONString(kaCallbackCreditReq), ip);
        try {

            GameParentPlatform platformGameParent = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.KA_PLATFORM_CODE);

            // 校验IP
            if (!ip.equals(kaCallbackCreditReq.getPlayerIp()) || !checkIp(ip, platformGameParent)) {
                return initFailureResponse(4, "玩家 IP 地址不匹配或玩家不存在");
            }

            // 查询玩家是否存在
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(kaCallbackCreditReq.getPartnerPlayerId());
            if (null == memBaseinfo) {
                return initFailureResponse(100, "玩家不存在");
            }

            // 会员余额
            BigDecimal balance = memBaseinfo.getBalance();
            // 派奖金额
            BigDecimal amount = getDivideAmount(kaCallbackCreditReq.getAmount());
            // 派彩金额小于0
            if (amount.compareTo(BigDecimal.ZERO) < 0) {
                return initFailureResponse(201, "派彩金额不能小0");
            }

            // 查询用户请求订单
            Txns oldTxns = getTxns(kaCallbackCreditReq.getTransactionId(), memBaseinfo.getId(), null);
            if (null != oldTxns) {
                return initFailureResponse(201, "交易已存在");
            }

            GamePlatform gamePlatform = gameCommonService.getGamePlatformByplatformCode(kaCallbackCreditReq.getGameId());
            GameCategory gameCategory = gameCommonService.getGameCategoryById(gamePlatform.getCategoryId());

            balance = balance.add(amount);
            gameCommonService.updateUserBalance(memBaseinfo, amount, GoldchangeEnum.ACTIVITY_GIVE, TradingEnum.INCOME);

            Txns txns = new Txns();
            //游戏商注单号
            txns.setPlatformTxId(kaCallbackCreditReq.getTransactionId());

            //玩家 ID
            txns.setUserId(memBaseinfo.getId().toString());
            //玩家货币代码
            txns.setCurrency(platformGameParent.getCurrencyType());
//            txns.setOdds(kaCallbackPlayReq.getBetPerSelection());
//            txns.setRoundId(kaCallbackPlayReq.getRound().toString());
            //平台代码
            txns.setPlatform(platformGameParent.getPlatformCode());
            //平台名称
            txns.setPlatformEnName(platformGameParent.getPlatformEnName());
            txns.setPlatformCnName(platformGameParent.getPlatformCnName());
            //平台游戏类型
            txns.setGameType(gameCategory.getGameType());
            //游戏分类ID
            txns.setCategoryId(gameCategory.getId());
            //游戏分类名称
            txns.setCategoryName(gameCategory.getGameName());
            //平台游戏代码
            txns.setGameCode(gamePlatform.getPlatformCode());
            //游戏名称
            txns.setGameName(gamePlatform.getPlatformEnName());
            //下注金额
            txns.setBetAmount(BigDecimal.ZERO);
            //游戏平台的下注项目
            txns.setBetType(kaCallbackCreditReq.getGameId());
            //中奖金额（赢为正数，亏为负数，和为0）或者总输赢
            txns.setWinningAmount(amount);
            //玩家下注时间
            txns.setBetTime(DateUtils.formatByLong(kaCallbackCreditReq.getTimestamp(), DateUtils.newFormat));
            //真实下注金额,需增加在玩家的金额
            txns.setRealBetAmount(BigDecimal.ZERO);
            //真实返还金额,游戏赢分
            txns.setRealWinAmount(amount);
            //返还金额 (包含下注金额)
            txns.setWinAmount(amount);
            //有效投注金额 或 投注面值
            txns.setTurnover(BigDecimal.ZERO);
            //辨认交易时间依据
            txns.setTxTime(DateUtils.formatByLong(kaCallbackCreditReq.getTimestamp(), DateUtils.newFormat));
            //操作名称
            txns.setMethod("Settle");
            txns.setStatus("Running");
            //余额
            txns.setBalance(balance);
            //创建时间
            String dateStr = DateUtils.format(new Date(), DateUtils.newFormat);
            txns.setCreateTime(dateStr);
            //投注 IP
            txns.setBetIp(ip);//  string 是 投注 IP
            int num = txnsMapper.insert(txns);
            if (num <= 0) {
                return initFailureResponse(1, "订单写入失败");
            }

            KACallbackStartResp kaCallbackStartResp = new KACallbackStartResp();
            kaCallbackStartResp.setStatusCode(0);
            kaCallbackStartResp.setStatus("success");
            kaCallbackStartResp.setBalance(getMultiplyBalance(balance));
            kaCallbackStartResp.setCurrency(platformGameParent.getCurrencyType());
            kaCallbackStartResp.setPlayerId(memBaseinfo.getAccount());
            kaCallbackStartResp.setSessionId(kaCallbackCreditReq.getSessionId());
            return JSONObject.toJSONString(kaCallbackStartResp);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return initFailureResponse(1, e.getMessage());
        }
    }

    // 取消交易
    @Override
    public Object revoke(KACallbackRevokeReq kaCallbackRevokeReq, String ip) {
        logger.info("ka_revoke kaGame paramJson:{}, ip:{}", JSONObject.toJSONString(kaCallbackRevokeReq), ip);
        try {

            GameParentPlatform platformGameParent = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.KA_PLATFORM_CODE);

            // 校验IP
            if (!ip.equals(kaCallbackRevokeReq.getPlayerIp()) || !checkIp(ip, platformGameParent)) {
                return initFailureResponse(4, "玩家 IP 地址不匹配或玩家不存在");
            }

            // 查询玩家是否存在
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(kaCallbackRevokeReq.getPartnerPlayerId());
            if (null == memBaseinfo) {
                return initFailureResponse(100, "玩家不存在");
            }

            // 查询用户请求订单
            Txns oldTxns = getTxns(kaCallbackRevokeReq.getTransactionId(), memBaseinfo.getId(), kaCallbackRevokeReq.getRound());
            if (null == oldTxns) {
                return initFailureResponse(400, "该笔交易不存在");
            }

            // 如果订单已经取消
            if ("Cancel Bet".equals(oldTxns.getMethod())) {
                return initFailureResponse(401, "该笔交易不能注销");
            }

            // 会员余额
            BigDecimal cancelAmount = oldTxns.getBetAmount().subtract(oldTxns.getWinAmount());
            BigDecimal balance = memBaseinfo.getBalance().add(cancelAmount);
            gameCommonService.updateUserBalance(memBaseinfo, cancelAmount, GoldchangeEnum.CANCEL_BET, TradingEnum.INCOME);
            String dateStr = DateUtils.format(new Date(), DateUtils.newFormat);

            Txns txns = new Txns();
            BeanUtils.copyProperties(oldTxns, txns);
            txns.setBalance(balance);
            txns.setId(null);
            txns.setStatus("Running");
            txns.setRealWinAmount(cancelAmount);//真实返还金额
            txns.setMethod("Adjust Bet");
            txns.setCreateTime(dateStr);
            txnsMapper.insert(txns);

            oldTxns.setStatus("Cancel Bet");
            oldTxns.setUpdateTime(dateStr);
            txnsMapper.updateById(oldTxns);

            KACallbackStartResp kaCallbackStartResp = new KACallbackStartResp();
            kaCallbackStartResp.setStatusCode(0);
            kaCallbackStartResp.setStatus("success");
            kaCallbackStartResp.setBalance(getMultiplyBalance(balance));
            kaCallbackStartResp.setCurrency(platformGameParent.getCurrencyType());
            kaCallbackStartResp.setPlayerId(memBaseinfo.getAccount());
            kaCallbackStartResp.setSessionId(kaCallbackRevokeReq.getSessionId());
            return JSONObject.toJSONString(kaCallbackStartResp);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return initFailureResponse(1, e.getMessage());
        }
    }

    // 查询玩家平台余额
    @Override
    public Object balance(KACallbackCommonReq kaCallbackCommonReq, String ip) {
        logger.info("ka_balance kaGame paramJson:{}, ip:{}", JSONObject.toJSONString(kaCallbackCommonReq), ip);
        try {
            GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.KA_PLATFORM_CODE);
            // 校验IP
            if (!ip.equals(kaCallbackCommonReq.getPlayerIp()) || !checkIp(ip, gameParentPlatform)) {
                return initFailureResponse(4, "玩家 IP 地址不匹配或玩家不存在");
            }

            // 查询玩家是否存在
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(kaCallbackCommonReq.getPartnerPlayerId());
            if (null == memBaseinfo) {
                return initFailureResponse(100, "玩家不存在");
            }

            // 会员余额返回
            KACallbackStartResp kaCallbackStartResp = new KACallbackStartResp();
            kaCallbackStartResp.setStatusCode(0);
            kaCallbackStartResp.setStatus("success");
            kaCallbackStartResp.setBalance(getMultiplyBalance(memBaseinfo.getBalance()));
            kaCallbackStartResp.setCurrency(gameParentPlatform.getCurrencyType());
            kaCallbackStartResp.setPlayerId(memBaseinfo.getAccount());
            kaCallbackStartResp.setSessionId(kaCallbackCommonReq.getSessionId());
            return JSONObject.toJSONString(kaCallbackStartResp);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return initFailureResponse(1, e.getMessage());
        }
    }

    // 结束,返回成功
    @Override
    public Object end(KACallbackCommonReq kaCallbackCommonReq, String ip) {
        logger.info("ka_end kaGame paramJson:{}, ip:{}", JSONObject.toJSONString(kaCallbackCommonReq), ip);
        GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.KA_PLATFORM_CODE);
        // 校验IP
        if (!ip.equals(kaCallbackCommonReq.getPlayerIp()) || !checkIp(ip, gameParentPlatform)) {
            return initFailureResponse(4, "玩家 IP 地址不匹配或玩家不存在");
        }

        KAApiResponseData ppCommonResp = new KAApiResponseData();
        ppCommonResp.setStatusCode(0);
        ppCommonResp.setStatus("succcess");
        return JSONObject.toJSONString(ppCommonResp);
    }

    /**
     * 获取历史订单
     *
     * @param reference 订单编号
     * @param userId    会员ID
     * @param round     回合(一般游戏为0,免费游戏有具体1.2.3...)
     * @return 订单
     */
    private Txns getTxns(String reference, Long userId, Long round) {
        LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(c -> c.eq(Txns::getMethod, "Place Bet")
                .or().eq(Txns::getMethod, "Cancel Bet")
                .or().eq(Txns::getMethod, "Adjust Bet")
                .or().eq(Txns::getMethod, "Settle"));
        wrapper.eq(Txns::getStatus, "Running");
        wrapper.eq(Txns::getPlatformTxId, reference);
        wrapper.eq(Txns::getUserId, userId);
        if (null != round) {
            wrapper.eq(Txns::getRoundId, round);
        }
        return txnsMapper.selectOne(wrapper);
    }

    /**
     * 返回余额转换
     *
     * @param balance 余额
     * @return 乘以100 转换货币
     */
    private Long getMultiplyBalance(BigDecimal balance) {
        return balance.multiply(BigDecimal.valueOf(100L)).longValue();
    }

    /**
     * 传递金额转换
     *
     * @param amount 下注或者奖金
     * @return 除以100 转换货币
     */
    private BigDecimal getDivideAmount(Long amount) {
        if (0 == amount) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(amount).divide(BigDecimal.valueOf(100L));
    }

    /**
     * 查询IP是否被封
     *
     * @param ip ip
     * @return boolean
     */
    private boolean checkIp(String ip, GameParentPlatform gameParentPlatform) {
        if (null == gameParentPlatform) {
            return false;
        }
        if (null == gameParentPlatform.getIpAddr() || "".equals(gameParentPlatform.getIpAddr())) {
            return true;
        }

        return gameParentPlatform.getIpAddr().equals(ip);
    }

    /**
     * 初始化交互失败返回
     *
     * @param error       错误码
     * @param description 错误描述
     * @return String
     */
    private String initFailureResponse(Integer error, String description) {
        KAApiResponseData ppCommonResp = new KAApiResponseData();
        ppCommonResp.setStatusCode(error);
        ppCommonResp.setStatus(description);
        return JSONObject.toJSONString(ppCommonResp);
    }
}
