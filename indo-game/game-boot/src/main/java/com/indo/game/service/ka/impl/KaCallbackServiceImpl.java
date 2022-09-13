package com.indo.game.service.ka.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.enums.GoldchangeEnum;
import com.indo.common.enums.TradingEnum;
import com.indo.common.utils.DateUtils;
import com.indo.core.mapper.game.TxnsMapper;
import com.indo.core.pojo.bo.MemTradingBO;
import com.indo.core.pojo.entity.game.GameCategory;
import com.indo.core.pojo.entity.game.GameParentPlatform;
import com.indo.core.pojo.entity.game.GamePlatform;
import com.indo.core.pojo.entity.game.Txns;
import com.indo.game.pojo.dto.ka.KACallbackCommonReq;
import com.indo.game.pojo.dto.ka.KACallbackCreditReq;
import com.indo.game.pojo.dto.ka.KACallbackPlayReq;
import com.indo.game.pojo.dto.ka.KACallbackRevokeReq;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.ka.KaCallbackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
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
    DecimalFormat format = new DecimalFormat("0");
    // 开始游戏账号验证
    @Override
    public Object startGame(KACallbackCommonReq commonReq, String ip) {
        logger.info("ka_startGame kaGame paramJson:{}, ip:{}", JSONObject.toJSONString(commonReq), ip);
        try {
            GameParentPlatform platformGameParent = getGameParentPlatform();

            // 校验IP
            if (checkIp(ip, platformGameParent)) {
                return initFailureResponse(4, "IP不在白名单");
            }

            // 查询玩家是否存在
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(commonReq.getPartnerPlayerId());
            if (null == memBaseinfo) {
                return initFailureResponse(100, "玩家不存在");
            }

            // HASH 验证

            // 会员余额返回
            JSONObject jsonObject = initSuccessResponse();
            jsonObject.put("balance", format.format(getMultiplyBalance(memBaseinfo.getBalance()).divide(platformGameParent.getCurrencyPro()).longValue()));
//            jsonObject.put("balance", memBaseinfo.getBalance());
            jsonObject.put("currency", platformGameParent.getCurrencyType());
            jsonObject.put("playerId", memBaseinfo.getAccount());
            jsonObject.put("sessionId", commonReq.getSessionId());
            return jsonObject;

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

            GameParentPlatform platformGameParent = getGameParentPlatform();

            // 校验IP
            if (checkIp(ip, platformGameParent)) {
                return initFailureResponse(4, "IP不在白名单");
            }

            // 查询玩家是否存在
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(kaCallbackPlayReq.getPartnerPlayerId());
            if (null == memBaseinfo) {
                return initFailureResponse(100, "玩家不存在");
            }

            // 会员余额
            BigDecimal balance = memBaseinfo.getBalance();
            // 下注金额
            BigDecimal betAmount = getDivideAmount(kaCallbackPlayReq.getBetAmount().multiply(platformGameParent.getCurrencyPro()));
            BigDecimal freeWinAmount = getDivideAmount(kaCallbackPlayReq.getWinAmount().multiply(platformGameParent.getCurrencyPro()));
            String method = "Place Bet";
            BigDecimal winningAmount = BigDecimal.ZERO;
            // 免费游戏
            if (kaCallbackPlayReq.getFreeGames()) {
                method = "Settle";
                winningAmount = freeWinAmount;
                balance = balance.add(freeWinAmount);
                // 更新玩家余额
                gameCommonService.updateUserBalance(memBaseinfo, freeWinAmount, GoldchangeEnum.SETTLE, TradingEnum.INCOME);
            } else {
                if (freeWinAmount.compareTo(BigDecimal.ZERO) == 0) {//下注
                    // 下注金额小于0
                    if (betAmount.compareTo(BigDecimal.ZERO) < 0) {
                        return initFailureResponse(201, "下注金额不能小0");
                    }

                    if (balance.compareTo(betAmount) < 0) {
                        return initFailureResponse(200, "玩家没有足够余额以进行当前下注");
                    }
                    winningAmount = betAmount.negate();
                    balance = balance.subtract(betAmount.abs());
                    gameCommonService.updateUserBalance(memBaseinfo, betAmount.abs(), GoldchangeEnum.PLACE_BET, TradingEnum.SPENDING);
                }else {
                    method = "Settle";
                    BigDecimal winAmount = freeWinAmount.subtract(betAmount);
                    winningAmount = freeWinAmount;
                    if (winAmount.compareTo(BigDecimal.ZERO) != 0) {
                        if (winAmount.compareTo(BigDecimal.ZERO) == 1) {
                            balance = balance.add(winAmount);
                            gameCommonService.updateUserBalance(memBaseinfo, winAmount, GoldchangeEnum.SETTLE, TradingEnum.INCOME);
                        } else {
                            balance = balance.subtract(winAmount.abs());
                            gameCommonService.updateUserBalance(memBaseinfo, winAmount.abs(), GoldchangeEnum.SETTLE, TradingEnum.SPENDING);
                        }
                    }
                }

            }
            // 查询用户请求订单
            Txns oldTxns = getTxns(kaCallbackPlayReq.getTransactionId(), memBaseinfo.getAccount(), kaCallbackPlayReq.getRound());
            if (null != oldTxns) {
                return initFailureResponse(201, "交易已存在");
            }
            GamePlatform gamePlatform;
            if("Y".equals(OpenAPIProperties.KA_IS_PLATFORM_LOGIN)){
                gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(platformGameParent.getPlatformCode(),platformGameParent.getPlatformCode());
            }else {
                gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(kaCallbackPlayReq.getGameId(), platformGameParent.getPlatformCode());

            }
            GameCategory gameCategory = gameCommonService.getGameCategoryById(gamePlatform.getCategoryId());

            Txns txns = new Txns();
            //游戏商注单号
            txns.setPlatformTxId(kaCallbackPlayReq.getTransactionId());

            //玩家 ID
            txns.setUserId(memBaseinfo.getAccount());
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
            txns.setWinningAmount(winningAmount);
            //中奖金额（赢为正数，亏为负数，和为0）或者总输赢
            txns.setWinAmount(winningAmount.abs());
            txns.setMethod(method);
            //玩家下注时间
            txns.setBetTime(DateUtils.formatByLong(kaCallbackPlayReq.getTimestamp(), DateUtils.newFormat));
            //真实下注金额,需增加在玩家的金额
            txns.setRealBetAmount(betAmount);
            //真实返还金额,游戏赢分
            txns.setRealWinAmount(freeWinAmount);
            if (!kaCallbackPlayReq.getFreeGames()) {
                //此交易是否是投注 true是投注 false 否
                txns.setBet(true);
                //赌注的结果 : 赢:0,输:1,平手:2
                int resultTyep;
                if (freeWinAmount.compareTo(BigDecimal.ZERO) == 0) {
                    resultTyep = 2;
                } else if (freeWinAmount.compareTo(BigDecimal.ZERO) > 0) {
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

            // 会员余额返回
            JSONObject jsonObject = initSuccessResponse();
            jsonObject.put("balance", format.format(getMultiplyBalance(balance).divide(platformGameParent.getCurrencyPro())));
//            jsonObject.put("balance", balance);
            jsonObject.put("currency", platformGameParent.getCurrencyType());
            jsonObject.put("playerId", memBaseinfo.getAccount());
            jsonObject.put("sessionId", kaCallbackPlayReq.getSessionId());
            return jsonObject;
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

            GameParentPlatform platformGameParent = getGameParentPlatform();

            // 校验IP
            if (checkIp(ip, platformGameParent)) {
                return initFailureResponse(4, "IP不在白名单");
            }

            // 查询玩家是否存在
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(kaCallbackCreditReq.getPartnerPlayerId());
            if (null == memBaseinfo) {
                return initFailureResponse(100, "玩家不存在");
            }

            // 会员余额
            BigDecimal balance = memBaseinfo.getBalance();
            // 派奖金额
            BigDecimal amount = getDivideAmount(kaCallbackCreditReq.getAmount().multiply(platformGameParent.getCurrencyPro()));
//            BigDecimal amount = kaCallbackCreditReq.getAmount();
            // 派彩金额小于0
            if (amount.compareTo(BigDecimal.ZERO) < 0) {
                return initFailureResponse(201, "派彩金额不能小0");
            }

            // 查询用户请求订单
            Txns oldTxns = getTxns(kaCallbackCreditReq.getTransactionId(), memBaseinfo.getAccount(), null);
            if (null != oldTxns) {
                return initFailureResponse(201, "交易已存在");
            }

            GamePlatform gamePlatform;
            if("Y".equals(OpenAPIProperties.KA_IS_PLATFORM_LOGIN)){
                gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(platformGameParent.getPlatformCode(),platformGameParent.getPlatformCode());
            }else {
                gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(kaCallbackCreditReq.getGameId(), platformGameParent.getPlatformCode());

            }
            GameCategory gameCategory = gameCommonService.getGameCategoryById(gamePlatform.getCategoryId());

            balance = balance.add(amount);
            gameCommonService.updateUserBalance(memBaseinfo, amount, GoldchangeEnum.ACTIVITY_GIVE, TradingEnum.INCOME);

            Txns txns = new Txns();
            //游戏商注单号
            txns.setPlatformTxId(kaCallbackCreditReq.getTransactionId());

            //玩家 ID
            txns.setUserId(memBaseinfo.getAccount());
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
            JSONObject jsonObject = initSuccessResponse();
            jsonObject.put("balance", format.format(getMultiplyBalance(balance).divide(platformGameParent.getCurrencyPro())));
//            jsonObject.put("balance", balance);
            jsonObject.put("currency", platformGameParent.getCurrencyType());
            jsonObject.put("playerId", memBaseinfo.getAccount());
            jsonObject.put("sessionId", kaCallbackCreditReq.getSessionId());
            return jsonObject;
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

            GameParentPlatform platformGameParent = getGameParentPlatform();

            // 校验IP
            if (checkIp(ip, platformGameParent)) {
                return initFailureResponse(4, "IP不在白名单");
            }

            // 查询玩家是否存在
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(kaCallbackRevokeReq.getPartnerPlayerId());
            if (null == memBaseinfo) {
                return initFailureResponse(100, "玩家不存在");
            }
            Long round = kaCallbackRevokeReq.getRound();
            // 查询用户请求订单
            Txns oldTxns = getTxns(kaCallbackRevokeReq.getTransactionId(), memBaseinfo.getAccount(), round);
            if (null == oldTxns) {
                return initFailureResponse(400, "该笔交易不存在");
            }

            // 如果订单已经取消
            if ("Cancel Bet".equals(oldTxns.getStatus())) {
                return initFailureResponse(401, "该笔交易不能注销");
            }
            BigDecimal balance = memBaseinfo.getBalance();
            // 会员余额
            BigDecimal cancelAmount = getDivideAmount(null!=kaCallbackRevokeReq.getWin()?kaCallbackRevokeReq.getWin().multiply(platformGameParent.getCurrencyPro()):BigDecimal.ZERO);
            BigDecimal betAmount = getDivideAmount(null!=kaCallbackRevokeReq.getBet()?kaCallbackRevokeReq.getBet().multiply(platformGameParent.getCurrencyPro()):BigDecimal.ZERO);
            if (cancelAmount.compareTo(BigDecimal.ZERO) != 0) {
                if (cancelAmount.compareTo(BigDecimal.ZERO) == 1) {
                    balance = balance.subtract(cancelAmount.abs());
                    gameCommonService.updateUserBalance(memBaseinfo, cancelAmount.abs(), GoldchangeEnum.UNSETTLE, TradingEnum.SPENDING);
                }else {
                    balance = balance.add(cancelAmount);
                    gameCommonService.updateUserBalance(memBaseinfo, cancelAmount, GoldchangeEnum.UNSETTLE, TradingEnum.INCOME);
                }
            }
            if (betAmount.compareTo(BigDecimal.ZERO) != 0&&0==round) {
                balance = balance.add(betAmount);
                gameCommonService.updateUserBalance(memBaseinfo, betAmount, GoldchangeEnum.CANCEL_BET, TradingEnum.INCOME);
            }
            String dateStr = DateUtils.format(new Date(), DateUtils.newFormat);

            Txns txns = new Txns();
            BeanUtils.copyProperties(oldTxns, txns);
            txns.setBalance(balance);
            txns.setId(null);
            txns.setBetAmount(betAmount);
            txns.setWinningAmount(cancelAmount);
            txns.setWinAmount(cancelAmount);
            txns.setStatus("Running");
            txns.setRealWinAmount(cancelAmount);//真实返还金额
            txns.setMethod("Cancel Bet");
            txns.setCreateTime(dateStr);
            txnsMapper.insert(txns);

            oldTxns.setStatus("Cancel Bet");
            oldTxns.setUpdateTime(dateStr);
            txnsMapper.updateById(oldTxns);

            JSONObject jsonObject = initSuccessResponse();
            jsonObject.put("balance", format.format(getMultiplyBalance(balance).divide(platformGameParent.getCurrencyPro())));
//            jsonObject.put("balance", balance);
            jsonObject.put("currency", platformGameParent.getCurrencyType());
            jsonObject.put("playerId", memBaseinfo.getAccount());
            jsonObject.put("sessionId", kaCallbackRevokeReq.getSessionId());
            return jsonObject;
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
            GameParentPlatform platformGameParent = getGameParentPlatform();
            // 校验IP
            if (checkIp(ip, platformGameParent)) {
                return initFailureResponse(4, "IP不在白名单");
            }

            // 查询玩家是否存在
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(kaCallbackCommonReq.getPartnerPlayerId());
            if (null == memBaseinfo) {
                return initFailureResponse(100, "玩家不存在");
            }

            // 会员余额返回
            JSONObject jsonObject = initSuccessResponse();
            jsonObject.put("balance", format.format(getMultiplyBalance(memBaseinfo.getBalance()).divide(platformGameParent.getCurrencyPro())));
//            jsonObject.put("balance", memBaseinfo.getBalance());
            jsonObject.put("currency", platformGameParent.getCurrencyType());
            jsonObject.put("playerId", memBaseinfo.getAccount());
            jsonObject.put("sessionId", kaCallbackCommonReq.getSessionId());
            return jsonObject;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return initFailureResponse(1, e.getMessage());
        }
    }

    // 结束,返回成功
    @Override
    public Object end(KACallbackCommonReq kaCallbackCommonReq, String ip) {
        logger.info("ka_end kaGame paramJson:{}, ip:{}", JSONObject.toJSONString(kaCallbackCommonReq), ip);
        GameParentPlatform gameParentPlatform = getGameParentPlatform();
        // 校验IP
        if (checkIp(ip, gameParentPlatform)) {
            return initFailureResponse(4, "IP不在白名单");
        }

        return initSuccessResponse();
    }

    /**
     * 获取历史订单
     *
     * @param reference 订单编号
     * @param round     回合(一般游戏为0,免费游戏有具体1.2.3...)
     * @return 订单
     */
    private Txns getTxns(String reference, String userAccount, Long round) {
        LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(c -> c.eq(Txns::getMethod, "Place Bet")
                .or().eq(Txns::getMethod, "Cancel Bet")
                .or().eq(Txns::getMethod, "Adjust Bet")
                .or().eq(Txns::getMethod, "Settle"));
        wrapper.eq(Txns::getStatus, "Running");
        wrapper.eq(Txns::getPlatformTxId, reference);
//        wrapper.eq(Txns::getUserId, userAccount);
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
    private BigDecimal getMultiplyBalance(BigDecimal balance) {
        return balance.multiply(BigDecimal.valueOf(100L));
    }

    /**
     * 传递金额转换
     *
     * @param amount 下注或者奖金
     * @return 除以100 转换货币
     */
    private BigDecimal getDivideAmount(BigDecimal amount) {
        return amount.divide(BigDecimal.valueOf(100L), 2, RoundingMode.HALF_UP);
    }

    /**
     * 查询IP是否被封
     *
     * @param ip ip
     * @return boolean
     */
    private boolean checkIp(String ip, GameParentPlatform platformGameParent) {
        if (null == platformGameParent) {
            return true;
        } else if (null == platformGameParent.getIpAddr() || "".equals(platformGameParent.getIpAddr())) {
            return false;
        }
        return !platformGameParent.getIpAddr().equals(ip);

    }

    /**
     * 初始化成功json返回
     *
     * @return JSONObject
     */
    private JSONObject initSuccessResponse() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("statusCode", 0);
        jsonObject.put("status", "success");
        return jsonObject;
    }

    /**
     * 初始化交互失败返回
     *
     * @param error       错误码
     * @param description 错误描述
     * @return JSONObject
     */
    private JSONObject initFailureResponse(Integer error, String description) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("statusCode", error);
        jsonObject.put("status", description);
        return jsonObject;
    }

    private GameParentPlatform getGameParentPlatform(){
        return gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.KA_PLATFORM_CODE);
    }

    public static void main(String[] args) {
        DecimalFormat format = new DecimalFormat("0");
        System.out.println(format.format(9990000000000123456789.00));
    }
}
