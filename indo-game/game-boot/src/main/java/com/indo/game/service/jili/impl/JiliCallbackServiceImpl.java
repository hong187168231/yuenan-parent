package com.indo.game.service.jili.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.enums.GoldchangeEnum;
import com.indo.common.enums.TradingEnum;
import com.indo.common.utils.DateUtils;
import com.indo.common.utils.StringUtils;
import com.indo.core.mapper.game.TxnsMapper;
import com.indo.game.pojo.dto.jili.JiliCallbackBetReq;
import com.indo.game.pojo.dto.jili.JiliCallbackSessionBetReq;
import com.indo.game.pojo.entity.CptOpenMember;
import com.indo.core.pojo.entity.game.GameCategory;
import com.indo.core.pojo.entity.game.GameParentPlatform;
import com.indo.core.pojo.entity.game.GamePlatform;
import com.indo.core.pojo.entity.game.Txns;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.cptopenmember.CptOpenMemberService;
import com.indo.game.service.jili.JiliCallbackService;
import com.indo.core.pojo.bo.MemTradingBO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

@Service
public class JiliCallbackServiceImpl implements JiliCallbackService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private GameCommonService gameCommonService;
    @Autowired
    private CptOpenMemberService externalService;
    @Autowired
    private TxnsMapper txnsMapper;


    @Override
    public Object auth(String token, String ip) {
        logger.info("jili_auth JiliGame paramJson:{}, ip:{}", token, ip);

        try {
            GameParentPlatform platformGameParent = getGameParentPlatform();
            // 校验IP
            if (checkIp(ip, platformGameParent)) {
                return initFailureResponse(5, "非信任來源IP");
            }
            String ppPlatformCode = OpenAPIProperties.JILI_PLATFORM_CODE;
            CptOpenMember cptOpenMember = externalService.quertCptOpenMember(token, ppPlatformCode);
            if (null == cptOpenMember) {
                return initFailureResponse(4, "Api access token 已过期或无效");
            }
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(cptOpenMember.getUserName());
            if (null == cptOpenMember) {
                return initFailureResponse(4, "Api access token 已过期或无效");
            }
            JSONObject jsonObject = initSuccessResponse();
            jsonObject.put("username", cptOpenMember.getUserName());
            jsonObject.put("balance", memBaseinfo.getBalance().divide(platformGameParent.getCurrencyPro()));
            jsonObject.put("currency", platformGameParent.getCurrencyType());
            return jsonObject;

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return initFailureResponse(5, e.getMessage());
        }

    }

    @Override
    public Object bet(JiliCallbackBetReq jiliCallbackBetReq, String ip) {
        logger.info("jili_bet JiliGame paramJson:{}, ip:{}", JSONObject.toJSONString(jiliCallbackBetReq), ip);

        GameParentPlatform platformGameParent = getGameParentPlatform();
        // 校验IP
        if (checkIp(ip, platformGameParent)) {
            return initFailureResponse(5, "非信任來源IP");
        }
        String paySerialno = jiliCallbackBetReq.getReqId();
        String ppPlatformCode = OpenAPIProperties.JILI_PLATFORM_CODE;
        CptOpenMember cptOpenMember = externalService.quertCptOpenMember(jiliCallbackBetReq.getToken(), ppPlatformCode);

        if (StringUtils.isBlank(paySerialno)) {
            return initFailureResponse(3, "交易序号不能为空");
        }

        try {
            GamePlatform gamePlatform = gameCommonService.getGamePlatformByplatformCode(jiliCallbackBetReq.getGame().toString());
            GameCategory gameCategory = gameCommonService.getGameCategoryById(gamePlatform.getCategoryId());

            // 查询订单
            Txns oldTxns = getTxns(platformGameParent, paySerialno);
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(cptOpenMember.getUserName());
            if (null == memBaseinfo) {
                return initFailureResponse(4, "Api access token 已过期或无效");
            }
            BigDecimal balance = memBaseinfo.getBalance();
            BigDecimal amount = null!=jiliCallbackBetReq.getBetAmount()?jiliCallbackBetReq.getBetAmount().multiply(platformGameParent.getCurrencyPro()):BigDecimal.ZERO;
            BigDecimal winloseAmount = null!=jiliCallbackBetReq.getWinloseAmount()?jiliCallbackBetReq.getWinloseAmount().multiply(platformGameParent.getCurrencyPro()):BigDecimal.ZERO;

            // 重复订单
            if (null != oldTxns) {
                return initFailureResponse(1, "该注单已承认");
            }
            if (memBaseinfo.getBalance().compareTo(amount) < 0) {
                return initFailureResponse(2, "玩家余额不足");
            }

            balance = balance.subtract(amount).add(winloseAmount);
            if (amount.compareTo(winloseAmount) < 0) {
                // 更新余额
                gameCommonService.updateUserBalance(
                        memBaseinfo, winloseAmount.subtract(amount), GoldchangeEnum.BETNSETTLE, TradingEnum.INCOME);
            } else {
                // 更新余额
                gameCommonService.updateUserBalance(
                        memBaseinfo, amount.subtract(winloseAmount), GoldchangeEnum.BETNSETTLE, TradingEnum.SPENDING);
            }

            Txns txns = new Txns();
            //游戏商注单号
            txns.setPlatformTxId(paySerialno);

            //玩家 ID
            txns.setUserId(memBaseinfo.getId().toString());
            //玩家货币代码
            txns.setCurrency(jiliCallbackBetReq.getCurrency());
//            txns.setOdds(kaCallbackPlayReq.getBetPerSelection());
            txns.setRoundId(jiliCallbackBetReq.getRound());
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
            txns.setBetAmount(amount);
            //游戏平台的下注项目
            txns.setBetType(jiliCallbackBetReq.getGame().toString());
            //中奖金额（赢为正数，亏为负数，和为0）或者总输赢
            txns.setWinningAmount(winloseAmount);
            //玩家下注时间
            txns.setBetTime(DateUtils.formatByLong(jiliCallbackBetReq.getWagersTime(), DateUtils.newFormat));
            //真实下注金额,需增加在玩家的金额
            txns.setRealBetAmount(amount);
            //真实返还金额,游戏赢分
            txns.setRealWinAmount(winloseAmount);
            //此交易是否是投注 true是投注 false 否
            txns.setBet(true);
            //返还金额 (包含下注金额)
            txns.setWinAmount(winloseAmount);
            //赌注的结果 : 赢:0,输:1,平手:2
            int resultTyep;
            if (winloseAmount.compareTo(BigDecimal.ZERO) == 0) {
                resultTyep = 2;
            } else if (winloseAmount.compareTo(BigDecimal.ZERO) > 0) {
                resultTyep = 0;
            } else {
                resultTyep = 1;
            }
            txns.setResultType(resultTyep);

            //有效投注金额 或 投注面值
            txns.setTurnover(amount);
            //辨认交易时间依据
            txns.setTxTime(DateUtils.formatByLong(jiliCallbackBetReq.getWagersTime(), DateUtils.newFormat));
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
                return initFailureResponse(5, "订单写入失败");
            }

            JSONObject jsonObject = initSuccessResponse();
            jsonObject.put("username", cptOpenMember.getUserName());
            jsonObject.put("balance", balance.divide(platformGameParent.getCurrencyPro()));
            jsonObject.put("currency", platformGameParent.getCurrencyType());
            return jsonObject;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return initFailureResponse(5, e.getMessage());
        }

    }

    @Override
    public Object cancelBet(JiliCallbackBetReq jiliCallbackBetReq, String ip) {
        logger.info("jili_cancelBet JiliGame paramJson:{}, ip:{}", JSONObject.toJSONString(jiliCallbackBetReq), ip);
        try {
            GameParentPlatform platformGameParent = getGameParentPlatform();
            // 校验IP
            if (checkIp(ip, platformGameParent)) {
                return initFailureResponse(5, "非信任來源IP");
            }
            String paySerialno = jiliCallbackBetReq.getReqId();
            if (StringUtils.isBlank(paySerialno)) {
                return initFailureResponse(3, "交易序号不能为空");
            }

            // 查询订单
            Txns oldTxns = getTxns(platformGameParent, paySerialno);
            if (null == oldTxns) {
                return initFailureResponse(2, "注单无效");
            }

            // 如果订单已经取消
            if ("Cancel Bet".equals(oldTxns.getStatus())) {
                return initFailureResponse(1, "该注单已取消");
            }

            CptOpenMember cptOpenMember
                    = externalService.getCptOpenMember(Integer.parseInt(oldTxns.getUserId()), platformGameParent.getPlatformCnName());
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(cptOpenMember.getUserName());

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
            txns.setMethod("Cancel Bet");
            txns.setCreateTime(dateStr);
            txnsMapper.insert(txns);

            oldTxns.setStatus("Cancel Bet");
            oldTxns.setUpdateTime(dateStr);
            txnsMapper.updateById(oldTxns);

            JSONObject jsonObject = initSuccessResponse();
            jsonObject.put("username", cptOpenMember.getUserName());
            jsonObject.put("balance", balance.divide(platformGameParent.getCurrencyPro()));
            jsonObject.put("currency", platformGameParent.getCurrencyType());
            return jsonObject;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return initFailureResponse(5, e.getMessage());
        }
    }

    @Override
    public Object sessionBet(JiliCallbackSessionBetReq jiliCallbackSessionBetReq, String ip) {
        logger.info("jili_sessionBet JiliGame paramJson:{}, ip:{}", JSONObject.toJSONString(jiliCallbackSessionBetReq), ip);
        GameParentPlatform platformGameParent = getGameParentPlatform();
        // 校验IP
        if (checkIp(ip, platformGameParent)) {
            return initFailureResponse(5, "非信任來源IP");
        }
        String paySerialno = jiliCallbackSessionBetReq.getReqId();
        String ppPlatformCode = OpenAPIProperties.JILI_PLATFORM_CODE;
        CptOpenMember cptOpenMember = externalService.quertCptOpenMember(jiliCallbackSessionBetReq.getToken(), ppPlatformCode);
        if (null == cptOpenMember) {
            return initFailureResponse(4, "Api access token 已过期或无效");
        }
        String userName = cptOpenMember.getUserName();

        if (StringUtils.isBlank(paySerialno)) {
            return initFailureResponse(3, "交易序号不能为空");
        }

        try {
            GamePlatform gamePlatform = gameCommonService.getGamePlatformByplatformCode(jiliCallbackSessionBetReq.getGame().toString());
            GameCategory gameCategory = gameCommonService.getGameCategoryById(gamePlatform.getCategoryId());

            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(userName);
            if (null == memBaseinfo) {
                return initFailureResponse(4, "Api access token 已过期或无效");
            }
            BigDecimal balance = memBaseinfo.getBalance();
            BigDecimal betAmount = null!=jiliCallbackSessionBetReq.getBetAmount()?jiliCallbackSessionBetReq.getBetAmount().multiply(platformGameParent.getCurrencyPro()):BigDecimal.ZERO;
            BigDecimal jpContribute = null!=jiliCallbackSessionBetReq.getJpContribute()?jiliCallbackSessionBetReq.getJpContribute().multiply(platformGameParent.getCurrencyPro()):BigDecimal.ZERO;
            BigDecimal preserve = null!=jiliCallbackSessionBetReq.getPreserve()?jiliCallbackSessionBetReq.getPreserve().multiply(platformGameParent.getCurrencyPro()):BigDecimal.ZERO;
            BigDecimal getWinloseAmount = null!=jiliCallbackSessionBetReq.getWinloseAmount()?jiliCallbackSessionBetReq.getWinloseAmount().multiply(platformGameParent.getCurrencyPro()):BigDecimal.ZERO;
            BigDecimal jpWin = null!=jiliCallbackSessionBetReq.getJpWin()?jiliCallbackSessionBetReq.getJpWin().multiply(platformGameParent.getCurrencyPro()):BigDecimal.ZERO;
            BigDecimal amount = addBig(betAmount, jpContribute);
            amount = addBig(amount, preserve);
            BigDecimal winloseAmount = addBig(getWinloseAmount, jpWin);

            // 查询订单
            Txns oldTxns = getTxns(platformGameParent, paySerialno);
            // 1 下注  2 结算
            if (1 == jiliCallbackSessionBetReq.getType()) {
                if (null != oldTxns) {
                    return initFailureResponse(1, "该注单已承认");
                }
                if (memBaseinfo.getBalance().compareTo(amount) < 0) {
                    return initFailureResponse(2, "玩家余额不足");
                }
                // 更新余额
                gameCommonService.updateUserBalance(memBaseinfo, amount, GoldchangeEnum.PLACE_BET, TradingEnum.SPENDING);
                balance = balance.subtract(amount);
            } else {
                if (null == oldTxns) {
                    return initFailureResponse(5, "注单不存在");
                }
                // 更新余额
                gameCommonService.updateUserBalance(memBaseinfo, winloseAmount, GoldchangeEnum.SETTLE, TradingEnum.INCOME);
                balance = balance.add(winloseAmount);
            }

            if (1 == jiliCallbackSessionBetReq.getType()) {

                Txns txns = new Txns();
                //游戏商注单号
                txns.setPlatformTxId(paySerialno);

                //玩家 ID
                txns.setUserId(memBaseinfo.getId().toString());
                //玩家货币代码
                txns.setCurrency(jiliCallbackSessionBetReq.getCurrency());
//            txns.setOdds(kaCallbackPlayReq.getBetPerSelection());
                txns.setRoundId(jiliCallbackSessionBetReq.getRound());
                txns.setGameInfo(jiliCallbackSessionBetReq.getSessionId().toString());
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
                txns.setBetAmount(amount);
                //游戏平台的下注项目
                txns.setBetType(jiliCallbackSessionBetReq.getGame().toString());
                //中奖金额（赢为正数，亏为负数，和为0）或者总输赢
                txns.setWinningAmount(winloseAmount);
                //玩家下注时间
                txns.setBetTime(DateUtils.formatByLong(jiliCallbackSessionBetReq.getWagersTime(), DateUtils.newFormat));
                //真实下注金额,需增加在玩家的金额
                txns.setRealBetAmount(amount);
                //真实返还金额,游戏赢分
                txns.setRealWinAmount(winloseAmount);
                //此交易是否是投注 true是投注 false 否
                txns.setBet(true);
                //返还金额 (包含下注金额)
                txns.setWinAmount(winloseAmount);

                //有效投注金额 或 投注面值
                txns.setTurnover(amount);
                //辨认交易时间依据
                txns.setTxTime(DateUtils.formatByLong(jiliCallbackSessionBetReq.getWagersTime(), DateUtils.newFormat));
                //操作名称
                txns.setMethod("Place Bet");
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
                    return initFailureResponse(5, "订单写入失败");
                }
            } else if (null != oldTxns) {
                //真实返还金额,游戏赢分
                oldTxns.setRealWinAmount(winloseAmount);
                //返还金额 (包含下注金额)
                oldTxns.setWinAmount(winloseAmount);
                //中奖金额（赢为正数，亏为负数，和为0）或者总输赢
                oldTxns.setWinningAmount(winloseAmount);
                //赌注的结果 : 赢:0,输:1,平手:2
                int resultTyep;
                if (winloseAmount.compareTo(BigDecimal.ZERO) == 0) {
                    resultTyep = 2;
                } else if (winloseAmount.compareTo(BigDecimal.ZERO) > 0) {
                    resultTyep = 0;
                } else {
                    resultTyep = 1;
                }
                oldTxns.setResultType(resultTyep);
                oldTxns.setStatus("Settle");
                oldTxns.setUpdateTime(DateUtils.formatByLong(jiliCallbackSessionBetReq.getWagersTime(), DateUtils.newFormat));
                txnsMapper.updateById(oldTxns);
            }

            JSONObject jsonObject = initSuccessResponse();
            jsonObject.put("username", userName);
            jsonObject.put("balance", balance.divide(platformGameParent.getCurrencyPro()));
            jsonObject.put("currency", platformGameParent.getCurrencyType());
            return jsonObject;

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return initFailureResponse(5, e.getMessage());
        }
    }

    @Override
    public Object cancelSessionBet(JiliCallbackSessionBetReq jiliCallbackSessionBetReq, String ip) {
        logger.info("jili_cancelSessionBet JiliGame paramJson:{}, ip:{}", JSONObject.toJSONString(jiliCallbackSessionBetReq), ip);
        try {
            GameParentPlatform platformGameParent = getGameParentPlatform();
            // 校验IP
            if (checkIp(ip, platformGameParent)) {
                return initFailureResponse(5, "非信任來源IP");
            }
            String paySerialno = jiliCallbackSessionBetReq.getReqId();
            if (StringUtils.isBlank(paySerialno)) {
                return initFailureResponse(3, "交易序号不能为空");
            }

            // 查询订单
            Txns oldTxns = getTxns(platformGameParent, paySerialno);
            if (null == oldTxns) {
                return initFailureResponse(2, "注单无效");
            }

            // 如果订单已经取消
            if ("Cancel Bet".equals(oldTxns.getStatus())) {
                return initFailureResponse(1, "该注单已取消");
            }

            CptOpenMember cptOpenMember
                    = externalService.getCptOpenMember(Integer.parseInt(oldTxns.getUserId()), platformGameParent.getPlatformCnName());
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(cptOpenMember.getUserName());

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

            JSONObject jsonObject = initSuccessResponse();
            jsonObject.put("username", cptOpenMember.getUserName());
            jsonObject.put("balance", balance.divide(platformGameParent.getCurrencyPro()));
            jsonObject.put("currency", platformGameParent.getCurrencyType());
            return jsonObject;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return initFailureResponse(5, e.getMessage());
        }
    }

    private BigDecimal addBig(BigDecimal data1, BigDecimal data2) {
        data1 = null == data1 ? BigDecimal.ZERO : data1;
        data2 = null == data2 ? BigDecimal.ZERO : data2;
        return data2.add(data1);
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
     * 查询第三方订单是否存在
     *
     * @param gameParentPlatform gameParentPlatform
     * @param paySerialno        paySerialno
     * @return Txns
     */
    private Txns getTxns(GameParentPlatform gameParentPlatform, String paySerialno) {
        LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(c -> c.eq(Txns::getMethod, "Place Bet")
                .or().eq(Txns::getMethod, "Cancel Bet")
                .or().eq(Txns::getMethod, "Adjust Bet"));
        wrapper.eq(Txns::getStatus, "Running");
        wrapper.eq(Txns::getPlatformTxId, paySerialno);
        wrapper.eq(Txns::getPlatform, gameParentPlatform.getPlatformCode());
        return txnsMapper.selectOne(wrapper);
    }

    /**
     * 初始化成功json返回
     *
     * @return JSONObject
     */
    private JSONObject initSuccessResponse() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("statusCode", 0);
        jsonObject.put("status", "Success");
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

    private GameParentPlatform getGameParentPlatform() {
        return gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.JILI_PLATFORM_CODE);
    }
}
