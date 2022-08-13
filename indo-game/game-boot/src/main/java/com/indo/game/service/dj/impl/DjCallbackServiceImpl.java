package com.indo.game.service.dj.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.enums.GoldchangeEnum;
import com.indo.common.enums.TradingEnum;
import com.indo.common.utils.DateUtils;
import com.indo.core.mapper.game.TxnsMapper;
import com.indo.game.pojo.dto.dj.DjCallBackParentReq;
import com.indo.game.pojo.entity.CptOpenMember;
import com.indo.core.pojo.entity.game.GameCategory;
import com.indo.core.pojo.entity.game.GameParentPlatform;
import com.indo.core.pojo.entity.game.GamePlatform;
import com.indo.core.pojo.entity.game.Txns;
import com.indo.core.service.game.common.GameCommonService;
import com.indo.game.service.cptopenmember.CptOpenMemberService;
import com.indo.game.service.dj.DjCallbackService;
import com.indo.core.pojo.bo.MemTradingBO;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;


/**
 * PG
 *
 * @author
 */
@Slf4j
@Service
public class DjCallbackServiceImpl implements DjCallbackService {

    @Resource
    private GameCommonService gameCommonService;

    @Autowired
    private CptOpenMemberService externalService;

    @Autowired
    private TxnsMapper txnsMapper;

    @Override
    public Object getBalance(DjCallBackParentReq djCallBackParentReq, String ip) {
//        CptOpenMember cptOpenMember = externalService.getCptOpenMember(Integer.parseInt(djCallBackParentReq.getLogin_id()), OpenAPIProperties.DJ_PLATFORM_CODE);
        StringBuilder stringBuilder = new StringBuilder();
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(djCallBackParentReq.getLogin_id());
        stringBuilder.append("<?xml version=\"1.0\" ?>").append("<get_balance>").append("<status_code>");
        if (memBaseinfo == null) {
            stringBuilder.append("99</status_code>").append("<status_text>OK</status_text>").append("<balance>");
            stringBuilder.append("0").append("</balance></get_balance>");
            return stringBuilder;
        }

        stringBuilder.append("00</status_code>").append("<status_text>OK</status_text>").append("<balance>");
        stringBuilder.append(memBaseinfo.getBalance()).append("</balance></get_balance>");
        return stringBuilder;
    }

    @Override
    public Object djBetCallback(DjCallBackParentReq djCallBackParentReq, String ip) {
//        CptOpenMember cptOpenMember = externalService.getCptOpenMember(Integer.parseInt(djCallBackParentReq.getLogin_id()), OpenAPIProperties.DJ_PLATFORM_CODE);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<?xml version=\"1.0\" ?>").append("<bet>").append("<status_code>");
        GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.DJ_PLATFORM_CODE);
        GamePlatform gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(OpenAPIProperties.DJ_PLATFORM_CODE,gameParentPlatform.getPlatformCode());
        GameCategory gameCategory = gameCommonService.getGameCategoryById(gamePlatform.getCategoryId());
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(djCallBackParentReq.getLogin_id());
        BigDecimal balance = memBaseinfo.getBalance();
        BigDecimal betAmount = djCallBackParentReq.getStake_money();
        if (betAmount.compareTo(BigDecimal.ZERO) == -1 && memBaseinfo.getBalance().compareTo(betAmount) == -1) {
            stringBuilder.append("88</status_code>").append("<status_text>Insufficient fund to bet </status_text>");
            stringBuilder.append("<ref_id>").append(djCallBackParentReq.getTicket_id()).append("</ref_id>").append("<balance>");
            stringBuilder.append(balance).append("</balance></bet>");
            return stringBuilder;
        }
        LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(c -> c.eq(Txns::getMethod, "Place Bet").or().eq(Txns::getMethod, "Cancel Bet").or().eq(Txns::getMethod, "Settle"));
        wrapper.eq(Txns::getStatus, "Running");
        wrapper.eq(Txns::getPlatformTxId, djCallBackParentReq.getTicket_id());
        wrapper.eq(Txns::getPlatform, OpenAPIProperties.DJ_PLATFORM_CODE);

        Txns oldTxns = txnsMapper.selectOne(wrapper);
        if (null != oldTxns) {
            if ("Cancel Bet".equals(oldTxns.getMethod())||"Settle".equals(oldTxns.getMethod())) {
                stringBuilder.append("All Other Code</status_code>").append("<status_text>General error </status_text>");
                stringBuilder.append("<ref_id>").append(djCallBackParentReq.getTicket_id()).append("</ref_id>").append("<balance>");
                stringBuilder.append(balance).append("</balance></bet>");
                return stringBuilder;
            }
        }
        if (null != oldTxns) {
            if (betAmount.compareTo(BigDecimal.ZERO) != 0) {
                if (betAmount.compareTo(BigDecimal.ZERO) == -1) {
                    balance = balance.subtract(betAmount.abs());
                    gameCommonService.updateUserBalance(memBaseinfo, betAmount.abs(), GoldchangeEnum.SETTLE, TradingEnum.SPENDING);
                } else {
                    balance = balance.add(betAmount);
                    gameCommonService.updateUserBalance(memBaseinfo, betAmount, GoldchangeEnum.SETTLE, TradingEnum.INCOME);
                }
            }
        }else {
            if (betAmount.compareTo(BigDecimal.ZERO) != 0) {
                if (betAmount.compareTo(BigDecimal.ZERO) == -1) {
                    balance = balance.subtract(betAmount.abs());
                    gameCommonService.updateUserBalance(memBaseinfo, betAmount.abs(), GoldchangeEnum.PLACE_BET, TradingEnum.SPENDING);
                } else {
                    balance = balance.subtract(betAmount);
                    gameCommonService.updateUserBalance(memBaseinfo, betAmount, GoldchangeEnum.PLACE_BET, TradingEnum.SPENDING);
                }
            }
        }

        Txns txns = new Txns();
        //游戏商注单号
        txns.setPlatformTxId(djCallBackParentReq.getTicket_id());
        //混合码
        txns.setRoundId(djCallBackParentReq.getTicket_id());
        //此交易是否是投注 true是投注 false 否
        //玩家 ID
        txns.setUserId(memBaseinfo.getAccount());
        //玩家货币代码
        txns.setCurrency(gameParentPlatform.getCurrencyType());
        //平台代码
        txns.setPlatform(gameParentPlatform.getPlatformCode());
        //平台英文名称
        txns.setPlatformEnName(gameParentPlatform.getPlatformEnName());
        //平台中文名称
        txns.setPlatformCnName(gameParentPlatform.getPlatformCnName());
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
        if (null != oldTxns) {
            txns.setWinningAmount(betAmount);
        }else {
            if (betAmount.compareTo(BigDecimal.ZERO) == -1) {
                txns.setWinningAmount(betAmount);
            }else {
                txns.setWinningAmount(betAmount.negate());
            }
        }
        txns.setWinAmount(betAmount);
        //玩家下注时间
        txns.setBetTime(DateUtils.formatByString(djCallBackParentReq.getCreated_datetime(), DateUtils.newFormat));
        //真实下注金额,需增加在玩家的金额
        txns.setRealBetAmount(betAmount);
        //返还金额 (包含下注金额)
        //有效投注金额 或 投注面值
        txns.setTurnover(betAmount);
        //辨认交易时间依据
        txns.setTxTime(null != djCallBackParentReq.getCreated_datetime() ? DateUtils.formatByString(djCallBackParentReq.getCreated_datetime(), DateUtils.newFormat) : "");
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
        if (oldTxns != null) {
            oldTxns.setStatus("Settle");
            txnsMapper.updateById(oldTxns);
            txns.setMethod("Settle");
        }

        int num = txnsMapper.insert(txns);
        if (num <= 0) {
            stringBuilder.append("All Other Code</status_code>").append("<status_text>General error </status_text>");
            stringBuilder.append("<ref_id>").append(djCallBackParentReq.getTicket_id()).append("</ref_id>").append("<balance>");
            stringBuilder.append(balance).append("</balance></bet>");
            return stringBuilder;
        }
        stringBuilder.append("00</status_code>").append("<status_text>OK</status_text>");
        stringBuilder.append("<ref_id>").append(djCallBackParentReq.getTicket_id()).append("</ref_id>").append("<balance>");
        stringBuilder.append(balance).append("</balance></bet>");

        return stringBuilder;
    }

    @Override
    public Object djRefundtCallback(DjCallBackParentReq djCallBackParentReq, String ip) {
        LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(c -> c.eq(Txns::getMethod, "Place Bet").or().eq(Txns::getMethod, "Cancel Bet").or().eq(Txns::getMethod, "Settle"));
        wrapper.eq(Txns::getStatus, "Running");
        wrapper.eq(Txns::getPlatformTxId, djCallBackParentReq.getTicket_id());
        wrapper.eq(Txns::getPlatform, OpenAPIProperties.DJ_PLATFORM_CODE);
        
        Txns oldTxns = txnsMapper.selectOne(wrapper);
        if (null == oldTxns) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<?xml version=\"1.0\" ?>").append("<cancel_bet>").append("<status_code>");
            stringBuilder.append("All Other Code</status_code>").append("<status_text>General error </status_text>");
            stringBuilder.append("</cancel_bet>");
            return stringBuilder;
        }
        if (null != oldTxns) {
            if ("Cancel Bet".equals(oldTxns.getMethod())) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("<?xml version=\"1.0\" ?>").append("<cancel_bet>").append("<status_code>");
                stringBuilder.append("All Other Code</status_code>").append("<status_text>General error </status_text>");
                stringBuilder.append("</cancel_bet>");
                return stringBuilder;
            }
        }
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(oldTxns.getUserId());
        BigDecimal balance = memBaseinfo.getBalance();
        BigDecimal amount = oldTxns.getWinningAmount();
        if ("Place Bet".equals(oldTxns.getMethod())) {
            if (amount.compareTo(BigDecimal.ZERO) != 0) {
                if (amount.compareTo(BigDecimal.ZERO) == -1) {//小于0
                    balance = balance.add(amount.abs());
                    gameCommonService.updateUserBalance(memBaseinfo, amount.abs(), GoldchangeEnum.CANCEL_BET, TradingEnum.INCOME);
                } else {//大于0
                    balance = balance.add(amount);
                    gameCommonService.updateUserBalance(memBaseinfo, amount, GoldchangeEnum.CANCEL_BET, TradingEnum.SPENDING);
                }
            }
        }else {
            if (amount.compareTo(BigDecimal.ZERO) != 0) {
                if (amount.compareTo(BigDecimal.ZERO) == -1) {//小于0
                    balance = balance.add(amount.abs());
                    gameCommonService.updateUserBalance(memBaseinfo, amount.abs(), GoldchangeEnum.UNSETTLE, TradingEnum.INCOME);
                } else {//大于0
                    balance = balance.add(amount);
                    gameCommonService.updateUserBalance(memBaseinfo, amount, GoldchangeEnum.UNSETTLE, TradingEnum.SPENDING);
                }
            }
        }
        String dateStr = DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT);

        Txns txns = new Txns();
        BeanUtils.copyProperties(oldTxns, txns);
        //游戏商注单号
        txns.setPlatformTxId(djCallBackParentReq.getTicket_id());
        //混合码
        txns.setBalance(balance);
        txns.setId(null);
        txns.setMethod("Cancel Bet");
        txns.setStatus("Running");
        txns.setCreateTime(dateStr);
        txnsMapper.insert(txns);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<?xml version=\"1.0\" ?>").append("<cancel_bet>").append("<status_code>");
        stringBuilder.append("00</status_code>").append("<status_text>OK</status_text>");
        stringBuilder.append("</cancel_bet>");
        return stringBuilder;
    }

}
