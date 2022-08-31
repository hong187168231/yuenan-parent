package com.indo.game.service.fc.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.enums.GoldchangeEnum;
import com.indo.common.enums.TradingEnum;
import com.indo.common.utils.DateUtils;
import com.indo.core.mapper.game.TxnsMapper;
import com.indo.game.pojo.dto.fc.FCBalanceCallbackReq;
import com.indo.game.pojo.dto.fc.FCBetCallbackReq;
import com.indo.game.pojo.dto.fc.FCCancelCallbackReq;
import com.indo.core.pojo.entity.game.GameCategory;
import com.indo.core.pojo.entity.game.GameParentPlatform;
import com.indo.core.pojo.entity.game.GamePlatform;
import com.indo.core.pojo.entity.game.Txns;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.fc.FCCallbackService;
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
public class FCCallbackServiceImpl implements FCCallbackService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private GameCommonService gameCommonService;
    @Autowired
    private TxnsMapper txnsMapper;

    @Override
    public Object balance(FCBalanceCallbackReq fcBalanceCallbackReq, String ip) {
        logger.info("fc_balance fcGame paramJson:{}, ip:{}", JSONObject.toJSONString(fcBalanceCallbackReq), ip);
        try {
            GameParentPlatform gameParentPlatform = getGameParentPlatform();
            // 校验IP
            if (checkIp(ip, gameParentPlatform)) {
                return initFailureResponse(401, "不允许的IP");
            }

            // 查询玩家是否存在
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(fcBalanceCallbackReq.getMemberAccount());
            if (null == memBaseinfo) {
                return initFailureResponse(500, "玩家不存在");
            }

            // 会员余额返回
            JSONObject jsonObject = initSuccessResponse();
            jsonObject.put("MainPoints", memBaseinfo.getBalance().divide(gameParentPlatform.getCurrencyPro()));
            return jsonObject;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return initFailureResponse(1, e.getMessage());
        }
    }


    @Override
    public Object bet(FCBetCallbackReq fcBetCallbackReq, String ip) {
        logger.info("fc_bet fcGame paramJson:{}, ip:{}", JSONObject.toJSONString(fcBetCallbackReq), ip);
        try {

            GameParentPlatform platformGameParent = getGameParentPlatform();

            // 校验IP
            if (checkIp(ip, platformGameParent)) {
                return initFailureResponse(401, "不允许的IP");
            }

            // 查询玩家是否存在
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(fcBetCallbackReq.getMemberAccount());
            if (null == memBaseinfo) {
                return initFailureResponse(704, "玩家不存在");
            }

            // 会员余额
            BigDecimal balance = memBaseinfo.getBalance();
            // 下注金额
            BigDecimal betAmount = null!=fcBetCallbackReq.getBet()?fcBetCallbackReq.getBet().multiply(platformGameParent.getCurrencyPro()):BigDecimal.ZERO;
            BigDecimal winAmount = null!=fcBetCallbackReq.getWin()?fcBetCallbackReq.getWin().multiply(platformGameParent.getCurrencyPro()):BigDecimal.ZERO;
            BigDecimal netWin = null!=fcBetCallbackReq.getNetWin()?fcBetCallbackReq.getNetWin().multiply(platformGameParent.getCurrencyPro()):BigDecimal.ZERO;
            if (null != fcBetCallbackReq.getRequireAmt()) {
                betAmount = fcBetCallbackReq.getRequireAmt();
            }
            if (balance.compareTo(betAmount) < 0) {
                return initFailureResponse(203, "玩家余额不足");
            }

            if (netWin.compareTo(BigDecimal.ZERO) > 0) {
                balance = balance.add(netWin);
                // 更新玩家余额
                gameCommonService.updateUserBalance(memBaseinfo, netWin, GoldchangeEnum.BETNSETTLE, TradingEnum.INCOME);
            } else if (netWin.compareTo(BigDecimal.ZERO) < 0) {
                balance = balance.subtract(netWin);
                gameCommonService.updateUserBalance(memBaseinfo, netWin, GoldchangeEnum.BETNSETTLE, TradingEnum.SPENDING);
            }


            // 查询用户请求订单
            Txns oldTxns = getTxns(fcBetCallbackReq.getBankID(), memBaseinfo.getId(), fcBetCallbackReq.getRecordID());
            if (null != oldTxns) {
                return initFailureResponse(201, "交易已存在");
            }


            GamePlatform gamePlatform = gameCommonService.getGamePlatformByplatformCode(fcBetCallbackReq.getGameId());
            GameCategory gameCategory = gameCommonService.getGameCategoryById(gamePlatform.getCategoryId());

            Txns txns = new Txns();
            //游戏商注单号
            txns.setPlatformTxId(fcBetCallbackReq.getBankID());

            //玩家 ID
            txns.setUserId(memBaseinfo.getId().toString());
            //玩家货币代码
            txns.setCurrency(platformGameParent.getCurrencyType());
//            txns.setOdds(kaCallbackPlayReq.getBetPerSelection());
            txns.setRoundId(fcBetCallbackReq.getRecordID().toString());
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
            //下注金额 (实际下注金额加彩金下注金额)
            txns.setBetAmount(betAmount.add(fcBetCallbackReq.getJpBet()));
            //游戏平台的下注项目
            txns.setBetType(fcBetCallbackReq.getGameId());
            //中奖金额（赢为正数，亏为负数，和为0）或者总输赢
            txns.setWinningAmount(netWin);
            //玩家下注时间
            txns.setBetTime(DateUtils.format(fcBetCallbackReq.getCreateDate(), DateUtils.newFormat));
            //真实下注金额,需增加在玩家的金额
            txns.setRealBetAmount(betAmount);
            // 实际中奖金额
            txns.setRealWinAmount(winAmount.add(fcBetCallbackReq.getJpPrize()));
            //真实返还金额,游戏赢分
            txns.setRealWinAmount(netWin);
            // 彩金中奖金额算派彩
            txns.setAmount(fcBetCallbackReq.getJpPrize());
            //返还金额 (包含下注金额)
            txns.setWinAmount(netWin);
            //此交易是否是投注 true是投注 false 否
            txns.setBet(true);
            //赌注的结果 : 赢:0,输:1,平手:2
            int resultTyep;
            if (netWin.compareTo(BigDecimal.ZERO) == 0) {
                resultTyep = 2;
            } else if (netWin.compareTo(BigDecimal.ZERO) > 0) {
                resultTyep = 0;
            } else {
                resultTyep = 1;
            }
            txns.setResultType(resultTyep);

            //有效投注金额 或 投注面值
            txns.setTurnover(betAmount);
            //辨认交易时间依据
            txns.setTxTime(DateUtils.format(fcBetCallbackReq.getGameDate(), DateUtils.newFormat));
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
                return initFailureResponse(702, "订单写入失败");
            }

            JSONObject jsonObject = initSuccessResponse();
            jsonObject.put("MainPoints", balance.divide(platformGameParent.getCurrencyPro()));
            return jsonObject;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return initFailureResponse(1, e.getMessage());
        }
    }

    @Override
    public Object cancel(FCCancelCallbackReq fcCancelCallbackReq, String ip) {
        logger.info("fc_cancel fcGame paramJson:{}, ip:{}", JSONObject.toJSONString(fcCancelCallbackReq), ip);
        try {

            GameParentPlatform platformGameParent = getGameParentPlatform();

            // 校验IP
            if (checkIp(ip, platformGameParent)) {
                return initFailureResponse(401, "不允许的IP");
            }

            // 查询玩家是否存在
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(fcCancelCallbackReq.getMemberAccount());
            if (null == memBaseinfo) {
                return initFailureResponse(704, "玩家不存在");
            }

            // 查询用户请求订单
            Txns oldTxns = getTxns(fcCancelCallbackReq.getBankID(), memBaseinfo.getId(), null);
            if (null == oldTxns) {
                return initFailureResponse(221, "交易单号不存在");
            }

            // 如果订单已经取消
            if ("Cancel Bet".equals(oldTxns.getStatus())) {
                return initFailureResponse(205, "该笔交易不能注销");
            }

            // 会员余额
            BigDecimal cancelAmount = oldTxns.getWinAmount();
            BigDecimal balance = memBaseinfo.getBalance();

            if (cancelAmount.compareTo(BigDecimal.ZERO) > 0) {
                balance = balance.subtract(cancelAmount);
                // 更新玩家余额
                gameCommonService.updateUserBalance(memBaseinfo, cancelAmount, GoldchangeEnum.CANCEL_BET, TradingEnum.SPENDING);
            } else if (cancelAmount.compareTo(BigDecimal.ZERO) < 0) {
                balance = balance.add(cancelAmount.abs());
                gameCommonService.updateUserBalance(memBaseinfo, cancelAmount.abs(), GoldchangeEnum.CANCEL_BET, TradingEnum.INCOME);
            }
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
            jsonObject.put("MainPoints", balance.divide(platformGameParent.getCurrencyPro()));
            return jsonObject;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return initFailureResponse(1, e.getMessage());
        }
    }

    /**
     * 获取历史订单
     *
     * @param reference 订单编号
     * @param userId    会员ID
     * @param round     回合(一般游戏为0,免费游戏有具体1.2.3...)
     * @return 订单
     */
    private Txns getTxns(String reference, Long userId, String round) {
        LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(c -> c.eq(Txns::getMethod, "Place Bet")
                .or().eq(Txns::getMethod, "Cancel Bet")
                .or().eq(Txns::getMethod, "Adjust Bet")
                .or().eq(Txns::getMethod, "Settle"));
        wrapper.eq(Txns::getStatus, "Running");
        wrapper.eq(Txns::getPlatformTxId, reference);
        wrapper.eq(Txns::getPlatform, OpenAPIProperties.FC_PLATFORM_CODE);
        wrapper.eq(Txns::getUserId, userId);
        if (null != round) {
            wrapper.eq(Txns::getRoundId, round);
        }
        return txnsMapper.selectOne(wrapper);
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
        jsonObject.put("Result", 0);
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
        jsonObject.put("Result", error);
        jsonObject.put("ErrorText", description);
        return jsonObject;
    }

    private GameParentPlatform getGameParentPlatform() {
        return gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.FC_PLATFORM_CODE);
    }
}
