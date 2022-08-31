package com.indo.game.service.pp.impl;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.enums.GoldchangeEnum;
import com.indo.common.enums.TradingEnum;
import com.indo.common.utils.DateUtils;
import com.indo.common.utils.StringUtils;
import com.indo.game.common.util.PPHashAESEncrypt;
import com.indo.core.mapper.game.TxnsMapper;
import com.indo.game.pojo.dto.pp.*;
import com.indo.game.pojo.entity.CptOpenMember;
import com.indo.core.pojo.entity.game.GameCategory;
import com.indo.core.pojo.entity.game.GameParentPlatform;
import com.indo.core.pojo.entity.game.GamePlatform;
import com.indo.core.pojo.entity.game.Txns;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.cptopenmember.CptOpenMemberService;
import com.indo.game.service.pp.PpCallbackService;
import com.indo.core.pojo.bo.MemTradingBO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * PP电子回调服务
 */
@Service
public class PpCallbackServiceImpl implements PpCallbackService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private GameCommonService gameCommonService;
    @Autowired
    private CptOpenMemberService externalService;
    @Autowired
    private TxnsMapper txnsMapper;

    // 权限校验
    @Override
    public Object authenticate(PpAuthenticateCallBackReq ppAuthenticateCallBackReq, String ip) {
        logger.info("pp_authenticate  ppGame paramJson:{}, ip:{}", JSONObject.toJSONString(ppAuthenticateCallBackReq), ip);
        JSONObject json = initSuccessResponse();
        // 请求参数与哈希参数对比。如果失败，娱乐场运营商应发送错误代码5
        if (checkReqHash(ppAuthenticateCallBackReq, ppAuthenticateCallBackReq.getHash())) {
            return initFailureResponse(5, "请求参数非法",json);
        }
        GameParentPlatform platformGameParent = getGameParentPlatform();
        // 校验IP
        if (checkIp(ip, platformGameParent)) {
            return initFailureResponse(5, "非信任來源IP",json);
        }
        String ppPlatformCode = OpenAPIProperties.PP_PLATFORM_CODE;
        CptOpenMember cptOpenMember = externalService.quertCptOpenMember(ppAuthenticateCallBackReq.getToken(), ppPlatformCode);

        if (null == cptOpenMember) {
            return initFailureResponse(2, "玩家不存在",json);
        }

        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(cptOpenMember.getUserName());
        if (null == memBaseinfo) {
            return initFailureResponse(2, "玩家不存在",json);
        }


        json.put("currency", platformGameParent.getCurrencyType());
        json.put("cash", cptOpenMember.getBalance().divide(platformGameParent.getCurrencyPro()));
        json.put("bonus", BigDecimal.ZERO);
        json.put("token", cptOpenMember.getPassword());
        return json;
    }

    // 获取余额
    @Override
    public Object getBalance(PpBalanceCallBackReq ppBalanceCallBackReq, String ip) {
        logger.info("pp_getBalance  ppGame paramJson:{}, ip:{}", JSONObject.toJSONString(ppBalanceCallBackReq), ip);
        JSONObject json = initSuccessResponse();
        // 请求参数与哈希参数对比。如果失败，娱乐场运营商应发送错误代码5
        if (checkReqHash(ppBalanceCallBackReq, ppBalanceCallBackReq.getHash())) {
            return initFailureResponse(5, "请求参数非法",json);
        }
        GameParentPlatform platformGameParent = getGameParentPlatform();
        // 校验IP
        if (checkIp(ip, platformGameParent)) {
            return initFailureResponse(5, "非信任來源IP",json);
        }
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(ppBalanceCallBackReq.getUserId());
        if (null == memBaseinfo) {
            return initFailureResponse(2, "玩家不存在",json);
        }

        json.put("currency", platformGameParent.getCurrencyType());
        json.put("cash", memBaseinfo.getBalance().divide(platformGameParent.getCurrencyPro()));
        json.put("bonus", BigDecimal.ZERO);
        return json;

    }

    // 下注
    @Override
    public Object bet(PpBetCallBackReq ppBetCallBackReq, String ip) {
        logger.info("pp_bet  ppGame paramJson:{}, ip:{}", JSONObject.toJSONString(ppBetCallBackReq), ip);
        GameParentPlatform platformGameParent = getGameParentPlatform();
        JSONObject json = initSuccessResponse(ppBetCallBackReq.getReference(), platformGameParent.getCurrencyType(), BigDecimal.ZERO);
        json.put("usedPromo", BigDecimal.ZERO);

        // 请求参数与哈希参数对比。如果失败，娱乐场运营商应发送错误代码5
        if (checkReqHash(ppBetCallBackReq, ppBetCallBackReq.getHash())) {
            return initFailureResponse(5, "请求参数非法",json);
        }

        // 校验IP
        if (checkIp(ip, platformGameParent)) {
            return initFailureResponse(5, "非信任來源IP",json);
        }

        try {
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(ppBetCallBackReq.getUserId());
            if (null == memBaseinfo) {
                return initFailureResponse(2, "玩家不存在", json);
            }
            // 会员余额
            BigDecimal balance = memBaseinfo.getBalance();
            GamePlatform gamePlatform;
            if ("Y".equals(OpenAPIProperties.PP_IS_PLATFORM_LOGIN)) {
                gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(platformGameParent.getPlatformCode(), platformGameParent.getPlatformCode());
            } else {
                gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(ppBetCallBackReq.getGameId(), platformGameParent.getPlatformCode());

            }
            if (null == gamePlatform) {
                json.put("cash", balance.divide(platformGameParent.getCurrencyPro()));
                return initFailureResponse(3, "游戏不存在",json);
            }
            GameCategory gameCategory = gameCommonService.getGameCategoryById(gamePlatform.getCategoryId());


            // 下注金额
            BigDecimal betAmount = null!=ppBetCallBackReq.getAmount()?ppBetCallBackReq.getAmount().multiply(platformGameParent.getCurrencyPro()):BigDecimal.ZERO;

            // 查询用户请求订单
            Txns oldTxns = getTxns(ppBetCallBackReq.getReference(), memBaseinfo.getAccount());
            if (null != oldTxns) {
                json.put("cash", balance.divide(platformGameParent.getCurrencyPro()));
                return json;
            }

            if (memBaseinfo.getBalance().compareTo(betAmount) < 0) {
                return initFailureResponse(1, "玩家余额不足", json);
            }

            // 下注金额小于0
            if (betAmount.compareTo(BigDecimal.ZERO) < 0) {
                return initFailureResponse(7, "下注金额不能小0", json);
            }
            balance = balance.subtract(betAmount);
            // 更新玩家余额
            gameCommonService.updateUserBalance(memBaseinfo, betAmount, GoldchangeEnum.PLACE_BET, TradingEnum.SPENDING);

            Txns txns = new Txns();
            //游戏商注单号
            txns.setPlatformTxId(ppBetCallBackReq.getReference());
            //此交易是否是投注 true是投注 false 否
            txns.setBet(true);
            //玩家 ID
            txns.setUserId(memBaseinfo.getAccount());
            //玩家货币代码
            txns.setCurrency(platformGameParent.getCurrencyType());
            txns.setGameInfo(ppBetCallBackReq.getRoundDetails());
            txns.setRoundId(ppBetCallBackReq.getRoundId());
            //平台代码
            txns.setPlatform(platformGameParent.getPlatformCode());
            //平台名称
            txns.setPlatformEnName(platformGameParent.getPlatformEnName());
            txns.setPlatformCnName(platformGameParent.getPlatformCnName());
            if (null!=gamePlatform){
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
        }
            //下注金额
            txns.setBetAmount(betAmount);
            //游戏平台的下注项目
            txns.setBetType(ppBetCallBackReq.getGameId());
            //中奖金额（赢为正数，亏为负数，和为0）或者总输赢
            txns.setWinningAmount(betAmount.negate());
            //玩家下注时间
            txns.setBetTime(DateUtils.formatByLong(ppBetCallBackReq.getTimestamp(), DateUtils.newFormat));
            //真实下注金额,需增加在玩家的金额
            txns.setRealBetAmount(betAmount);
            //真实返还金额,游戏赢分
            txns.setRealWinAmount(BigDecimal.ZERO);
            //返还金额 (包含下注金额)
            //赌注的结果 : 赢:0,输:1,平手:2
            //有效投注金额 或 投注面值
            txns.setTurnover(betAmount);
            //辨认交易时间依据
            txns.setTxTime(DateUtils.formatByLong(ppBetCallBackReq.getTimestamp(), DateUtils.newFormat));
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
                return initFailureResponse(100, "订单入库请求失败",json);
            }

            json.put("cash", balance.divide(platformGameParent.getCurrencyPro()));
            return json;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return initFailureResponse(100, e.getMessage(),json);
        }

    }

    // 中奖
    @Override
    public Object result(PpResultCallBackReq ppResultCallBackReq, String ip) {
        logger.info("pp_result  ppGame paramJson:{}, ip:{}", JSONObject.toJSONString(ppResultCallBackReq), ip);
        GameParentPlatform platformGameParent = getGameParentPlatform();
        JSONObject json = initSuccessResponse(ppResultCallBackReq.getReference(), platformGameParent.getCurrencyType(), BigDecimal.ZERO);
        // 请求参数与哈希参数对比。如果失败，娱乐场运营商应发送错误代码5
        if (checkReqHash(ppResultCallBackReq, ppResultCallBackReq.getHash())) {
            return initFailureResponse(5, "请求参数非法",json);
        }

        // 校验IP
        if (checkIp(ip, platformGameParent)) {
            return initFailureResponse(5, "非信任來源IP",json);
        }

        try {
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(ppResultCallBackReq.getUserId());
            if (null == memBaseinfo) {
                return initFailureResponse(2, "玩家不存在",json);
            }
            // 会员余额
            BigDecimal balance = memBaseinfo.getBalance();
            GamePlatform gamePlatform;
            if ("Y".equals(OpenAPIProperties.PP_IS_PLATFORM_LOGIN)) {
                gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(platformGameParent.getPlatformCode(), platformGameParent.getPlatformCode());
            } else {
                gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(ppResultCallBackReq.getGameId(), platformGameParent.getPlatformCode());

            }
            if (null == gamePlatform) {
                json.put("cash", balance.divide(platformGameParent.getCurrencyPro()));
                return initFailureResponse(3, "游戏不存在",json);
            }
            GameCategory gameCategory = gameCommonService.getGameCategoryById(gamePlatform.getCategoryId());
            // 查询用户请求订单
            Txns oldTxns = getTxns(ppResultCallBackReq.getReference(), memBaseinfo.getAccount());
            if (null != oldTxns && "Settle".equals(oldTxns.getMethod())) {
                json.put("cash", balance.divide(platformGameParent.getCurrencyPro()));
                return json;
            }

            // 中奖金额
            BigDecimal settledAmount = null!=ppResultCallBackReq.getAmount()?ppResultCallBackReq.getAmount().multiply(platformGameParent.getCurrencyPro()):BigDecimal.ZERO;
            if(settledAmount.compareTo(BigDecimal.ZERO)!=0) {
                // 会员余额
                balance = balance.add(settledAmount);
                if(settledAmount.compareTo(BigDecimal.ZERO)==1) {//赢
                    // 更新玩家余额
                    gameCommonService.updateUserBalance(memBaseinfo, settledAmount, GoldchangeEnum.SETTLE, TradingEnum.INCOME);
                }else {
                    gameCommonService.updateUserBalance(memBaseinfo, settledAmount.abs(), GoldchangeEnum.SETTLE, TradingEnum.SPENDING);
                }
            }
            BigDecimal promoWinAmount = null!=ppResultCallBackReq.getPromoWinAmount()?ppResultCallBackReq.getPromoWinAmount().multiply(platformGameParent.getCurrencyPro()):BigDecimal.ZERO;
            if(null!=promoWinAmount&&promoWinAmount.compareTo(BigDecimal.ZERO)!=0) {
                // 会员余额
                balance = balance.add(promoWinAmount);
                settledAmount =  settledAmount.add(promoWinAmount);
                if(promoWinAmount.compareTo(BigDecimal.ZERO)==1) {//赢
                    // 更新玩家余额
                    gameCommonService.updateUserBalance(memBaseinfo, promoWinAmount, GoldchangeEnum.SETTLE, TradingEnum.INCOME);
                }else {
                    gameCommonService.updateUserBalance(memBaseinfo, promoWinAmount.abs(), GoldchangeEnum.SETTLE, TradingEnum.SPENDING);
                }
            }
            // 查询用户请求订单
            List<Txns> oldTxnsList = getTxnsByRoundld(ppResultCallBackReq.getRoundId(), memBaseinfo.getAccount());
            BigDecimal betAmount = BigDecimal.ZERO;
            for(Txns oldTxnsUpdate:oldTxnsList){
                betAmount = betAmount.add(oldTxnsUpdate.getBetAmount());
                //更新时间
                String dateStr = DateUtils.format(new Date(), DateUtils.newFormat);
                oldTxnsUpdate.setUpdateTime(dateStr);
                oldTxnsUpdate.setStatus("Settle");
                txnsMapper.updateById(oldTxnsUpdate);
            }
            Txns txns = new Txns();
            //游戏商注单号
            txns.setPlatformTxId(ppResultCallBackReq.getReference());
            //此交易是否是投注 true是投注 false 否
            txns.setBet(false);
            //玩家 ID
            txns.setUserId(memBaseinfo.getAccount());
            //玩家货币代码
            txns.setCurrency(platformGameParent.getCurrencyType());
            txns.setGameInfo(ppResultCallBackReq.getRoundDetails());
            txns.setRoundId(ppResultCallBackReq.getRoundId());
            //平台代码
            txns.setPlatform(platformGameParent.getPlatformCode());
            //平台名称
            txns.setPlatformEnName(platformGameParent.getPlatformEnName());
            txns.setPlatformCnName(platformGameParent.getPlatformCnName());
            if (null!=gamePlatform){
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
            }
            //下注金额
            txns.setBetAmount(betAmount);
            //中奖金额（赢为正数，亏为负数，和为0）或者总输赢
            txns.setWinningAmount(settledAmount);
            txns.setWinAmount(settledAmount);
            //真实返还金额,游戏赢分
            txns.setRealWinAmount(settledAmount);
            //辨认交易时间依据
            txns.setTxTime(DateUtils.formatByLong(ppResultCallBackReq.getTimestamp(), DateUtils.newFormat));
            txns.setUpdateTime(DateUtils.formatByLong(ppResultCallBackReq.getTimestamp(), DateUtils.newFormat));
            //赌注的结果 : 赢:0,输:1,平手:2
            int resultTyep;
            if (settledAmount.compareTo(BigDecimal.ZERO) == 0) {
                resultTyep = 2;
            } else if (settledAmount.compareTo(BigDecimal.ZERO) > 0) {
                resultTyep = 0;
            } else {
                resultTyep = 1;
            }
            txns.setResultType(resultTyep);
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
                return initFailureResponse(100, "订单入库请求失败",json);
            }
            json.put("cash", balance.divide(platformGameParent.getCurrencyPro()));
            return json;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return initFailureResponse(100, e.getMessage(),json);
        }

    }

    // 免费回合赢奖
    @Override
    public Object bonusWin(PpBonusWinCallBackReq ppBonusWinCallBackReq, String ip) {
        logger.info("pp_bonuswin  ppGame paramJson:{}, ip:{}", JSONObject.toJSONString(ppBonusWinCallBackReq), ip);
        GameParentPlatform platformGameParent = getGameParentPlatform();
        JSONObject json = initSuccessResponse(ppBonusWinCallBackReq.getReference(), platformGameParent.getCurrencyType(), BigDecimal.ZERO);
        // 请求参数与哈希参数对比。如果失败，娱乐场运营商应发送错误代码5
        if (checkReqHash(ppBonusWinCallBackReq, ppBonusWinCallBackReq.getHash())) {
            return initFailureResponse(5, "请求参数非法",json);
        }

        // 校验IP
        if (checkIp(ip, platformGameParent)) {
            return initFailureResponse(5, "非信任來源IP",json);
        }
        try {
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(ppBonusWinCallBackReq.getUserId());
            if (null == memBaseinfo) {
                return initFailureResponse(2, "玩家不存在",json);
            }// 会员余额
            BigDecimal balance = memBaseinfo.getBalance();
//            GamePlatform gamePlatform;
//            if("Y".equals(OpenAPIProperties.PP_IS_PLATFORM_LOGIN)){
//                gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(platformGameParent.getPlatformCode(),platformGameParent.getPlatformCode());
//            }else {
//                gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(ppBonusWinCallBackReq.getGameId(), platformGameParent.getPlatformCode());
//
//            }
//            if (null == gamePlatform) {
//                gamePlatform = gameCommonService.getGamePlatformByParentName(OpenAPIProperties.PP_PLATFORM_CODE).get(0);
//            }
//            GameCategory gameCategory = gameCommonService.getGameCategoryById(gamePlatform.getCategoryId());
//
//            // 赢奖金额
//            BigDecimal betAmount = ppBonusWinCallBackReq.getAmount();
//
//            // 赢奖金额小于0
//            if (betAmount.compareTo(BigDecimal.ZERO) < 0) {
//                json.put("cash", balance);
//                return initFailureResponse(7, "赢奖金额不能小0",json);
//            }

            // 查询用户请求订单
//            Txns txns = getTxnsByBonusCode(ppBonusWinCallBackReq.getBonusCode(), memBaseinfo.getAccount());
//            if (null != txns) {
//                return initSuccessResponse(ppBonusWinCallBackReq.getReference(), platformGameParent.getCurrencyType(), balance);
//            }
//            txns = new Txns();
//
//            // 会员余额
//            balance = balance.add(betAmount);
//            // 更新玩家余额
//            gameCommonService.updateUserBalance(memBaseinfo, betAmount, GoldchangeEnum.FREE_SPIN, TradingEnum.INCOME);
//
//            //游戏商注单号
//            txns.setPlatformTxId(ppBonusWinCallBackReq.getReference());
//            //此交易是否是投注 true是投注 false 否
//            txns.setBet(false);
//            //玩家 ID
//            txns.setUserId(memBaseinfo.getAccount());
//            //玩家货币代码
//            txns.setCurrency(platformGameParent.getCurrencyType());
//            txns.setGameInfo(ppBonusWinCallBackReq.getGameId());
//            txns.setRoundId(ppBonusWinCallBackReq.getRoundId());
//            txns.setRePlatformTxId(ppBonusWinCallBackReq.getBonusCode());
//            //游戏平台的下注项目
//            txns.setBetType(ppBonusWinCallBackReq.getGameId());
//            // 奖金游戏
//            txns.setHasBonusGame(1);
//            //平台代码
//            txns.setPlatform(platformGameParent.getPlatformCode());
//            //平台名称
//            txns.setPlatformEnName(platformGameParent.getPlatformEnName());
//            txns.setPlatformCnName(platformGameParent.getPlatformCnName());
//            //平台游戏类型
//            txns.setGameType(gameCategory.getGameType());
//            //游戏分类ID
//            txns.setCategoryId(gameCategory.getId());
//            //游戏分类名称
//            txns.setCategoryName(gameCategory.getGameName());
//            //平台游戏代码
//            txns.setGameCode(gamePlatform.getPlatformCode());
//            //游戏名称
//            txns.setGameName(gamePlatform.getPlatformEnName());
//            //下注金额
//            txns.setBetAmount(BigDecimal.ZERO);
//            //中奖金额（赢为正数，亏为负数，和为0）或者总输赢
//            txns.setWinningAmount(ppBonusWinCallBackReq.getAmount());
//            //玩家下注时间
//            txns.setBetTime(DateUtils.formatByLong(ppBonusWinCallBackReq.getTimestamp(), DateUtils.newFormat));
//            //真实下注金额,需增加在玩家的金额
//            txns.setRealBetAmount(BigDecimal.ZERO);
//            //真实返还金额,游戏赢分
//            txns.setRealWinAmount(ppBonusWinCallBackReq.getAmount());
//            //返还金额 (包含下注金额)
//            txns.setWinAmount(ppBonusWinCallBackReq.getAmount());
//            //赌注的结果 : 赢:0,输:1,平手:2 (零金额会被视为输)
//            int resultTyep;
//            if (ppBonusWinCallBackReq.getAmount().compareTo(BigDecimal.ZERO) > 0) {
//                resultTyep = 0;
//            } else {
//                resultTyep = 1;
//            }
//            txns.setResultType(resultTyep);
//            //有效投注金额 或 投注面值
//            txns.setTurnover(BigDecimal.ZERO);
//            //辨认交易时间依据
//            txns.setTxTime(DateUtils.formatByLong(ppBonusWinCallBackReq.getTimestamp(), DateUtils.newFormat));
//            //操作名称
//            txns.setMethod("Settle");
//            txns.setStatus("Running");
//            //余额
//            txns.setBalance(balance);
//            //创建时间
//            String dateStr = DateUtils.format(new Date(), DateUtils.newFormat);
//            //投注 IP
//            txns.setBetIp(ip);//  string 是 投注 IP
//            txns.setCreateTime(dateStr);
//            int num = txnsMapper.insert(txns);
//            if (num <= 0) {
//                return initFailureResponse(100, "订单入库请求失败",json);
//            }
            json.put("cash", balance.divide(platformGameParent.getCurrencyPro()));
            return json;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return initFailureResponse(100, e.getMessage(),json);
        }

    }

    // 累计奖金赢奖(促销)
    @Override
    public Object jackpotWin(PpJackpotWinCallBackReq ppJackpotWinCallBackReq, String ip) {
        logger.info("pp_jackpotwin  ppGame paramJson:{}, ip:{}", JSONObject.toJSONString(ppJackpotWinCallBackReq), ip);
        GameParentPlatform platformGameParent = getGameParentPlatform();
        JSONObject json = initSuccessResponse(ppJackpotWinCallBackReq.getReference(), platformGameParent.getCurrencyType(), BigDecimal.ZERO);
        // 请求参数与哈希参数对比。如果失败，娱乐场运营商应发送错误代码5
        if (checkReqHash(ppJackpotWinCallBackReq, ppJackpotWinCallBackReq.getHash())) {
            return initFailureResponse(5, "请求参数非法",json);
        }

        // 校验IP
        if (checkIp(ip, platformGameParent)) {
            return initFailureResponse(5, "非信任來源IP",json);
        }
        try {
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(ppJackpotWinCallBackReq.getUserId());
            if (null == memBaseinfo) {
                return initFailureResponse(2, "玩家不存在",json);
            }
            // 会员余额
            BigDecimal balance = memBaseinfo.getBalance();
            GamePlatform gamePlatform;
            if("Y".equals(OpenAPIProperties.PP_IS_PLATFORM_LOGIN)){
                gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(platformGameParent.getPlatformCode(),platformGameParent.getPlatformCode());
            }else {
                gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(ppJackpotWinCallBackReq.getGameId(), platformGameParent.getPlatformCode());

            }
            if (null == gamePlatform) {
                gamePlatform = gameCommonService.getGamePlatformByParentName(OpenAPIProperties.PP_PLATFORM_CODE).get(0);
            }
            GameCategory gameCategory = gameCommonService.getGameCategoryById(gamePlatform.getCategoryId());

            // 赢奖金额
            BigDecimal betAmount = null!=ppJackpotWinCallBackReq.getAmount()?ppJackpotWinCallBackReq.getAmount().multiply(platformGameParent.getCurrencyPro()):BigDecimal.ZERO;

            // 赢奖金额小于0
            if (betAmount.compareTo(BigDecimal.ZERO) < 0) {
                json.put("cash", balance.divide(platformGameParent.getCurrencyPro()));
                return initFailureResponse(7, "赢奖金额不能小0",json);
            }

            // 查询用户请求订单
            Txns txns = getTxnsJackpotWin(ppJackpotWinCallBackReq.getReference(), memBaseinfo.getAccount());
            if (null != txns) {
                return initSuccessResponse(ppJackpotWinCallBackReq.getReference(), platformGameParent.getCurrencyType(), balance);
            }
            txns = new Txns();
            // 会员余额
            balance = balance.add(betAmount);
            // 更新玩家余额
            gameCommonService.updateUserBalance(memBaseinfo, betAmount, GoldchangeEnum.reward, TradingEnum.INCOME);

            //游戏商注单号
            txns.setPlatformTxId(ppJackpotWinCallBackReq.getReference());
            //此交易是否是投注 true是投注 false 否
            txns.setBet(false);
            //玩家 ID
            txns.setUserId(memBaseinfo.getAccount());
            //玩家货币代码
            txns.setCurrency(platformGameParent.getCurrencyType());
            txns.setGameInfo(ppJackpotWinCallBackReq.getGameId());
            txns.setRoundId(ppJackpotWinCallBackReq.getRoundId());
            //游戏平台的下注项目
            txns.setBetType(ppJackpotWinCallBackReq.getJackpotId());
            // 奖金游戏
            txns.setHasBonusGame(1);
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
            //中奖金额（赢为正数，亏为负数，和为0）或者总输赢
            txns.setWinningAmount(betAmount);
            //玩家下注时间
            txns.setBetTime(DateUtils.formatByLong(ppJackpotWinCallBackReq.getTimestamp(), DateUtils.newFormat));
            //真实下注金额,需增加在玩家的金额
            txns.setRealBetAmount(BigDecimal.ZERO);
            //真实返还金额,游戏赢分
            txns.setRealWinAmount(betAmount);
            //返还金额 (包含下注金额)
            txns.setWinAmount(betAmount);
            //赌注的结果 : 赢:0,输:1,平手:2
            int resultTyep;
            if (betAmount.compareTo(BigDecimal.ZERO) == 0) {
                resultTyep = 2;
            } else if (betAmount.compareTo(BigDecimal.ZERO) > 0) {
                resultTyep = 0;
            } else {
                resultTyep = 1;
            }
            txns.setResultType(resultTyep);
            //有效投注金额 或 投注面值
            txns.setTurnover(BigDecimal.ZERO);
            //辨认交易时间依据
            txns.setTxTime(DateUtils.formatByLong(ppJackpotWinCallBackReq.getTimestamp(), DateUtils.newFormat));
            //操作名称
            txns.setMethod("Settle");
            txns.setStatus("Running");
            //余额
            txns.setBalance(balance);
            //创建时间
            String dateStr = DateUtils.format(new Date(), DateUtils.newFormat);
            //投注 IP
            txns.setBetIp(ip);//  string 是 投注 IP
            txns.setCreateTime(dateStr);
            int num = txnsMapper.insert(txns);
            if (num <= 0) {
                return initFailureResponse(100, "订单入库请求失败",json);
            }
            json.put("cash", balance.divide(platformGameParent.getCurrencyPro()));
            return json;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return initFailureResponse(100, e.getMessage(),json);
        }

    }

    // 活动奖励
    @Override
    public Object promoWin(PpPromoWinCallBackReq ppPromoWinCallBackReq, String ip) {
        logger.info("pp_promowin  ppGame paramJson:{}, ip:{}", JSONObject.toJSONString(ppPromoWinCallBackReq), ip);
        GameParentPlatform platformGameParent = getGameParentPlatform();
        JSONObject json = initSuccessResponse(ppPromoWinCallBackReq.getReference(), platformGameParent.getCurrencyType(), BigDecimal.ZERO);
        // 请求参数与哈希参数对比。如果失败，娱乐场运营商应发送错误代码5
        if (checkReqHash(ppPromoWinCallBackReq, ppPromoWinCallBackReq.getHash())) {
            return initFailureResponse(5, "请求参数非法",json);
        }

        // 校验IP
        if (checkIp(ip, platformGameParent)) {
            return initFailureResponse(5, "非信任來源IP",json);
        }
        try {
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(ppPromoWinCallBackReq.getUserId());
            if (null == memBaseinfo) {
                return initFailureResponse(2, "玩家不存在",json);
            }
            // 会员余额
            BigDecimal balance = memBaseinfo.getBalance();
            GamePlatform gamePlatform = gameCommonService.getGamePlatformByParentName(OpenAPIProperties.PP_PLATFORM_CODE).get(0);
            GameCategory gameCategory = gameCommonService.getGameCategoryById(gamePlatform.getCategoryId());

            // 赢奖金额
            BigDecimal betAmount = null!=ppPromoWinCallBackReq.getAmount()?ppPromoWinCallBackReq.getAmount().multiply(platformGameParent.getCurrencyPro()):BigDecimal.ZERO;

            // 赢奖金额小于0
            if (betAmount.compareTo(BigDecimal.ZERO) < 0) {
                json.put("cash", balance.divide(platformGameParent.getCurrencyPro()));
                return initFailureResponse(7, "赢奖金额不能小0",json);
            }

            // 查询用户请求订单
            Txns txns = getTxns(ppPromoWinCallBackReq.getReference(), memBaseinfo.getAccount());
            if (null != txns) {
                return initSuccessResponse(ppPromoWinCallBackReq.getReference(), platformGameParent.getCurrencyType(), balance);
            }
            txns = new Txns();
            // 会员余额
            balance = balance.add(betAmount);
            // 更新玩家余额
            gameCommonService.updateUserBalance(memBaseinfo, betAmount, GoldchangeEnum.ACTIVITY_GIVE, TradingEnum.INCOME);

            //游戏商注单号
            txns.setPlatformTxId(ppPromoWinCallBackReq.getReference());
            //此交易是否是投注 true是投注 false 否
            txns.setBet(false);
            //玩家 ID
            txns.setUserId(memBaseinfo.getAccount());
            //玩家货币代码
            txns.setCurrency(ppPromoWinCallBackReq.getCurrency());
            //游戏平台的下注项目
//            txns.setBetType(ppPromoWinCallBackReq.getProviderId());
            // 奖金游戏
//            txns.setHasBonusGame(1);
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
            //中奖金额（赢为正数，亏为负数，和为0）或者总输赢
            txns.setWinningAmount(betAmount);
            //玩家下注时间
            txns.setBetTime(DateUtils.formatByLong(ppPromoWinCallBackReq.getTimestamp(), DateUtils.newFormat));
            //真实下注金额,需增加在玩家的金额
            txns.setRealBetAmount(BigDecimal.ZERO);
            //真实返还金额,游戏赢分
            txns.setRealWinAmount(betAmount);
            //返还金额 (包含下注金额)
            txns.setWinAmount(betAmount);
            // 活动派彩
            txns.setAmount(betAmount);
            // 活动ID
            txns.setPromotionId(ppPromoWinCallBackReq.getCampaignId());
            // 活动类型ID
            txns.setPromotionTypeId(ppPromoWinCallBackReq.getCampaignType());
            //赌注的结果 : 赢:0,输:1,平手:2
            int resultTyep;
            if (betAmount.compareTo(BigDecimal.ZERO) == 0) {
                resultTyep = 2;
            } else if (betAmount.compareTo(BigDecimal.ZERO) > 0) {
                resultTyep = 0;
            } else {
                resultTyep = 1;
            }
            txns.setResultType(resultTyep);
            //有效投注金额 或 投注面值
            txns.setTurnover(BigDecimal.ZERO);
            //辨认交易时间依据
            txns.setTxTime(DateUtils.formatByLong(ppPromoWinCallBackReq.getTimestamp(), DateUtils.newFormat));
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
                return initFailureResponse(100, "活动派奖订单入库请求失败",json);
            }
            json.put("cash", balance.divide(platformGameParent.getCurrencyPro()));
            return json;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return initFailureResponse(100, e.getMessage(),json);
        }
    }

    //实时结束游戏回合的交易
    @Override
    public Object endRound(PpEndRoundCallBackReq ppEndRoundCallBackReq, String ip) {
        GameParentPlatform platformGameParent = getGameParentPlatform();
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(ppEndRoundCallBackReq.getUserId());
        JSONObject json = initSuccessResponse();
        if (null == memBaseinfo) {
            return initFailureResponse(2, "玩家不存在",json);
        }
        // 会员余额
        BigDecimal balance = memBaseinfo.getBalance();
        json.put("cash", balance.divide(platformGameParent.getCurrencyPro()));
        json.put("bonus", BigDecimal.ZERO);
        return json;
    }

    // 退款
    @Override
    public Object refund(PpRefundWinCallBackReq ppRefundWinCallBackReq, String ip) {
        logger.info("pp_refund  ppGame paramJson:{}, ip:{}", JSONObject.toJSONString(ppRefundWinCallBackReq), ip);
        JSONObject json = initSuccessResponse();
        json.put("transactionId", ppRefundWinCallBackReq.getReference());
        // 请求参数与哈希参数对比。如果失败，娱乐场运营商应发送错误代码5
        if (checkReqHash(ppRefundWinCallBackReq, ppRefundWinCallBackReq.getHash())) {
            return initFailureResponse(5, "请求参数非法",json);
        }
        GameParentPlatform platformGameParent = getGameParentPlatform();
        // 校验IP
        if (checkIp(ip, platformGameParent)) {
            return initFailureResponse(5, "非信任來源IP",json);
        }

        try {

            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(ppRefundWinCallBackReq.getUserId());
            if (null == memBaseinfo) {
                return initFailureResponse(2, "玩家不存在",json);
            }
            // 构建返回

            // 查询用户请求订单
            Txns oldTxns = getTxns(ppRefundWinCallBackReq.getReference(), memBaseinfo.getAccount());
            if (null == oldTxns) {
                return json;
            }

            // 如果订单已经取消
            if ("Cancel Bet".equals(oldTxns.getMethod())) {
                return json;
            }
            BigDecimal realWinAmount = null!=ppRefundWinCallBackReq.getAmount()?ppRefundWinCallBackReq.getAmount().multiply(platformGameParent.getCurrencyPro()):BigDecimal.ZERO;
            if (null == realWinAmount) {
                realWinAmount = oldTxns.getBetAmount();
            }
            BigDecimal balance = memBaseinfo.getBalance().add(realWinAmount);
            gameCommonService.updateUserBalance(memBaseinfo, realWinAmount, GoldchangeEnum.REFUND, TradingEnum.INCOME);
            String dateStr = DateUtils.format(new Date(), DateUtils.newFormat);

            Txns txns = new Txns();
            BeanUtils.copyProperties(oldTxns, txns);
            txns.setBalance(balance);
            txns.setId(null);
            txns.setStatus("Running");
            txns.setRealWinAmount(realWinAmount);//真实返还金额
            txns.setMethod("Cancel Bet");
            txns.setCreateTime(dateStr);
            txnsMapper.insert(txns);

            oldTxns.setStatus("Cancel Bet");
            oldTxns.setUpdateTime(dateStr);
            txnsMapper.updateById(oldTxns);


            return json;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return initFailureResponse(100, e.getMessage(),json);
        }
    }


    /**
     * 判断请求参数hash是否合法
     *
     * @param object  请求参数对象
     * @param reqHash 请求hash
     * @return boolean
     */
    private boolean checkReqHash(Object object, String reqHash) {
        // 根据请求参数生成HASH
        String reqParamHash = PPHashAESEncrypt.encrypt(object, OpenAPIProperties.PP_API_SECRET_KEY);
        // 与哈希参数对比。如果失败，娱乐场运营商应发送错误代码5
        return StringUtils.isBlank(reqHash) || !reqHash.equals(reqParamHash);
    }
    // 系统将向娱乐场运营商发送玩家需要调整的余额金额
    @Override
    public Object adjustment(PpAdjustmentCallBackReq ppAdjustmentCallBackReq, String ip) {
        logger.info("pp_adjustment  ppGame paramJson:{}, ip:{}", JSONObject.toJSONString(ppAdjustmentCallBackReq), ip);
        GameParentPlatform platformGameParent = getGameParentPlatform();
        JSONObject json = initSuccessResponse(ppAdjustmentCallBackReq.getReference(), platformGameParent.getCurrencyType(), BigDecimal.ZERO);

        // 请求参数与哈希参数对比。如果失败，娱乐场运营商应发送错误代码5
        if (checkReqHash(ppAdjustmentCallBackReq, ppAdjustmentCallBackReq.getHash())) {
            return initFailureResponse(5, "请求参数非法",json);
        }

        // 校验IP
        if (checkIp(ip, platformGameParent)) {
            return initFailureResponse(5, "非信任來源IP",json);
        }

        try {
            MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(ppAdjustmentCallBackReq.getUserId());
            if (null == memBaseinfo) {
                return initFailureResponse(2, "玩家不存在", json);
            }
            // 会员余额
            BigDecimal balance = memBaseinfo.getBalance();
            GamePlatform gamePlatform;
            if ("Y".equals(OpenAPIProperties.PP_IS_PLATFORM_LOGIN)) {
                gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(platformGameParent.getPlatformCode(), platformGameParent.getPlatformCode());
            } else {
                gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(ppAdjustmentCallBackReq.getGameId(), platformGameParent.getPlatformCode());

            }
            if (null == gamePlatform) {
                json.put("cash", balance.divide(platformGameParent.getCurrencyPro()));
                return initFailureResponse(3, "游戏不存在",json);
            }
            GameCategory gameCategory = gameCommonService.getGameCategoryById(gamePlatform.getCategoryId());


            // 下注金额
            BigDecimal betAmount = null!=ppAdjustmentCallBackReq.getAmount()?ppAdjustmentCallBackReq.getAmount().multiply(platformGameParent.getCurrencyPro()):BigDecimal.ZERO;

            // 查询用户请求订单
            Txns oldTxns = getTxns(ppAdjustmentCallBackReq.getReference(), memBaseinfo.getAccount());
            if (null != oldTxns) {
                json.put("cash", balance.divide(platformGameParent.getCurrencyPro()));
                return json;
            }

            // 下注金额小于0
            if (betAmount.compareTo(BigDecimal.ZERO) < 0) {//调整金额小于0
                if (balance.compareTo(betAmount.abs()) < 0) {
                    json.put("cash", balance.divide(platformGameParent.getCurrencyPro()));
                    return initFailureResponse(1, "玩家余额不足", json);
                }
                gameCommonService.updateUserBalance(memBaseinfo, betAmount, GoldchangeEnum.SETTLE, TradingEnum.SPENDING);
            }else {
                // 更新玩家余额
                gameCommonService.updateUserBalance(memBaseinfo, betAmount, GoldchangeEnum.SETTLE, TradingEnum.INCOME);
            }
            balance = balance.add(betAmount);


            Txns txns = new Txns();
            //游戏商注单号
            txns.setPlatformTxId(ppAdjustmentCallBackReq.getReference());
            //此交易是否是投注 true是投注 false 否
            txns.setBet(true);
            //玩家 ID
            txns.setUserId(memBaseinfo.getAccount());
            //玩家货币代码
            txns.setCurrency(platformGameParent.getCurrencyType());
            txns.setRoundId(ppAdjustmentCallBackReq.getRoundId());
            //平台代码
            txns.setPlatform(platformGameParent.getPlatformCode());
            //平台名称
            txns.setPlatformEnName(platformGameParent.getPlatformEnName());
            txns.setPlatformCnName(platformGameParent.getPlatformCnName());
            if (null!=gamePlatform){
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
            }
            //下注金额
            txns.setBetAmount(betAmount);
            //游戏平台的下注项目
            txns.setBetType(ppAdjustmentCallBackReq.getGameId());
            //中奖金额（赢为正数，亏为负数，和为0）或者总输赢
            txns.setWinningAmount(betAmount);
            //玩家下注时间
            txns.setBetTime(DateUtils.formatByLong(ppAdjustmentCallBackReq.getTimestamp(), DateUtils.newFormat));
            //真实下注金额,需增加在玩家的金额
            txns.setRealBetAmount(betAmount);
            //真实返还金额,游戏赢分
            txns.setRealWinAmount(BigDecimal.ZERO);
            //返还金额 (包含下注金额)
            //赌注的结果 : 赢:0,输:1,平手:2
            //有效投注金额 或 投注面值
            txns.setTurnover(betAmount);
            //辨认交易时间依据
            txns.setTxTime(DateUtils.formatByLong(ppAdjustmentCallBackReq.getTimestamp(), DateUtils.newFormat));
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
                return initFailureResponse(100, "订单入库请求失败",json);
            }

            json.put("cash", balance.divide(platformGameParent.getCurrencyPro()));
            return json;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return initFailureResponse(100, e.getMessage(),json);
        }

    }
    /**
     * 获取历史订单
     *
     * @param reference 订单编号
     * @param userId    会员ID
     * @return 订单
     */
    private Txns getTxns(String reference, String userId) {
        LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(c -> c.eq(Txns::getMethod, "Place Bet")
                .or().eq(Txns::getMethod, "Cancel Bet")
                .or().eq(Txns::getMethod, "Settle"));
        wrapper.and(c -> c.eq(Txns::getStatus, "Running")
                .or().eq(Txns::getStatus, "Settle"));
        wrapper.eq(Txns::getPlatformTxId, reference);
        wrapper.eq(Txns::getPlatform, OpenAPIProperties.PP_PLATFORM_CODE);
        wrapper.eq(Txns::getUserId, userId);
        return txnsMapper.selectOne(wrapper);
    }

    private Txns getTxnsJackpotWin(String reference, String userId) {
        LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Txns::getMethod, "Settle");
        wrapper.and(c -> c.eq(Txns::getStatus, "Running")
                .or().eq(Txns::getStatus, "Settle"));
        wrapper.eq(Txns::getPlatformTxId, reference);
        wrapper.eq(Txns::getPlatform, OpenAPIProperties.PP_PLATFORM_CODE);
        wrapper.eq(Txns::getUserId, userId);
        return txnsMapper.selectOne(wrapper);
    }

    private Txns getTxnsByBonusCode(String bonusCode, String userId) {
        LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(c -> c.eq(Txns::getMethod, "Place Bet")
                .or().eq(Txns::getMethod, "Cancel Bet")
                .or().eq(Txns::getMethod, "Settle"));
        wrapper.eq(Txns::getStatus, "Running");
        wrapper.eq(Txns::getRePlatformTxId, bonusCode);
        wrapper.eq(Txns::getPlatform, OpenAPIProperties.PP_PLATFORM_CODE);
        wrapper.eq(Txns::getUserId, userId);
        return txnsMapper.selectOne(wrapper);
    }

    private List<Txns> getTxnsByRoundld(String roundld, String userId) {
        LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(c -> c.eq(Txns::getMethod, "Place Bet")
                .or().eq(Txns::getMethod, "Cancel Bet")
                .or().eq(Txns::getMethod, "Settle"));
        wrapper.eq(Txns::getStatus, "Running");
        wrapper.eq(Txns::getPlatform, OpenAPIProperties.PP_PLATFORM_CODE);
        wrapper.eq(Txns::getRoundId, roundld);
        wrapper.eq(Txns::getUserId, userId);
        return txnsMapper.selectList(wrapper);
    }

    /**
     * 初始化成功json返回
     *
     * @return JSONObject
     */
    private JSONObject initSuccessResponse() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("error", 0);
        jsonObject.put("description", "Success");
        return jsonObject;
    }

    /**
     * 初始化成功json返回
     *
     * @return JSONObject
     */
    private JSONObject initSuccessResponse(String transactionId, String currency, BigDecimal balance) {
        JSONObject json = initSuccessResponse();
        json.put("transactionId", transactionId);
        json.put("currency", currency);
        json.put("cash", balance);
        json.put("bonus", BigDecimal.ZERO);
        return json;
    }


    /**
     * 初始化交互失败返回
     *
     * @param error       错误码
     * @param description 错误描述
     * @return JSONObject
     */
    private JSONObject initFailureResponse(Integer error, String description,JSONObject jsonObject) {
        jsonObject.put("error", error);
        jsonObject.put("description", description);
        return jsonObject;
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

    private GameParentPlatform getGameParentPlatform() {
        return gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.PP_PLATFORM_CODE);
    }
}
