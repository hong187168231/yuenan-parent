package com.indo.game.service.awc.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.indo.common.config.OpenAPIProperties;
import com.indo.common.enums.GoldchangeEnum;
import com.indo.common.enums.TradingEnum;
import com.indo.common.utils.DateUtils;
import com.indo.core.mapper.game.TxnsMapper;
import com.indo.game.pojo.dto.awc.*;
import com.indo.core.pojo.entity.game.GameCategory;
import com.indo.core.pojo.entity.game.GameParentPlatform;
import com.indo.core.pojo.entity.game.GamePlatform;
import com.indo.core.pojo.entity.game.Txns;
import com.indo.game.pojo.vo.callback.awc.AwcCallBackParentRespSuccess;
import com.indo.game.pojo.vo.callback.awc.AwcCallBackRespFail;
import com.indo.game.pojo.vo.callback.awc.AwcCallBackRespSuccess;
import com.indo.game.pojo.vo.callback.awc.AwcGetBalanceRespSuccess;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.awc.AwcCallbackService;
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

@Service
public class AwcCallbackServiceImpl implements AwcCallbackService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private GameCommonService gameCommonService;
    @Autowired
    private TxnsMapper txnsMapper;

    public Object awcCallback(AwcApiRequestParentData awcApiRequestData, String ip) {
        if (!OpenAPIProperties.AWC_API_SECRET_KEY.equals(awcApiRequestData.getKey())) {
            AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
            callBacekFail.setStatus("1008");
            callBacekFail.setDesc("Invalid token!");
            return callBacekFail;
        }
        JSONObject jsonObject = JSONObject.parseObject(String.valueOf(awcApiRequestData.getMessage()));
        String action = jsonObject.getString("action");
        //Get Balance 取得玩家余额
        try {
            if ("getBalance".equals(action)) {
                return getBalance(jsonObject);
            }
            //Place Bet 下注
            if ("bet".equals(action)) {
                return bet(awcApiRequestData, ip);
            }
            //Cancel Bet 取消注单
            if ("cancelBet".equals(action)) {
                return cancelBet(awcApiRequestData, ip);
            }
            //Adjust Bet 调整投注
            if ("adjustBet".equals(action)) {
                return adjustBet(awcApiRequestData, ip);
            }
            //Void Bet 交易作废
            if ("voidBet".equals(action)) {
                return voidBet(awcApiRequestData, ip);
            }
            //Unvoid Bet 取消交易作废
            if ("unvoidBet".equals(action)) {
                return unvoidBet(awcApiRequestData, ip);
            }
            //Refund 返还金额
            if ("refund".equals(action)) {
                return refund(awcApiRequestData, ip);
            }
            //Settle 已结帐派彩
            if ("settle".equals(action)) {
                return settle(awcApiRequestData, ip);
            }
            //Unsettle 取消结帐派彩
            if ("unsettle".equals(action)) {
                return unsettle(awcApiRequestData, ip);
            }
            //Void Settle 结帐单转为无效
            if ("voidSettle".equals(action)) {
                return voidSettle(awcApiRequestData, ip);
            }
            //Unvoid Settle 无效单结账
            if ("unvoidSettle".equals(action)) {
                return unvoidSettle(awcApiRequestData, ip);
            }
            // BetNSettle 下注并直接结算
            if ("betNSettle".equals(action)) {
                return betNSettle(awcApiRequestData, ip);
            }
            // Cancel BetNSettle 取消结算并取消注单
            if ("cancelBetNSettle".equals(action)) {
                return cancelBetNSettle(awcApiRequestData, ip);
            }
            // Free Spin 免费旋转
            if ("freeSpin".equals(action)) {
                return freeSpin(awcApiRequestData, ip);
            }
            //  Give (Promotion Bonus) 活动派彩
            if ("give".equals(action)) {
                return give(awcApiRequestData, ip);
            }
            //  Tip 打赏
            if ("tip".equals(action)) {
                return tip(awcApiRequestData, ip);
            }
            //  Cancel Tip 取消打赏
            if ("cancelTip".equals(action)) {
                return cancelTip(awcApiRequestData, ip);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("awcCallBack {9999Fail} callBack 回调,IP:" + ip + " params:{}", e);
            AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
            callBacekFail.setStatus("9999");
            callBacekFail.setDesc("Fail");
            return callBacekFail;
        }
        return "";
    }

    //Get Balance 取得玩家余额
    private Object getBalance(JSONObject jsonObject) {
        String userId = jsonObject.getString("userId");
        MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(userId);
        if (null == memBaseinfo) {
            AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
            callBacekFail.setStatus("1002");
            callBacekFail.setDesc("Account is not exists");
            return callBacekFail;
        } else {
            GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.AWC_PLATFORM_CODE);
            AwcGetBalanceRespSuccess getBalanceSuccess = new AwcGetBalanceRespSuccess();
            getBalanceSuccess.setStatus("0000");
            getBalanceSuccess.setBalance(memBaseinfo.getBalance().divide(gameParentPlatform.getCurrencyPro()));

            getBalanceSuccess.setBalanceTs(DateUtils.getTimeAndZone());
            getBalanceSuccess.setUserId(userId);
            return getBalanceSuccess;
        }


    }


    //Place Bet 下注
    private Object bet(AwcApiRequestParentData awcApiRequestData, String ip) {
        AwcApiRequestData<List<PlaceBetTxns>> apiRequestData = JSON.parseObject(String.valueOf(awcApiRequestData.getMessage()), new TypeReference<AwcApiRequestData<List<PlaceBetTxns>>>() {
        });
        List<PlaceBetTxns> placeBetTxnsList = apiRequestData.getTxns();
        if (null != placeBetTxnsList && placeBetTxnsList.size() > 0) {
            BigDecimal balance = BigDecimal.ZERO;
            MemTradingBO memBaseinfo = new MemTradingBO();
            GameCategory gameCategory = new GameCategory();
            GamePlatform gamePlatform = new GamePlatform();
            GameParentPlatform gameParentPlatform = new GameParentPlatform();
            for (int i = 0; i < placeBetTxnsList.size(); i++) {
                PlaceBetTxns placeBetTxns = placeBetTxnsList.get(i);
                String platformCode = placeBetTxns.getGameCode();

                if (i == 0) {
                    String userId = placeBetTxns.getUserId();
                    gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.AWC_PLATFORM_CODE);
                    if (!checkIp(gameParentPlatform,ip)) {
                        AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                        callBacekFail.setStatus("1029");
                        callBacekFail.setDesc("invalid IP address.");
                        return callBacekFail;
                    }
                    if(OpenAPIProperties.AWC_IS_PLATFORM_LOGIN.equals("Y")){//平台登录Y 游戏登录N
                        gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(OpenAPIProperties.AWC_PLATFORM_CODE,gameParentPlatform.getPlatformCode());
                    }else {
                        gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(platformCode,gameParentPlatform.getPlatformCode());
                    }
                    gameCategory = gameCommonService.getGameCategoryById(gamePlatform.getCategoryId());
                    memBaseinfo = gameCommonService.getMemTradingInfo(userId);
                    balance = memBaseinfo.getBalance();
                    if (null == memBaseinfo) {
                        AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                        callBacekFail.setStatus("1002");
                        callBacekFail.setDesc("Account is not exists");
                        return callBacekFail;
                    }
                }
                BigDecimal betAmount = null!=placeBetTxns.getBetAmount()?placeBetTxns.getBetAmount().multiply(gameParentPlatform.getCurrencyPro()):BigDecimal.ZERO;
                if (balance.compareTo(betAmount) == -1) {
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1018");
                    callBacekFail.setDesc("Not Enough Balance");
                    return callBacekFail;
                }
                LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
                wrapper.and(c -> c.eq(Txns::getMethod, "Place Bet").or().eq(Txns::getMethod, "Cancel Bet").or().eq(Txns::getMethod, "Adjust Bet"));
                wrapper.eq(Txns::getStatus, "Running");
                wrapper.eq(Txns::getPlatformTxId, placeBetTxns.getPlatformTxId());
                wrapper.eq(Txns::getPlatform, OpenAPIProperties.AWC_PLATFORM_CODE);
                Txns oldTxns = txnsMapper.selectOne(wrapper);
                if (null != oldTxns) {
                    if ("Cancel Bet".equals(oldTxns.getMethod())) {
                        AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                        callBacekFail.setStatus("1043");
                        callBacekFail.setDesc("Bet has canceled.");
                        return callBacekFail;
                    } else {
                        AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                        callBacekFail.setStatus("1038");
                        callBacekFail.setDesc("Duplicate transaction.");
                        return callBacekFail;
                    }
                }


                balance = balance.subtract(betAmount);
                gameCommonService.updateUserBalance(memBaseinfo, betAmount, GoldchangeEnum.PLACE_BET, TradingEnum.SPENDING);

                Txns txns = new Txns();
                BeanUtils.copyProperties(placeBetTxns, txns);
                txns.setBalance(balance);
                txns.setMethod("Place Bet");
                txns.setStatus("Running");
                String dateStr = DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT);
                txns.setCreateTime(dateStr);
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
                //平台游戏类型
                txns.setGameType(gameCategory.getGameType());
                txns.setWinningAmount(betAmount.negate());
                txns.setBetAmount(betAmount);
                txns.setWinAmount(betAmount);
                //游戏分类名称
                txns.setCategoryName(gameCategory.getGameName());
                //平台游戏代码
                txns.setGameCode(gamePlatform.getPlatformCode());
                //游戏名称
                txns.setGameName(gamePlatform.getPlatformEnName());
                txnsMapper.insert(txns);

            }
            AwcCallBackRespSuccess placeBetSuccess = new AwcCallBackRespSuccess();
            placeBetSuccess.setBalanceTs(DateUtils.getTimeAndZone());
            placeBetSuccess.setStatus("0000");
            placeBetSuccess.setBalance(balance.divide(gameParentPlatform.getCurrencyPro()));
            return placeBetSuccess;
        } else {
            AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
            callBacekFail.setStatus("1036");
            callBacekFail.setDesc("Invalid parameters.");
            return callBacekFail;
        }
    }

//        gameCommonService.inOrOutBalanceCommon(GoldchangeEnum.AWCAESEXYBCRT_IN.getValue(), balance, memBaseinfo, content, cptOpenMember, Constants.AWC_AESEXYBCRT_ACCOUNT_TYPE);

    //Cancel Bet 取消注单
    private Object cancelBet(AwcApiRequestParentData awcApiRequestData, String ip) {
        AwcApiRequestData<List<CancelBetTxns>> apiRequestData = JSON.parseObject(String.valueOf(awcApiRequestData.getMessage()), new TypeReference<AwcApiRequestData<List<CancelBetTxns>>>() {
        });
        List<CancelBetTxns> cancelBetTxnsList = apiRequestData.getTxns();
        GameParentPlatform gameParentPlatform = new GameParentPlatform();
        if (null != cancelBetTxnsList && cancelBetTxnsList.size() > 0) {
            BigDecimal balance = BigDecimal.ZERO;
            MemTradingBO memBaseinfo = new MemTradingBO();
            for (int i = 0; i < cancelBetTxnsList.size(); i++) {
                CancelBetTxns cancelBetTxns = cancelBetTxnsList.get(i);

                if (i == 0) {
                    gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.AWC_PLATFORM_CODE);
                    if (!checkIp(gameParentPlatform,ip)) {
                        AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                        callBacekFail.setStatus("1029");
                        callBacekFail.setDesc("invalid IP address.");
                        return callBacekFail;
                    }
                    String userId = cancelBetTxns.getUserId();
                    memBaseinfo = gameCommonService.getMemTradingInfo(userId);
                    balance = memBaseinfo.getBalance();
                    if (null == memBaseinfo) {
                        AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                        callBacekFail.setStatus("1002");
                        callBacekFail.setDesc("Account is not exists");
                        return callBacekFail;
                    }
                }
                LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
                wrapper.and(c -> c.eq(Txns::getMethod, "Place Bet").or().eq(Txns::getMethod, "Cancel Bet").or().eq(Txns::getMethod, "Adjust Bet"));
                wrapper.eq(Txns::getStatus, "Running");
                wrapper.eq(Txns::getPlatformTxId, cancelBetTxns.getPlatformTxId());
                wrapper.eq(Txns::getPlatform, OpenAPIProperties.AWC_PLATFORM_CODE);
                Txns oldTxns = txnsMapper.selectOne(wrapper);
                if (null == oldTxns) {
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1017");
                    callBacekFail.setDesc("TxCode is not exist");
                    return callBacekFail;
                } else if ("Cancel Bet".equals(oldTxns.getMethod())) {
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1043");
                    callBacekFail.setDesc("Bet has canceled.");
                    return callBacekFail;
                }


                //查询下注订单
                BigDecimal betAmount = oldTxns.getBetAmount();
                balance = balance.add(betAmount);
                gameCommonService.updateUserBalance(memBaseinfo, betAmount, GoldchangeEnum.PLACE_BET, TradingEnum.INCOME);

                String dateStr = DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT);


                Txns txns = new Txns();
                BeanUtils.copyProperties(oldTxns, txns);
                txns.setId(null);
                txns.setBalance(balance);
                txns.setMethod("Cancel Bet");
                txns.setStatus("Running");
                txns.setCreateTime(dateStr);
                txnsMapper.insert(txns);
                oldTxns.setStatus("Cancel");
                oldTxns.setUpdateTime(dateStr);
                txnsMapper.updateById(oldTxns);
            }
            AwcCallBackRespSuccess placeBetSuccess = new AwcCallBackRespSuccess();
            placeBetSuccess.setStatus("0000");
            placeBetSuccess.setBalance(balance.divide(gameParentPlatform.getCurrencyPro()));
            placeBetSuccess.setBalanceTs(DateUtils.getTimeAndZone());
            return placeBetSuccess;
        } else {
            AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
            callBacekFail.setStatus("1036");
            callBacekFail.setDesc("Invalid parameters.");
            return callBacekFail;
        }
    }

    //Adjust Bet 调整投注
    private Object adjustBet(AwcApiRequestParentData awcApiRequestData, String ip) {
        AwcApiRequestData<List<AdjustBetTxns>> apiRequestData = JSON.parseObject(String.valueOf(awcApiRequestData.getMessage()), new TypeReference<AwcApiRequestData<List<AdjustBetTxns>>>() {
        });
        List<AdjustBetTxns> adjustBetTxnsList = apiRequestData.getTxns();
        GameParentPlatform gameParentPlatform = new GameParentPlatform();
        if (null != adjustBetTxnsList && adjustBetTxnsList.size() > 0) {
            BigDecimal balance = BigDecimal.ZERO;
            MemTradingBO memBaseinfo = new MemTradingBO();
            for (int i = 0; i < adjustBetTxnsList.size(); i++) {
                AdjustBetTxns adjustBetTxns = adjustBetTxnsList.get(i);

                if (i == 0) {
                    gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.AWC_PLATFORM_CODE);
                    if (!checkIp(gameParentPlatform,ip)) {
                        AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                        callBacekFail.setStatus("1029");
                        callBacekFail.setDesc("invalid IP address.");
                        return callBacekFail;
                    }
                    String userId = adjustBetTxns.getUserId();
                    memBaseinfo = gameCommonService.getMemTradingInfo(userId);
                    balance = memBaseinfo.getBalance();
                    if (null == memBaseinfo) {
                        AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                        callBacekFail.setStatus("1002");
                        callBacekFail.setDesc("Account is not exists");
                        return callBacekFail;
                    }
                }
                LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
                wrapper.and(c -> c.eq(Txns::getMethod, "Place Bet").or().eq(Txns::getMethod, "Cancel Bet"));
                wrapper.eq(Txns::getStatus, "Running");
                wrapper.eq(Txns::getPlatformTxId, adjustBetTxns.getPlatformTxId());
                wrapper.eq(Txns::getPlatform, OpenAPIProperties.AWC_PLATFORM_CODE);
                Txns oldTxns = txnsMapper.selectOne(wrapper);
                if (null == oldTxns) {
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1017");
                    callBacekFail.setDesc("TxCode is not exist");
                    return callBacekFail;
                } else if ("Cancel Bet".equals(oldTxns.getMethod())) {
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1043");
                    callBacekFail.setDesc("Bet has canceled.");
                    return callBacekFail;
                }

                // 下注金额
                BigDecimal realBetAmount = BigDecimal.valueOf(Double.valueOf(adjustBetTxns.getBetAmount())).multiply(gameParentPlatform.getCurrencyPro());
                //需要调整的超收金额，返还因为赔率超收金额 下注金额减去实际的下注金额
                BigDecimal realWinAmount = BigDecimal.valueOf(Double.valueOf(adjustBetTxns.getAdjustAmount())).multiply(gameParentPlatform.getCurrencyPro());
                balance = balance.add(realWinAmount);
                gameCommonService.updateUserBalance(memBaseinfo, realWinAmount, GoldchangeEnum.ADJUST_BET, TradingEnum.INCOME);
                String dateStr = DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT);


                Txns txns = new Txns();
                BeanUtils.copyProperties(oldTxns, txns);
                txns.setId(null);
                txns.setBalance(balance);
                txns.setMethod("Adjust Bet");
                txns.setStatus("Running");
                txns.setRealBetAmount(realBetAmount);//真实下注金额
                txns.setRealWinAmount(realWinAmount);//真实返还金额
                txns.setWinAmount(realBetAmount.add(realWinAmount));//返还金额 (包含下注金额)
                txns.setWinningAmount(realBetAmount.add(realWinAmount).negate());//返还金额 (包含下注金额)
                txns.setCreateTime(dateStr);
                txnsMapper.insert(txns);
                oldTxns.setStatus("Adjust");
                oldTxns.setUpdateTime(dateStr);
                txnsMapper.updateById(oldTxns);


            }
            AwcCallBackRespSuccess placeBetSuccess = new AwcCallBackRespSuccess();
            placeBetSuccess.setStatus("0000");
            placeBetSuccess.setBalance(balance.divide(gameParentPlatform.getCurrencyPro()));
            placeBetSuccess.setBalanceTs(DateUtils.getTimeAndZone());
            return placeBetSuccess;
        } else {
            AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
            callBacekFail.setStatus("1036");
            callBacekFail.setDesc("Invalid parameters.");
            return callBacekFail;
        }
    }

    //Void Bet 交易作废
    private Object voidBet(AwcApiRequestParentData awcApiRequestData, String ip) {
        AwcApiRequestData<List<VoidBetTxns>> apiRequestData = JSON.parseObject(String.valueOf(awcApiRequestData.getMessage()), new TypeReference<AwcApiRequestData<List<VoidBetTxns>>>() {
        });
        List<VoidBetTxns> voidBetTxnsList = apiRequestData.getTxns();
        GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.AWC_PLATFORM_CODE);
        if (!checkIp(gameParentPlatform,ip)) {
            AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
            callBacekFail.setStatus("1029");
            callBacekFail.setDesc("invalid IP address.");
            return callBacekFail;
        }
        AwcCallBackParentRespSuccess callBackSuccess = new AwcCallBackParentRespSuccess();
        callBackSuccess.setStatus("0000");
        if (null != voidBetTxnsList && voidBetTxnsList.size() > 0) {
            for (VoidBetTxns voidBetTxns : voidBetTxnsList) {

                String userId = voidBetTxns.getUserId();
                MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(userId);
                if (null == memBaseinfo) {
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1002");
                    callBacekFail.setDesc("Account is not exists");
                    return callBacekFail;
                } else {
                    LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
                    wrapper.and(c -> c.eq(Txns::getMethod, "Place Bet").or().eq(Txns::getMethod, "Cancel Bet")
                            .or().eq(Txns::getMethod, "Adjust Bet").or().eq(Txns::getMethod, "Void Bet"));
                    wrapper.eq(Txns::getStatus, "Running");
                    wrapper.eq(Txns::getPlatformTxId, voidBetTxns.getPlatformTxId());
                    wrapper.eq(Txns::getPlatform, OpenAPIProperties.AWC_PLATFORM_CODE);
                    Txns oldTxns = txnsMapper.selectOne(wrapper);
                    if (null == oldTxns) {
                        AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                        callBacekFail.setStatus("1017");
                        callBacekFail.setDesc("TxCode is not exist");
                        return callBacekFail;
                    } else if ("Void Bet".equals(oldTxns.getMethod())) {
                        return callBackSuccess;
                    }else if ("Cancel Bet".equals(oldTxns.getMethod())) {
                        AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                        callBacekFail.setStatus("1043");
                        callBacekFail.setDesc("Bet has canceled.");
                        return callBacekFail;
                    }
                    // 下注金额
                    BigDecimal realBetAmount = BigDecimal.valueOf(Double.valueOf(voidBetTxns.getBetAmount())).multiply(gameParentPlatform.getCurrencyPro());
                    BigDecimal balance = memBaseinfo.getBalance().add(realBetAmount);
                    gameCommonService.updateUserBalance(memBaseinfo, realBetAmount, GoldchangeEnum.VOID_BET, TradingEnum.INCOME);
                    String dateStr = DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT);


                    Txns txns = new Txns();
                    BeanUtils.copyProperties(oldTxns, txns);
                    txns.setId(null);
                    txns.setBalance(balance);
                    txns.setMethod("Void Bet");
                    txns.setStatus(oldTxns.getMethod());
                    txns.setRealBetAmount(realBetAmount);
                    txns.setCreateTime(dateStr);
                    txnsMapper.insert(txns);
                    oldTxns.setStatus("Running");
                    oldTxns.setUpdateTime(dateStr);
                    txnsMapper.updateById(oldTxns);

                }
            }

            return callBackSuccess;
        } else {
            AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
            callBacekFail.setStatus("1036");
            callBacekFail.setDesc("Invalid parameters.");
            return callBacekFail;
        }
    }

    //Unvoid Bet 取消交易作废
    private Object unvoidBet(AwcApiRequestParentData awcApiRequestData, String ip) {
        AwcApiRequestData<List<UnvoidBetTxns>> apiRequestData = JSON.parseObject(String.valueOf(awcApiRequestData.getMessage()), new TypeReference<AwcApiRequestData<List<UnvoidBetTxns>>>() {
        });
        List<UnvoidBetTxns> unvoidBetTxnsList = apiRequestData.getTxns();
        GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.AWC_PLATFORM_CODE);
        if (!checkIp(gameParentPlatform,ip)) {
            AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
            callBacekFail.setStatus("1029");
            callBacekFail.setDesc("invalid IP address.");
            return callBacekFail;
        }
        if (null != unvoidBetTxnsList && unvoidBetTxnsList.size() > 0) {
            for (UnvoidBetTxns unvoidBetTxns : unvoidBetTxnsList) {
                String userId = unvoidBetTxns.getUserId();
                MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(userId);
                if (null == memBaseinfo) {
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1002");
                    callBacekFail.setDesc("Account is not exists");
                    return callBacekFail;
                } else {
                    LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
                    wrapper.eq(Txns::getMethod, "Void Bet");
                    wrapper.eq(Txns::getPlatformTxId, unvoidBetTxns.getPlatformTxId());
                    wrapper.eq(Txns::getPlatform, OpenAPIProperties.AWC_PLATFORM_CODE);
                    Txns oldTxns = txnsMapper.selectOne(wrapper);
                    if (null == oldTxns) {
                        AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                        callBacekFail.setStatus("1017");
                        callBacekFail.setDesc("TxCode is not exist");
                        return callBacekFail;
                    }
                    // 下注金额
                    BigDecimal realBetAmount = oldTxns.getRealBetAmount();
                    BigDecimal balance = memBaseinfo.getBalance().subtract(realBetAmount);
                    gameCommonService.updateUserBalance(memBaseinfo, realBetAmount, GoldchangeEnum.UNVOID_BET, TradingEnum.SPENDING);
                    String dateStr = DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT);

                    Txns txns = new Txns();
                    BeanUtils.copyProperties(oldTxns, txns);
                    txns.setId(null);
                    txns.setBalance(balance);
                    txns.setMethod(oldTxns.getStatus());
                    txns.setStatus("Running");
                    txns.setCreateTime(dateStr);
                    txnsMapper.insert(txns);

                }
            }
            AwcCallBackParentRespSuccess callBackSuccess = new AwcCallBackParentRespSuccess();
            callBackSuccess.setStatus("0000");
            return callBackSuccess;
        } else {
            AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
            callBacekFail.setStatus("1036");
            callBacekFail.setDesc("Invalid parameters.");
            return callBacekFail;
        }
    }

    //Refund 返还金额
    private Object refund(AwcApiRequestParentData awcApiRequestData, String ip) {
        AwcApiRequestData<List<RefundTxns>> apiRequestData = JSON.parseObject(String.valueOf(awcApiRequestData.getMessage()), new TypeReference<AwcApiRequestData<List<RefundTxns>>>() {
        });
        List<RefundTxns> refundTxnsList = apiRequestData.getTxns();
        GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.AWC_PLATFORM_CODE);
        if (!checkIp(gameParentPlatform,ip)) {
            AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
            callBacekFail.setStatus("1029");
            callBacekFail.setDesc("invalid IP address.");
            return callBacekFail;
        }
        if (null != refundTxnsList && refundTxnsList.size() > 0) {
            GameCategory gameCategory = new GameCategory();
            GamePlatform gamePlatform = new GamePlatform();
            for (RefundTxns refundTxns : refundTxnsList) {
                String platformCode = refundTxns.getGameCode();
                if(OpenAPIProperties.AWC_IS_PLATFORM_LOGIN.equals("Y")){//平台登录Y 游戏登录N
                    gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(OpenAPIProperties.AWC_PLATFORM_CODE,gameParentPlatform.getPlatformCode());
                }else {
                    gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(platformCode,gameParentPlatform.getPlatformCode());
                }
                gameCategory = gameCommonService.getGameCategoryById(gamePlatform.getCategoryId());
                String userId = refundTxns.getUserId();
                MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(userId);
                if (null == memBaseinfo) {
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1002");
                    callBacekFail.setDesc("Account is not exists");
                    return callBacekFail;
                } else {
                    BigDecimal betAmount = null!=refundTxns.getBetAmount()?refundTxns.getBetAmount().multiply(gameParentPlatform.getCurrencyPro()):BigDecimal.ZERO;
                    BigDecimal reWinAmount = null!=refundTxns.getWinAmount()?refundTxns.getWinAmount().multiply(gameParentPlatform.getCurrencyPro()):BigDecimal.ZERO;
                    BigDecimal winAmount = reWinAmount.subtract(betAmount);
                    BigDecimal balance = memBaseinfo.getBalance().add(winAmount);
                    gameCommonService.updateUserBalance(memBaseinfo, winAmount, GoldchangeEnum.REFUND, TradingEnum.INCOME);
                    String dateStr = DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT);

                    Txns txns = new Txns();
                    BeanUtils.copyProperties(refundTxns, txns);
                    txns.setId(null);
                    txns.setBalance(balance);
                    txns.setMethod("Refund");
                    txns.setStatus("Running");
                    txns.setCreateTime(dateStr);
                    txns.setWinningAmount(winAmount);
                    txns.setWinAmount(winAmount);
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
                    txnsMapper.insert(txns);
                }
            }
            AwcCallBackParentRespSuccess callBackSuccess = new AwcCallBackParentRespSuccess();
            callBackSuccess.setStatus("0000");
            return callBackSuccess;
        } else {
            AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
            callBacekFail.setStatus("1036");
            callBacekFail.setDesc("Invalid parameters.");
            return callBacekFail;
        }
    }

    //Settle 已结帐派彩
    private Object settle(AwcApiRequestParentData awcApiRequestData, String ip) {
        AwcApiRequestData<List<SettleTxns>> apiRequestData = JSON.parseObject(String.valueOf(awcApiRequestData.getMessage()), new TypeReference<AwcApiRequestData<List<SettleTxns>>>() {
        });
        List<SettleTxns> settleTxnsList = apiRequestData.getTxns();
        GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.AWC_PLATFORM_CODE);
        if (!checkIp(gameParentPlatform,ip)) {
            AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
            callBacekFail.setStatus("1029");
            callBacekFail.setDesc("invalid IP address.");
            return callBacekFail;
        }
        AwcCallBackParentRespSuccess callBackSuccess = new AwcCallBackParentRespSuccess();
        callBackSuccess.setStatus("0000");
        if (null != settleTxnsList && settleTxnsList.size() > 0) {
            for (SettleTxns settleTxns : settleTxnsList) {
                String platformCode = settleTxns.getGameCode();
                String userId = settleTxns.getUserId();
                MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(userId);
                if (null == memBaseinfo) {
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1002");
                    callBacekFail.setDesc("Account is not exists");
                    return callBacekFail;
                } else {
                    LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
                    wrapper.and(c -> c.eq(Txns::getMethod, "Place Bet").or().eq(Txns::getMethod, "Unsettle")
                            .or().eq(Txns::getMethod, "Adjust Bet").or().eq(Txns::getMethod, "Settle"));
                    wrapper.eq(Txns::getStatus, "Running");
                    wrapper.eq(Txns::getPlatformTxId, settleTxns.getPlatformTxId());
                    wrapper.eq(Txns::getPlatform, OpenAPIProperties.AWC_PLATFORM_CODE);
                    Txns oldTxns = txnsMapper.selectOne(wrapper);
                    if (null == oldTxns) {
                        AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                        callBacekFail.setStatus("1017");
                        callBacekFail.setDesc("TxCode is not exist");
                        return callBacekFail;
                    }else if("Settle".equals(oldTxns.getMethod())) {
                        return callBackSuccess;
                    }else {

                        BigDecimal winAmount = null!=settleTxns.getWinAmount()?settleTxns.getWinAmount().multiply(gameParentPlatform.getCurrencyPro()):BigDecimal.ZERO;
                        BigDecimal balance = memBaseinfo.getBalance().add(winAmount);
                        gameCommonService.updateUserBalance(memBaseinfo, winAmount, GoldchangeEnum.SETTLE, TradingEnum.INCOME);
                        String dateStr = DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT);

                        Txns txns = new Txns();
                        BeanUtils.copyProperties(oldTxns, txns);
//                        BeanUtils.copyProperties(settleTxns, txns);
                        txns.setId(null);
                        txns.setBalance(balance);
                        txns.setWinningAmount(winAmount);
                        txns.setWinAmount(winAmount);
                        txns.setMethod("Settle");
                        txns.setStatus("Running");
                        txns.setCreateTime(dateStr);
                        txnsMapper.insert(txns);
                        oldTxns.setStatus("Settle");
                        oldTxns.setUpdateTime(dateStr);
                        txnsMapper.updateById(oldTxns);

                    }
                }
            }
            return callBackSuccess;
        } else {
            AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
            callBacekFail.setStatus("1036");
            callBacekFail.setDesc("Invalid parameters.");
            return callBacekFail;
        }
    }

    //Unsettle 取消结帐派彩
    private Object unsettle(AwcApiRequestParentData awcApiRequestData, String ip) {
        AwcApiRequestData<List<UnsettleTxns>> apiRequestData = JSON.parseObject(String.valueOf(awcApiRequestData.getMessage()), new TypeReference<AwcApiRequestData<List<UnsettleTxns>>>() {
        });
        List<UnsettleTxns> unsettleTxnsList = apiRequestData.getTxns();
        GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.AWC_PLATFORM_CODE);
        if (!checkIp(gameParentPlatform,ip)) {
            AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
            callBacekFail.setStatus("1029");
            callBacekFail.setDesc("invalid IP address.");
            return callBacekFail;
        }
        if (null != unsettleTxnsList && unsettleTxnsList.size() > 0) {
            for (UnsettleTxns unsettleTxns : unsettleTxnsList) {
                String userId = unsettleTxns.getUserId();
                MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(userId);
                if (null == memBaseinfo) {
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1002");
                    callBacekFail.setDesc("Account is not exists");
                    return callBacekFail;
                } else {
                    LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
                    wrapper.eq(Txns::getMethod, "Settle");
                    wrapper.eq(Txns::getStatus, "Running");
                    wrapper.eq(Txns::getPlatformTxId, unsettleTxns.getPlatformTxId());
                    wrapper.eq(Txns::getPlatform, OpenAPIProperties.AWC_PLATFORM_CODE);
                    Txns oldTxns = txnsMapper.selectOne(wrapper);
                    if (null == oldTxns) {
                        AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                        callBacekFail.setStatus("1017");
                        callBacekFail.setDesc("TxCode is not exist");
                        return callBacekFail;
                    }
                    BigDecimal winAmount = oldTxns.getWinAmount();
                    BigDecimal balance = memBaseinfo.getBalance().subtract(winAmount);
                    gameCommonService.updateUserBalance(memBaseinfo, winAmount, GoldchangeEnum.UNSETTLE, TradingEnum.SPENDING);
                    String dateStr = DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT);

                    Txns txns = new Txns();
                    BeanUtils.copyProperties(oldTxns, txns);
                    txns.setId(null);
                    txns.setBalance(balance);
                    txns.setMethod("Unsettle");
                    txns.setStatus("Running");
                    txns.setCreateTime(dateStr);
                    txnsMapper.insert(txns);
                    oldTxns.setStatus("Unsettle");
                    oldTxns.setUpdateTime(dateStr);
                    txnsMapper.updateById(oldTxns);

                }
            }
            AwcCallBackParentRespSuccess callBackSuccess = new AwcCallBackParentRespSuccess();
            callBackSuccess.setStatus("0000");
            return callBackSuccess;
        } else {
            AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
            callBacekFail.setStatus("1036");
            callBacekFail.setDesc("Invalid parameters.");
            return callBacekFail;
        }
    }

    //Void Settle 结帐单转为无效
    private Object voidSettle(AwcApiRequestParentData awcApiRequestData, String ip) {
        AwcApiRequestData<List<VoidSettleTxns>> apiRequestData = JSON.parseObject(String.valueOf(awcApiRequestData.getMessage()), new TypeReference<AwcApiRequestData<List<VoidSettleTxns>>>() {
        });
        List<VoidSettleTxns> voidSettleTxnsList = apiRequestData.getTxns();
        GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.AWC_PLATFORM_CODE);
        if (!checkIp(gameParentPlatform,ip)) {
            AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
            callBacekFail.setStatus("1029");
            callBacekFail.setDesc("invalid IP address.");
            return callBacekFail;
        }
        if (null != voidSettleTxnsList && voidSettleTxnsList.size() > 0) {
            for (VoidSettleTxns voidSettleTxns : voidSettleTxnsList) {
                String userId = voidSettleTxns.getUserId();
                MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(userId);
                if (null == memBaseinfo) {
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1002");
                    callBacekFail.setDesc("Account is not exists");
                    return callBacekFail;
                } else {
                    LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
                    wrapper.eq(Txns::getMethod, "Settle");
                    wrapper.eq(Txns::getStatus, "Running");
                    wrapper.eq(Txns::getPlatformTxId, voidSettleTxns.getPlatformTxId());
                    wrapper.eq(Txns::getPlatform, OpenAPIProperties.AWC_PLATFORM_CODE);
                    Txns oldTxns = txnsMapper.selectOne(wrapper);
                    if (null == oldTxns) {
                        AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                        callBacekFail.setStatus("1017");
                        callBacekFail.setDesc("TxCode is not exist");
                        return callBacekFail;
                    }
                    BigDecimal winAmount = oldTxns.getWinAmount();
                    BigDecimal balance = memBaseinfo.getBalance().subtract(winAmount);
                    gameCommonService.updateUserBalance(memBaseinfo, winAmount, GoldchangeEnum.VOID_SETTLE, TradingEnum.SPENDING);
                    String dateStr = DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT);

                    Txns txns = new Txns();
                    BeanUtils.copyProperties(oldTxns, txns);
                    txns.setId(null);
                    txns.setBalance(balance);
                    txns.setMethod("Void Settle");
                    txns.setStatus("Running");
                    txns.setCreateTime(dateStr);
                    txnsMapper.insert(txns);
                    oldTxns.setStatus("Void Settle");
                    oldTxns.setUpdateTime(dateStr);
                    txnsMapper.updateById(oldTxns);

                }
            }
            AwcCallBackParentRespSuccess callBackSuccess = new AwcCallBackParentRespSuccess();
            callBackSuccess.setStatus("0000");
            return callBackSuccess;
        } else {
            AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
            callBacekFail.setStatus("1036");
            callBacekFail.setDesc("Invalid parameters.");
            return callBacekFail;
        }
    }

    //Unvoid Settle 无效单结账
    private Object unvoidSettle(AwcApiRequestParentData awcApiRequestData, String ip) {
        AwcApiRequestData<List<UnvoidSettleTxns>> apiRequestData = JSON.parseObject(String.valueOf(awcApiRequestData.getMessage()), new TypeReference<AwcApiRequestData<List<UnvoidSettleTxns>>>() {
        });
        List<UnvoidSettleTxns> unvoidSettleTxnsList = apiRequestData.getTxns();
        MemTradingBO memBaseinfo = new MemTradingBO();
        GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.AWC_PLATFORM_CODE);
        if (!checkIp(gameParentPlatform,ip)) {
            AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
            callBacekFail.setStatus("1029");
            callBacekFail.setDesc("invalid IP address.");
            return callBacekFail;
        }
        if (null != unvoidSettleTxnsList && unvoidSettleTxnsList.size() > 0) {
            BigDecimal balance = BigDecimal.ZERO;
            for (int i = 0; i < unvoidSettleTxnsList.size(); i++) {
                UnvoidSettleTxns unvoidSettleTxns = unvoidSettleTxnsList.get(i);
                if (i == 0) {
                    String userId = unvoidSettleTxns.getUserId();
                    memBaseinfo = gameCommonService.getMemTradingInfo(userId);
                    balance = memBaseinfo.getBalance();
                    if (null == memBaseinfo) {
                        AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                        callBacekFail.setStatus("1002");
                        callBacekFail.setDesc("Account is not exists");
                        return callBacekFail;
                    }
                }

                LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(Txns::getMethod, "Void Settle");
                wrapper.eq(Txns::getStatus, "Running");
                wrapper.eq(Txns::getPlatformTxId, unvoidSettleTxns.getPlatformTxId());
                wrapper.eq(Txns::getPlatform, OpenAPIProperties.AWC_PLATFORM_CODE);
                Txns oldTxns = txnsMapper.selectOne(wrapper);
                if (null == oldTxns) {
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1017");
                    callBacekFail.setDesc("TxCode is not exist");
                    return callBacekFail;
                }
                BigDecimal winAmount = oldTxns.getWinAmount();
                balance = balance.add(winAmount);
                gameCommonService.updateUserBalance(memBaseinfo, winAmount, GoldchangeEnum.UNVOID_SETTLE, TradingEnum.INCOME);
                String dateStr = DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT);

                Txns txns = new Txns();
                BeanUtils.copyProperties(oldTxns, txns);
                txns.setId(null);
                txns.setBalance(balance);
                txns.setMethod("Settle");
                txns.setStatus("Running");
                txns.setCreateTime(dateStr);
                txnsMapper.insert(txns);
                oldTxns.setStatus("Unvoid Settle");
                oldTxns.setUpdateTime(dateStr);
                txnsMapper.updateById(oldTxns);

            }
            AwcCallBackRespSuccess placeBetSuccess = new AwcCallBackRespSuccess();
            placeBetSuccess.setStatus("0000");
            placeBetSuccess.setBalance(balance.divide(gameParentPlatform.getCurrencyPro()));
            placeBetSuccess.setBalanceTs(DateUtils.getTimeAndZone());
            return placeBetSuccess;
        } else {
            AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
            callBacekFail.setStatus("1036");
            callBacekFail.setDesc("Invalid parameters.");
            return callBacekFail;
        }
    }

    // BetNSettle 下注并直接结算
    private Object betNSettle(AwcApiRequestParentData awcApiRequestData, String ip) {
        AwcApiRequestData<List<BetNSettleTxns>> apiRequestData = JSON.parseObject(String.valueOf(awcApiRequestData.getMessage()), new TypeReference<AwcApiRequestData<List<BetNSettleTxns>>>() {
        });
        List<BetNSettleTxns> betNSettleTxnsList = apiRequestData.getTxns();
        GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.AWC_PLATFORM_CODE);
        if (!checkIp(gameParentPlatform,ip)) {
            AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
            callBacekFail.setStatus("1029");
            callBacekFail.setDesc("invalid IP address.");
            return callBacekFail;
        }
        if (null != betNSettleTxnsList && betNSettleTxnsList.size() > 0) {
            BigDecimal balance = BigDecimal.ZERO;
            MemTradingBO memBaseinfo = new MemTradingBO();
            GameCategory gameCategory = new GameCategory();
            GamePlatform gamePlatform = new GamePlatform();
            for (int i = 0; i < betNSettleTxnsList.size(); i++) {
                BetNSettleTxns betNSettleTxns = betNSettleTxnsList.get(i);
                String platformCode = betNSettleTxns.getGameCode();
                if (i == 0) {
                    if(OpenAPIProperties.AWC_IS_PLATFORM_LOGIN.equals("Y")){//平台登录Y 游戏登录N
                        gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(OpenAPIProperties.AWC_PLATFORM_CODE,gameParentPlatform.getPlatformCode());
                    }else {
                        gamePlatform = gameCommonService.getGamePlatformByplatformCodeAndParentName(platformCode,gameParentPlatform.getPlatformCode());
                    }
                    gameCategory = gameCommonService.getGameCategoryById(gamePlatform.getCategoryId());
                    String userId = betNSettleTxns.getUserId();
                    memBaseinfo = gameCommonService.getMemTradingInfo(userId);
                    balance = memBaseinfo.getBalance();
                    if (null == memBaseinfo) {
                        AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                        callBacekFail.setStatus("1002");
                        callBacekFail.setDesc("Account is not exists");
                        return callBacekFail;
                    }
                }
                BigDecimal winAmount = null!=betNSettleTxns.getWinAmount()?betNSettleTxns.getWinAmount().multiply(gameParentPlatform.getCurrencyPro()):BigDecimal.ZERO;
                if (memBaseinfo.getBalance().compareTo(winAmount) == -1) {
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1018");
                    callBacekFail.setDesc("Not Enough Balance");
                    return callBacekFail;
                }
                LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
                wrapper.and(c -> c.eq(Txns::getMethod, "BetNSettle").or().eq(Txns::getMethod, "Cancel BetNSettle"));
                wrapper.eq(Txns::getStatus, "Running");
                wrapper.eq(Txns::getPlatformTxId, betNSettleTxns.getPlatformTxId());
                wrapper.eq(Txns::getPlatform, OpenAPIProperties.AWC_PLATFORM_CODE);
                Txns oldTxns = txnsMapper.selectOne(wrapper);
                if (null != oldTxns) {
                    if ("Cancel BetNSettle".equals(oldTxns.getMethod())) {
                        AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                        callBacekFail.setStatus("1043");
                        callBacekFail.setDesc("Bet has canceled.");
                        return callBacekFail;
                    } else {
                        AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                        callBacekFail.setStatus("1038");
                        callBacekFail.setDesc("Duplicate transaction.");
                        return callBacekFail;
                    }
                }

                balance = balance.add(winAmount);
                gameCommonService.updateUserBalance(memBaseinfo, winAmount, GoldchangeEnum.BETNSETTLE, TradingEnum.INCOME);
                String dateStr = DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT);
                Txns txns = new Txns();
                BeanUtils.copyProperties(betNSettleTxns, txns);
                txns.setId(null);
                txns.setBalance(balance);
                txns.setMethod("BetNSettle");
                txns.setStatus("Running");
                txns.setCreateTime(dateStr);
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
                txnsMapper.insert(txns);
            }
            AwcCallBackRespSuccess placeBetSuccess = new AwcCallBackRespSuccess();
            placeBetSuccess.setStatus("0000");
            placeBetSuccess.setBalance(balance.divide(gameParentPlatform.getCurrencyPro()));
            placeBetSuccess.setBalanceTs(DateUtils.getTimeAndZone());
            return placeBetSuccess;
        } else {
            AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
            callBacekFail.setStatus("1036");
            callBacekFail.setDesc("Invalid parameters.");
            return callBacekFail;
        }
    }

    // Cancel BetNSettle 取消结算并取消注单
    private Object cancelBetNSettle(AwcApiRequestParentData awcApiRequestData, String ip) {
        AwcApiRequestData<List<CancelBetNSettleTxns>> apiRequestData = JSON.parseObject(String.valueOf(awcApiRequestData.getMessage()), new TypeReference<AwcApiRequestData<List<CancelBetNSettleTxns>>>() {
        });
        List<CancelBetNSettleTxns> cancelBetNSettleTxnsList = apiRequestData.getTxns();
        GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.AWC_PLATFORM_CODE);
        if (!checkIp(gameParentPlatform,ip)) {
            AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
            callBacekFail.setStatus("1029");
            callBacekFail.setDesc("invalid IP address.");
            return callBacekFail;
        }
        if (null != cancelBetNSettleTxnsList && cancelBetNSettleTxnsList.size() > 0) {
            BigDecimal balance = BigDecimal.ZERO;
            MemTradingBO memBaseinfo = new MemTradingBO();
            for (int i = 0; i < cancelBetNSettleTxnsList.size(); i++) {
                CancelBetNSettleTxns cancelBetNSettleTxns = cancelBetNSettleTxnsList.get(i);
                if (i == 0) {
                    String userId = cancelBetNSettleTxns.getUserId();
                    memBaseinfo = gameCommonService.getMemTradingInfo(userId);
                    balance = memBaseinfo.getBalance();
                    if (null == memBaseinfo) {
                        AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                        callBacekFail.setStatus("1002");
                        callBacekFail.setDesc("Account is not exists");
                        return callBacekFail;
                    }
                }
                LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(Txns::getMethod, "BetNSettle");
                wrapper.eq(Txns::getStatus, "Running");
                wrapper.eq(Txns::getPlatformTxId, cancelBetNSettleTxns.getPlatformTxId());
                wrapper.eq(Txns::getPlatform, OpenAPIProperties.AWC_PLATFORM_CODE);
                Txns oldTxns = txnsMapper.selectOne(wrapper);
                if (null == oldTxns) {
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1017");
                    callBacekFail.setDesc("TxCode is not exist");
                    return callBacekFail;
                }
                BigDecimal winAmount = oldTxns.getWinAmount();
                balance = balance.add(winAmount);
                gameCommonService.updateUserBalance(memBaseinfo, winAmount, GoldchangeEnum.CANCEL_BETNSETTLE, TradingEnum.SPENDING);
                String dateStr = DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT);

                Txns txns = new Txns();
                BeanUtils.copyProperties(oldTxns, txns);
                txns.setId(null);
                txns.setBalance(balance);
                txns.setMethod("Cancel BetNSettle");
                txns.setStatus("Running");
                txns.setCreateTime(dateStr);
                txnsMapper.insert(txns);
                oldTxns.setStatus("Cancel BetNSettle");
                oldTxns.setUpdateTime(dateStr);
                txnsMapper.updateById(oldTxns);

            }
            AwcCallBackRespSuccess placeBetSuccess = new AwcCallBackRespSuccess();
            placeBetSuccess.setStatus("0000");
            placeBetSuccess.setBalance(balance.divide(gameParentPlatform.getCurrencyPro()));
            placeBetSuccess.setBalanceTs(DateUtils.getTimeAndZone());
            return placeBetSuccess;
        } else {
            AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
            callBacekFail.setStatus("1036");
            callBacekFail.setDesc("Invalid parameters.");
            return callBacekFail;
        }
    }

    // Free Spin 免费旋转
    private Object freeSpin(AwcApiRequestParentData awcApiRequestData, String ip) {
        AwcApiRequestData<List<FreeSpinTxns>> apiRequestData = JSON.parseObject(String.valueOf(awcApiRequestData.getMessage()), new TypeReference<AwcApiRequestData<List<FreeSpinTxns>>>() {
        });
        List<FreeSpinTxns> freeSpinTxnsList = apiRequestData.getTxns();
        GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.AWC_PLATFORM_CODE);
        if (!checkIp(gameParentPlatform,ip)) {
            AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
            callBacekFail.setStatus("1029");
            callBacekFail.setDesc("invalid IP address.");
            return callBacekFail;
        }
        if (null != freeSpinTxnsList && freeSpinTxnsList.size() > 0) {
            for (FreeSpinTxns freeSpinTxns : freeSpinTxnsList) {
                String userId = freeSpinTxns.getUserId();
                MemTradingBO memBaseinfo = gameCommonService.getMemTradingInfo(userId);
                if (null == memBaseinfo) {
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1002");
                    callBacekFail.setDesc("Account is not exists");
                    return callBacekFail;
                } else {
                    BigDecimal winAmount = null!=freeSpinTxns.getWinAmount()?freeSpinTxns.getWinAmount().multiply(gameParentPlatform.getCurrencyPro()):BigDecimal.ZERO;
                    if (memBaseinfo.getBalance().compareTo(winAmount) == -1) {
                        AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                        callBacekFail.setStatus("1018");
                        callBacekFail.setDesc("Not Enough Balance");
                        return callBacekFail;
                    }
                    LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
                    wrapper.eq(Txns::getMethod, "Free Spin");
                    wrapper.eq(Txns::getStatus, "Running");
                    wrapper.eq(Txns::getPlatformTxId, freeSpinTxns.getPlatformTxId());
                    wrapper.eq(Txns::getPlatform, OpenAPIProperties.AWC_PLATFORM_CODE);
                    Txns oldTxns = txnsMapper.selectOne(wrapper);
                    if (null != oldTxns) {
                        AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                        callBacekFail.setStatus("1038");
                        callBacekFail.setDesc("Duplicate transaction.");
                        return callBacekFail;
                    }

                    BigDecimal balance = memBaseinfo.getBalance().add(winAmount);
                    gameCommonService.updateUserBalance(memBaseinfo, winAmount, GoldchangeEnum.FREE_SPIN, TradingEnum.INCOME);
                    String dateStr = DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT);
                    Txns txns = new Txns();
                    BeanUtils.copyProperties(freeSpinTxns, txns);
                    txns.setId(null);
                    txns.setBalance(balance);
                    txns.setMethod("Free Spin");
                    txns.setStatus("Running");
                    txns.setCreateTime(dateStr);
                    txnsMapper.insert(txns);

                }
            }
            AwcCallBackParentRespSuccess callBackSuccess = new AwcCallBackParentRespSuccess();
            callBackSuccess.setStatus("0000");
            return callBackSuccess;
        } else {
            AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
            callBacekFail.setStatus("1036");
            callBacekFail.setDesc("Invalid parameters.");
            return callBacekFail;
        }
    }

    //  Give (Promotion Bonus) 活动派彩
    private Object give(AwcApiRequestParentData awcApiRequestData, String ip) {
        AwcApiRequestData<List<GiveTxns>> apiRequestData = JSON.parseObject(String.valueOf(awcApiRequestData.getMessage()), new TypeReference<AwcApiRequestData<List<GiveTxns>>>() {
        });
        GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.AWC_PLATFORM_CODE);
        if (!checkIp(gameParentPlatform,ip)) {
            AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
            callBacekFail.setStatus("1029");
            callBacekFail.setDesc("invalid IP address.");
            return callBacekFail;
        }
        List<GiveTxns> giveTxnsList = apiRequestData.getTxns();
        if (null != giveTxnsList && giveTxnsList.size() > 0) {
            BigDecimal balance = BigDecimal.ZERO;
            MemTradingBO memBaseinfo = new MemTradingBO();
//            GameCategory gameCategory = new GameCategory();
//            GamePlatform gamePlatform = new GamePlatform();
            for (int i = 0; i < giveTxnsList.size(); i++) {
                GiveTxns giveTxns = giveTxnsList.get(i);
//                String platformCode = giveTxns.getPlatform()+"_"+giveTxns.getGameType();
//                if (!checkIp(ip)) {
//                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
//                    callBacekFail.setStatus("1029");
//                    callBacekFail.setDesc("invalid IP address.");
//                    return callBacekFail;
//                }
                if (i == 0) {
//                    gamePlatform = gameCommonService.getGamePlatformByplatformCode(platformCode);
//                    gameCategory = gameCommonService.getGameCategoryById(gamePlatform.getCategoryId());
                    String userId = giveTxns.getUserId();
                    memBaseinfo = gameCommonService.getMemTradingInfo(userId);
                    balance = memBaseinfo.getBalance();
                    if (null == memBaseinfo) {
                        AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                        callBacekFail.setStatus("1002");
                        callBacekFail.setDesc("Account is not exists");
                        return callBacekFail;
                    }
                }
                LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(Txns::getMethod, "Give");
                wrapper.eq(Txns::getStatus, "Running");
                wrapper.eq(Txns::getPromotionTxId, giveTxns.getPromotionTxId());
                wrapper.eq(Txns::getPlatform, OpenAPIProperties.AWC_PLATFORM_CODE);
                Txns oldTxns = txnsMapper.selectOne(wrapper);
                if (null != oldTxns) {
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1038");
                    callBacekFail.setDesc("Duplicate transaction.");
                    return callBacekFail;
                }
                BigDecimal amount = BigDecimal.valueOf(Double.valueOf(giveTxns.getAmount())).multiply(gameParentPlatform.getCurrencyPro());
                balance = balance.add(amount);
                gameCommonService.updateUserBalance(memBaseinfo, amount, GoldchangeEnum.ACTIVITY_GIVE, TradingEnum.INCOME);
                String dateStr = DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT);
                Txns txns = new Txns();
                BeanUtils.copyProperties(giveTxns, txns);
                txns.setBalance(balance);
                txns.setMethod("Give");
                txns.setStatus("Running");
                txns.setCreateTime(dateStr);
//                txns.setPlatformCnName(gamePlatform.getPlatformCnName());
//                txns.setPlatformEnName(gamePlatform.getPlatformEnName());
//                txns.setCategoryId(gameCategory.getId());
//                txns.setCategoryName(gameCategory.getGameName());
                txnsMapper.insert(txns);

            }
            AwcCallBackRespSuccess placeBetSuccess = new AwcCallBackRespSuccess();
            placeBetSuccess.setStatus("0000");
            placeBetSuccess.setBalance(balance.divide(gameParentPlatform.getCurrencyPro()));
            placeBetSuccess.setBalanceTs(DateUtils.getTimeAndZone());
            return placeBetSuccess;
        } else {
            AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
            callBacekFail.setStatus("1036");
            callBacekFail.setDesc("Invalid parameters.");
            return callBacekFail;
        }
    }

    //  Tip 打赏
    private Object tip(AwcApiRequestParentData awcApiRequestData, String ip) {
        AwcApiRequestData<List<TipTxns>> apiRequestData = JSON.parseObject(String.valueOf(awcApiRequestData.getMessage()), new TypeReference<AwcApiRequestData<List<TipTxns>>>() {
        });
        List<TipTxns> tipTxnsList = apiRequestData.getTxns();
        GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.AWC_PLATFORM_CODE);
        if (!checkIp(gameParentPlatform,ip)) {
            AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
            callBacekFail.setStatus("1029");
            callBacekFail.setDesc("invalid IP address.");
            return callBacekFail;
        }
        if (null != tipTxnsList && tipTxnsList.size() > 0) {
            BigDecimal balance = BigDecimal.ZERO;
            MemTradingBO memBaseinfo = new MemTradingBO();
            for (int i = 0; i < tipTxnsList.size(); i++) {
                TipTxns tipTxns = tipTxnsList.get(i);
                if (i == 0) {
                    String userId = tipTxns.getUserId();
                    memBaseinfo = gameCommonService.getMemTradingInfo(userId);
                    balance = memBaseinfo.getBalance();
                    if (null == memBaseinfo) {
                        AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                        callBacekFail.setStatus("1002");
                        callBacekFail.setDesc("Account is not exists");
                        return callBacekFail;
                    }
                }
                //打赏给直播主的金额
                BigDecimal tip = null!=tipTxns.getTip()?tipTxns.getTip().multiply(gameParentPlatform.getCurrencyPro()):BigDecimal.ZERO;
                if (memBaseinfo.getBalance().compareTo(tip) == -1) {
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1018");
                    callBacekFail.setDesc("Not Enough Balance");
                    return callBacekFail;
                }
                LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
                wrapper.and(c -> c.eq(Txns::getMethod, "Tip").or().eq(Txns::getMethod, "Cancel Tip"));
                wrapper.eq(Txns::getStatus, "Running");
                wrapper.eq(Txns::getPlatformTxId, tipTxns.getPlatformTxId());
                wrapper.eq(Txns::getPlatform, OpenAPIProperties.AWC_PLATFORM_CODE);
                Txns oldTxns = txnsMapper.selectOne(wrapper);
                if (null != oldTxns) {
                    if ("Cancel Tip".equals(oldTxns.getMethod())) {
                        AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                        callBacekFail.setStatus("1043");
                        callBacekFail.setDesc("Bet has canceled.");
                        return callBacekFail;
                    } else {
                        AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                        callBacekFail.setStatus("1038");
                        callBacekFail.setDesc("Duplicate transaction.");
                        return callBacekFail;
                    }
                }

                balance = balance.subtract(tip);
                gameCommonService.updateUserBalance(memBaseinfo, tip, GoldchangeEnum.TIP, TradingEnum.SPENDING);
                String dateStr = DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT);
                Txns txns = new Txns();
                BeanUtils.copyProperties(tipTxns, txns);
                txns.setBalance(balance);
                txns.setMethod("Tip");
                txns.setStatus("Running");
                txns.setCreateTime(dateStr);
                txnsMapper.insert(txns);

            }
            AwcCallBackRespSuccess placeBetSuccess = new AwcCallBackRespSuccess();
            placeBetSuccess.setStatus("0000");
            placeBetSuccess.setBalance(balance.divide(gameParentPlatform.getCurrencyPro()));
            placeBetSuccess.setBalanceTs(DateUtils.getTimeAndZone());
            return placeBetSuccess;
        } else {
            AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
            callBacekFail.setStatus("1036");
            callBacekFail.setDesc("Invalid parameters.");
            return callBacekFail;
        }
    }

    //  Cancel Tip 取消打赏
    private Object cancelTip(AwcApiRequestParentData awcApiRequestData, String ip) {
        AwcApiRequestData<List<CancelTipTxns>> apiRequestData = JSON.parseObject(String.valueOf(awcApiRequestData.getMessage()), new TypeReference<AwcApiRequestData<List<CancelTipTxns>>>() {
        });
        List<CancelTipTxns> cancelTipTxnsList = apiRequestData.getTxns();
        GameParentPlatform gameParentPlatform = gameCommonService.getGameParentPlatformByplatformCode(OpenAPIProperties.AWC_PLATFORM_CODE);
        if (!checkIp(gameParentPlatform,ip)) {
            AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
            callBacekFail.setStatus("1029");
            callBacekFail.setDesc("invalid IP address.");
            return callBacekFail;
        }
        if (null != cancelTipTxnsList && cancelTipTxnsList.size() > 0) {
            BigDecimal balance = BigDecimal.ZERO;
            MemTradingBO memBaseinfo = new MemTradingBO();
            for (int i = 0; i < cancelTipTxnsList.size(); i++) {
                CancelTipTxns cancelTipTxns = cancelTipTxnsList.get(i);
                if (i == 0) {
                    String userId = cancelTipTxns.getUserId();
                    memBaseinfo = gameCommonService.getMemTradingInfo(userId);
                    balance = memBaseinfo.getBalance();
                    if (null == memBaseinfo) {
                        AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                        callBacekFail.setStatus("1002");
                        callBacekFail.setDesc("Account is not exists");
                        return callBacekFail;
                    }
                }
                LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
                wrapper.and(c -> c.eq(Txns::getMethod, "Tip").or().eq(Txns::getMethod, "Cancel Tip"));
                wrapper.eq(Txns::getStatus, "Running");
                wrapper.eq(Txns::getPlatformTxId, cancelTipTxns.getPlatformTxId());
                wrapper.eq(Txns::getPlatform, OpenAPIProperties.AWC_PLATFORM_CODE);
                Txns oldTxns = txnsMapper.selectOne(wrapper);
                if (null == oldTxns) {
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1017");
                    callBacekFail.setDesc("TxCode is not exist");
                    return callBacekFail;
                } else if ("Cancel Tip".equals(oldTxns.getMethod())) {
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1043");
                    callBacekFail.setDesc("Bet has canceled.");
                    return callBacekFail;
                }

                //打赏给直播主的金额
                BigDecimal tip = oldTxns.getBetAmount();
                balance = balance.add(tip);
                gameCommonService.updateUserBalance(memBaseinfo, tip, GoldchangeEnum.CANCEL_TIP, TradingEnum.INCOME);
                String dateStr = DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT);

                Txns txns = new Txns();
                BeanUtils.copyProperties(oldTxns, txns);
                txns.setId(null);
                txns.setBalance(balance);
                txns.setMethod("Cancel Tip");
                txns.setStatus("Running");
                txns.setCreateTime(dateStr);
                txnsMapper.insert(txns);
                oldTxns.setStatus("Cancel Tip");
                oldTxns.setUpdateTime(dateStr);
                txnsMapper.updateById(oldTxns);
            }
            AwcCallBackRespSuccess placeBetSuccess = new AwcCallBackRespSuccess();
            placeBetSuccess.setStatus("0000");
            placeBetSuccess.setBalance(balance.divide(gameParentPlatform.getCurrencyPro()));
            placeBetSuccess.setBalanceTs(DateUtils.getTimeAndZone());
            return placeBetSuccess;
        } else {
            AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
            callBacekFail.setStatus("1036");
            callBacekFail.setDesc("Invalid parameters.");
            return callBacekFail;
        }
    }

    private boolean checkIp(GameParentPlatform gameParentPlatform,String ip) {
        if (null == gameParentPlatform) {
            return false;
        } else if (null == gameParentPlatform.getIpAddr() || "".equals(gameParentPlatform.getIpAddr())) {
            return true;
        } else if (gameParentPlatform.getIpAddr().equals(ip)) {
            return true;
        }
        return false;
    }

}
