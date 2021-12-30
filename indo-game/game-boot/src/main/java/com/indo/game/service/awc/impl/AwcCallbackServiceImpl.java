package com.indo.game.service.awc.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.indo.common.enums.GoldchangeEnum;
import com.indo.common.enums.TradingEnum;
import com.indo.common.utils.DateUtils;
import com.indo.game.config.OpenAPIProperties;
import com.indo.game.mapper.awc.TxnsMapper;
import com.indo.game.pojo.entity.awc.*;
import com.indo.game.pojo.entity.manage.GamePlatform;
import com.indo.game.pojo.vo.callback.awc.AwcCallBackParentRespSuccess;
import com.indo.game.pojo.vo.callback.awc.AwcCallBackRespFail;
import com.indo.game.pojo.vo.callback.awc.AwcCallBackRespSuccess;
import com.indo.game.pojo.vo.callback.awc.AwcGetBalanceRespSuccess;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.awc.AwcCallbackService;
import com.indo.user.pojo.entity.MemBaseinfo;
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

    public Object awcCallback(AwcApiRequestParentData awcApiRequestData,String ip) {
        if(!OpenAPIProperties.AWC_API_SECRET_KEY.equals(awcApiRequestData.getKey())){
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
                return bet(awcApiRequestData,ip);
            }
            //Cancel Bet 取消注单
            if ("cancelBet".equals(action)) {
                return cancelBet(awcApiRequestData,ip);
            }
            //Adjust Bet 调整投注
            if ("adjustBet".equals(action)) {
                return adjustBet(awcApiRequestData,ip);
            }
            //Void Bet 交易作废
            if ("voidBet".equals(action)) {
                return voidBet(awcApiRequestData,ip);
            }
            //Unvoid Bet 取消交易作废
            if ("unvoidBet".equals(action)) {
                return unvoidBet(awcApiRequestData,ip);
            }
            //Refund 返还金额
            if ("refund".equals(action)) {
                return refund(awcApiRequestData,ip);
            }
            //Settle 已结帐派彩
            if ("settle".equals(action)) {
                return settle(awcApiRequestData,ip);
            }
            //Unsettle 取消结帐派彩
            if ("unsettle".equals(action)) {
                return unsettle(awcApiRequestData,ip);
            }
            //Void Settle 结帐单转为无效
            if ("voidSettle".equals(action)) {
                return voidSettle(awcApiRequestData,ip);
            }
            //Unvoid Settle 无效单结账
            if ("unvoidSettle".equals(action)) {
                return unvoidSettle(awcApiRequestData,ip);
            }
            // BetNSettle 下注并直接结算
            if ("betNSettle".equals(action)) {
                return betNSettle(awcApiRequestData,ip);
            }
            // Cancel BetNSettle 取消结算并取消注单
            if ("cancelBetNSettle".equals(action)) {
                return cancelBetNSettle(awcApiRequestData,ip);
            }
            // Free Spin 免费旋转
            if ("freeSpin".equals(action)) {
                return freeSpin(awcApiRequestData,ip);
            }
            //  Give (Promotion Bonus) 活动派彩
            if ("give".equals(action)) {
                return give(awcApiRequestData,ip);
            }
            //  Tip 打赏
            if ("tip".equals(action)) {
                return tip(awcApiRequestData,ip);
            }
            //  Cancel Tip 取消打赏
            if ("cancelTip".equals(action)) {
                return cancelTip(awcApiRequestData,ip);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("awcCallBack {9999Fail} callBack 回调,IP:"+ip+" params:{}",e);
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
        MemBaseinfo memBaseinfo = gameCommonService.getByAccountNo(userId);
        if (null == memBaseinfo) {
            AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
            callBacekFail.setStatus("1002");
            callBacekFail.setDesc("Account is not exists");
            return callBacekFail;
        } else {
            AwcGetBalanceRespSuccess getBalanceSuccess = new AwcGetBalanceRespSuccess();
            getBalanceSuccess.setStatus("0000");
            getBalanceSuccess.setBalance(memBaseinfo.getBalance());

            getBalanceSuccess.setBalanceTs(DateUtils.getTimeAndZone());
            getBalanceSuccess.setUserId(userId);
            return getBalanceSuccess;
        }


    }


    //Place Bet 下注
    private Object bet(AwcApiRequestParentData awcApiRequestData,String ip) {
        AwcApiRequestData<List<PlaceBetTxns>> apiRequestData = JSON.parseObject(String.valueOf(awcApiRequestData.getMessage()), new TypeReference<AwcApiRequestData<List<PlaceBetTxns>>>() {
        });
        List<PlaceBetTxns> placeBetTxnsList = apiRequestData.getTxns();
        if (null != placeBetTxnsList && placeBetTxnsList.size() > 0) {
            for (PlaceBetTxns placeBetTxns : placeBetTxnsList) {
                if(!checkIp(ip,placeBetTxns.getPlatform())){
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1029");
                    callBacekFail.setDesc("invalid IP address.");
                    return callBacekFail;
                }
                String userId = placeBetTxns.getUserId();
                MemBaseinfo memBaseinfo = gameCommonService.getByAccountNo(userId);
                if (null == memBaseinfo) {
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1002");
                    callBacekFail.setDesc("Account is not exists");
                    return callBacekFail;
                } else {
                    BigDecimal betAmount = placeBetTxns.getBetAmount();
                    if(memBaseinfo.getBalance().compareTo(betAmount) == -1){
                        AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                        callBacekFail.setStatus("1018");
                        callBacekFail.setDesc("Not Enough Balance");
                        return callBacekFail;
                    }
                    LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
                    wrapper.and(c -> c.eq(Txns::getMethod,"Place Bet").or().eq(Txns::getMethod,"Cancel Bet").or().eq(Txns::getMethod,"Adjust Bet"));
                    wrapper.eq(Txns::getStatus,"Running");
                    wrapper.eq(Txns::getPlatformTxId,placeBetTxns.getPlatformTxId());
                    wrapper.eq(Txns::getPlatform,placeBetTxns.getPlatform());
                    wrapper.eq(Txns::getGameType,placeBetTxns.getGameType());
                    wrapper.eq(Txns::getUserId,placeBetTxns.getUserId());
                    Txns oldTxns = txnsMapper.selectOne(wrapper);
                    if(null!=oldTxns){
                        if("Cancel Bet".equals(oldTxns.getMethod())){
                            AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                            callBacekFail.setStatus("1043");
                            callBacekFail.setDesc("Bet has canceled.");
                            return callBacekFail;
                        }else {
                            AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                            callBacekFail.setStatus("1038");
                            callBacekFail.setDesc("Duplicate transaction.");
                            return callBacekFail;
                        }
                    }
                    AwcCallBackRespSuccess placeBetSuccess = new AwcCallBackRespSuccess();
                    placeBetSuccess.setStatus("0000");

                    BigDecimal balance = memBaseinfo.getBalance().subtract(betAmount);
                    gameCommonService.updateUserBalance(memBaseinfo, betAmount, GoldchangeEnum.PLACE_BET, TradingEnum.SPENDING);

                    placeBetSuccess.setBalance(balance);
                    String dateStr = DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT);
                    placeBetSuccess.setBalanceTs(DateUtils.getTimeAndZone());
                    Txns txns = new Txns();
                    BeanUtils.copyProperties(placeBetTxns,txns);
                    txns.setBalance(balance);
                    txns.setMethod("Place Bet");
                    txns.setStatus("Running");
                    txns.setCreateTime(dateStr);
                    txnsMapper.insert(txns);
                    return placeBetSuccess;
                }
            }
        } else {
            AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
            callBacekFail.setStatus("1036");
            callBacekFail.setDesc("Invalid parameters.");
            return callBacekFail;
        }
        return null;
    }

//        gameCommonService.inOrOutBalanceCommon(GoldchangeEnum.AWCAESEXYBCRT_IN.getValue(), balance, memBaseinfo, content, cptOpenMember, Constants.AWC_AESEXYBCRT_ACCOUNT_TYPE);

    //Cancel Bet 取消注单
    private Object cancelBet(AwcApiRequestParentData awcApiRequestData,String ip) {
        AwcApiRequestData<List<CancelBetTxns>> apiRequestData = JSON.parseObject(String.valueOf(awcApiRequestData.getMessage()), new TypeReference<AwcApiRequestData<List<CancelBetTxns>>>() {
        });
        List<CancelBetTxns> cancelBetTxnsList = apiRequestData.getTxns();
        if (null != cancelBetTxnsList && cancelBetTxnsList.size() > 0) {
            for (CancelBetTxns cancelBetTxns : cancelBetTxnsList) {
                if(!checkIp(ip,cancelBetTxns.getPlatform())){
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1029");
                    callBacekFail.setDesc("invalid IP address.");
                    return callBacekFail;
                }
                String userId = cancelBetTxns.getUserId();
                MemBaseinfo memBaseinfo = gameCommonService.getByAccountNo(userId);
                if (null == memBaseinfo) {
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1002");
                    callBacekFail.setDesc("Account is not exists");
                    return callBacekFail;
                } else {
                    LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
                    wrapper.and(c -> c.eq(Txns::getMethod,"Place Bet").or().eq(Txns::getMethod,"Cancel Bet").or().eq(Txns::getMethod,"Adjust Bet"));
                    wrapper.eq(Txns::getStatus,"Running");
                    wrapper.eq(Txns::getPlatformTxId,cancelBetTxns.getPlatformTxId());
                    wrapper.eq(Txns::getPlatform,cancelBetTxns.getPlatform());
                    wrapper.eq(Txns::getGameType,cancelBetTxns.getGameType());
                    wrapper.eq(Txns::getUserId,cancelBetTxns.getUserId());
                    Txns oldTxns = txnsMapper.selectOne(wrapper);
                    if(null==oldTxns){
                        AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                        callBacekFail.setStatus("1017");
                        callBacekFail.setDesc("TxCode is not exist");
                        return callBacekFail;
                    }else if("Cancel Bet".equals(oldTxns.getMethod())){
                        AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                        callBacekFail.setStatus("1043");
                        callBacekFail.setDesc("Bet has canceled.");
                        return callBacekFail;
                    }

                    AwcCallBackRespSuccess placeBetSuccess = new AwcCallBackRespSuccess();
                    placeBetSuccess.setStatus("0000");
                    //查询下注订单
                    BigDecimal betAmount = oldTxns.getBetAmount();
                    BigDecimal balance = memBaseinfo.getBalance().add(betAmount);
                    gameCommonService.updateUserBalance(memBaseinfo, betAmount, GoldchangeEnum.PLACE_BET, TradingEnum.INCOME);
                    placeBetSuccess.setBalance(balance);
                    String dateStr = DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT);
                    placeBetSuccess.setBalanceTs(DateUtils.getTimeAndZone());

                    Txns txns = new Txns();
                    BeanUtils.copyProperties(oldTxns,txns);
                    txns.setId(null);
                    txns.setBalance(balance);
                    txns.setMethod("Cancel Bet");
                    txns.setStatus("Running");
                    txns.setCreateTime(dateStr);
                    txnsMapper.insert(txns);
                    oldTxns.setStatus("Cancel");
                    oldTxns.setUpdateTime(dateStr);
                    txnsMapper.updateById(oldTxns);
                    return placeBetSuccess;
                }
            }
        } else {
            AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
            callBacekFail.setStatus("1036");
            callBacekFail.setDesc("Invalid parameters.");
            return callBacekFail;
        }
        return null;
    }

    //Adjust Bet 调整投注
    private Object adjustBet(AwcApiRequestParentData awcApiRequestData,String ip) {
        AwcApiRequestData<List<AdjustBetTxns>> apiRequestData = JSON.parseObject(String.valueOf(awcApiRequestData.getMessage()), new TypeReference<AwcApiRequestData<List<AdjustBetTxns>>>() {
        });
        List<AdjustBetTxns> adjustBetTxnsList = apiRequestData.getTxns();
        if (null != adjustBetTxnsList && adjustBetTxnsList.size() > 0) {
            for (AdjustBetTxns adjustBetTxns : adjustBetTxnsList) {
                if(!checkIp(ip,adjustBetTxns.getPlatform())){
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1029");
                    callBacekFail.setDesc("invalid IP address.");
                    return callBacekFail;
                }
                String userId = adjustBetTxns.getUserId();
                MemBaseinfo memBaseinfo = gameCommonService.getByAccountNo(userId);
                if (null == memBaseinfo) {
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1002");
                    callBacekFail.setDesc("Account is not exists");
                    return callBacekFail;
                } else {
                    LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
                    wrapper.and(c -> c.eq(Txns::getMethod,"Place Bet").or().eq(Txns::getMethod,"Cancel Bet"));
                    wrapper.eq(Txns::getStatus,"Running");
                    wrapper.eq(Txns::getPlatformTxId,adjustBetTxns.getPlatformTxId());
                    wrapper.eq(Txns::getPlatform,adjustBetTxns.getPlatform());
                    wrapper.eq(Txns::getGameType,adjustBetTxns.getGameType());
                    wrapper.eq(Txns::getUserId,adjustBetTxns.getUserId());
                    Txns oldTxns = txnsMapper.selectOne(wrapper);
                    if(null==oldTxns){
                        AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                        callBacekFail.setStatus("1017");
                        callBacekFail.setDesc("TxCode is not exist");
                        return callBacekFail;
                    }else if("Cancel Bet".equals(oldTxns.getMethod())){
                        AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                        callBacekFail.setStatus("1043");
                        callBacekFail.setDesc("Bet has canceled.");
                        return callBacekFail;
                    }
                    AwcCallBackRespSuccess placeBetSuccess = new AwcCallBackRespSuccess();
                    placeBetSuccess.setStatus("0000");
                    // 下注金额
                    BigDecimal realBetAmount = BigDecimal.valueOf(Double.valueOf(adjustBetTxns.getBetAmount()));
                    //需要调整的超收金额，返还因为赔率超收金额 下注金额减去实际的下注金额
                    BigDecimal realWinAmount = BigDecimal.valueOf(Double.valueOf(adjustBetTxns.getAdjustAmount()));
                    BigDecimal balance = memBaseinfo.getBalance().add(realWinAmount);
                    gameCommonService.updateUserBalance(memBaseinfo, realWinAmount, GoldchangeEnum.ADJUST_BET, TradingEnum.INCOME);
                    String dateStr = DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT);
                    placeBetSuccess.setBalance(balance);
                    placeBetSuccess.setBalanceTs(DateUtils.getTimeAndZone());

                    Txns txns = new Txns();
                    BeanUtils.copyProperties(oldTxns,txns);
                    txns.setId(null);
                    txns.setBalance(balance);
                    txns.setMethod("Adjust Bet");
                    txns.setStatus("Running");
                    txns.setRealBetAmount(realBetAmount);//真实下注金额
                    txns.setRealWinAmount(realWinAmount);//真实返还金额
                    txns.setWinAmount(realBetAmount.add(realWinAmount));//返还金额 (包含下注金额)
                    txns.setCreateTime(dateStr);
                    txnsMapper.insert(txns);
                    oldTxns.setStatus("Adjust");
                    oldTxns.setUpdateTime(dateStr);
                    txnsMapper.updateById(oldTxns);

                    return placeBetSuccess;
                }
            }
        } else {
            AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
            callBacekFail.setStatus("1036");
            callBacekFail.setDesc("Invalid parameters.");
            return callBacekFail;
        }
        return null;
    }

    //Void Bet 交易作废
    private Object voidBet(AwcApiRequestParentData awcApiRequestData,String ip) {
        AwcApiRequestData<List<VoidBetTxns>> apiRequestData = JSON.parseObject(String.valueOf(awcApiRequestData.getMessage()), new TypeReference<AwcApiRequestData<List<VoidBetTxns>>>() {
        });
        List<VoidBetTxns> voidBetTxnsList = apiRequestData.getTxns();
        if (null != voidBetTxnsList && voidBetTxnsList.size() > 0) {
            for (VoidBetTxns voidBetTxns : voidBetTxnsList) {
                if(!checkIp(ip,voidBetTxns.getPlatform())){
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1029");
                    callBacekFail.setDesc("invalid IP address.");
                    return callBacekFail;
                }
                String userId = voidBetTxns.getUserId();
                MemBaseinfo memBaseinfo = gameCommonService.getByAccountNo(userId);
                if (null == memBaseinfo) {
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1002");
                    callBacekFail.setDesc("Account is not exists");
                    return callBacekFail;
                } else {
                    LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
                    wrapper.and(c -> c.eq(Txns::getMethod,"Place Bet").or().eq(Txns::getMethod,"Cancel Bet").or().eq(Txns::getMethod,"Adjust Bet"));
                    wrapper.eq(Txns::getStatus,"Running");
                    wrapper.eq(Txns::getPlatformTxId,voidBetTxns.getPlatformTxId());
                    wrapper.eq(Txns::getPlatform,voidBetTxns.getPlatform());
                    wrapper.eq(Txns::getGameType,voidBetTxns.getGameType());
                    wrapper.eq(Txns::getUserId,voidBetTxns.getUserId());
                    Txns oldTxns = txnsMapper.selectOne(wrapper);
                    if(null==oldTxns){
                        AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                        callBacekFail.setStatus("1017");
                        callBacekFail.setDesc("TxCode is not exist");
                        return callBacekFail;
                    }else if("Cancel Bet".equals(oldTxns.getMethod())){
                        AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                        callBacekFail.setStatus("1043");
                        callBacekFail.setDesc("Bet has canceled.");
                        return callBacekFail;
                    }
                    // 下注金额
                    BigDecimal realBetAmount = BigDecimal.valueOf(Double.valueOf(voidBetTxns.getBetAmount()));
                    BigDecimal balance = memBaseinfo.getBalance().add(realBetAmount);
                    gameCommonService.updateUserBalance(memBaseinfo, realBetAmount, GoldchangeEnum.VOID_BET, TradingEnum.INCOME);
                    String dateStr = DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT);
                    AwcCallBackParentRespSuccess callBackSuccess = new AwcCallBackParentRespSuccess();
                    callBackSuccess.setStatus("0000");

                    Txns txns = new Txns();
                    BeanUtils.copyProperties(oldTxns,txns);
                    txns.setId(null);
                    txns.setBalance(balance);
                    txns.setMethod("Void Bet");
                    txns.setStatus(oldTxns.getMethod());
                    txns.setRealBetAmount(realBetAmount);
                    txns.setCreateTime(dateStr);
                    txnsMapper.insert(txns);
                    oldTxns.setStatus("Void");
                    oldTxns.setUpdateTime(dateStr);
                    txnsMapper.updateById(oldTxns);
                    return callBackSuccess;
                }
            }
        } else {
            AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
            callBacekFail.setStatus("1036");
            callBacekFail.setDesc("Invalid parameters.");
            return callBacekFail;
        }
        return null;
    }

    //Unvoid Bet 取消交易作废
    private Object unvoidBet(AwcApiRequestParentData awcApiRequestData,String ip) {
        AwcApiRequestData<List<UnvoidBetTxns>> apiRequestData = JSON.parseObject(String.valueOf(awcApiRequestData.getMessage()), new TypeReference<AwcApiRequestData<List<UnvoidBetTxns>>>() {
        });
        List<UnvoidBetTxns> unvoidBetTxnsList = apiRequestData.getTxns();
        if (null != unvoidBetTxnsList && unvoidBetTxnsList.size() > 0) {
            for (UnvoidBetTxns unvoidBetTxns : unvoidBetTxnsList) {
                if(!checkIp(ip,unvoidBetTxns.getPlatform())){
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1029");
                    callBacekFail.setDesc("invalid IP address.");
                    return callBacekFail;
                }
                String userId = unvoidBetTxns.getUserId();
                MemBaseinfo memBaseinfo = gameCommonService.getByAccountNo(userId);
                if (null == memBaseinfo) {
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1002");
                    callBacekFail.setDesc("Account is not exists");
                    return callBacekFail;
                } else {
                    LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
                    wrapper.eq(Txns::getMethod,"Void Bet");
                    wrapper.eq(Txns::getPlatformTxId,unvoidBetTxns.getPlatformTxId());
                    wrapper.eq(Txns::getPlatform,unvoidBetTxns.getPlatform());
                    wrapper.eq(Txns::getGameType,unvoidBetTxns.getGameType());
                    wrapper.eq(Txns::getUserId,unvoidBetTxns.getUserId());
                    Txns oldTxns = txnsMapper.selectOne(wrapper);
                    if(null==oldTxns){
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
                    AwcCallBackParentRespSuccess callBackSuccess = new AwcCallBackParentRespSuccess();
                    callBackSuccess.setStatus("0000");
                    Txns txns = new Txns();
                    BeanUtils.copyProperties(oldTxns,txns);
                    txns.setId(null);
                    txns.setBalance(balance);
                    txns.setMethod(oldTxns.getStatus());
                    txns.setStatus("Running");
                    txns.setCreateTime(dateStr);
                    txnsMapper.insert(txns);
                    return callBackSuccess;
                }
            }
        } else {
            AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
            callBacekFail.setStatus("1036");
            callBacekFail.setDesc("Invalid parameters.");
            return callBacekFail;
        }
        return null;
    }

    //Refund 返还金额
    private Object refund(AwcApiRequestParentData awcApiRequestData,String ip) {
        AwcApiRequestData<List<RefundTxns>> apiRequestData = JSON.parseObject(String.valueOf(awcApiRequestData.getMessage()), new TypeReference<AwcApiRequestData<List<RefundTxns>>>() {
        });
        List<RefundTxns> refundTxnsList = apiRequestData.getTxns();
        if (null != refundTxnsList && refundTxnsList.size() > 0) {
            for (RefundTxns refundTxns : refundTxnsList) {
                if(!checkIp(ip,refundTxns.getPlatform())){
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1029");
                    callBacekFail.setDesc("invalid IP address.");
                    return callBacekFail;
                }
                String userId = refundTxns.getUserId();
                MemBaseinfo memBaseinfo = gameCommonService.getByAccountNo(userId);
                if (null == memBaseinfo) {
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1002");
                    callBacekFail.setDesc("Account is not exists");
                    return callBacekFail;
                } else {
                    BigDecimal betAmount = refundTxns.getBetAmount();
                    BigDecimal winAmount = refundTxns.getWinAmount().subtract(betAmount);
                    BigDecimal balance = memBaseinfo.getBalance().add(winAmount);
                    gameCommonService.updateUserBalance(memBaseinfo, winAmount, GoldchangeEnum.REFUND, TradingEnum.INCOME);
                    String dateStr = DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT);

                    Txns txns = new Txns();
                    BeanUtils.copyProperties(refundTxns,txns);
                    txns.setId(null);
                    txns.setBalance(balance);
                    txns.setMethod("Refund");
                    txns.setStatus("Running");
                    txns.setCreateTime(dateStr);
                    txnsMapper.insert(txns);

                    AwcCallBackParentRespSuccess callBackSuccess = new AwcCallBackParentRespSuccess();
                    callBackSuccess.setStatus("0000");
                    return callBackSuccess;
                }
            }
        } else {
            AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
            callBacekFail.setStatus("1036");
            callBacekFail.setDesc("Invalid parameters.");
            return callBacekFail;
        }
        return null;
    }

    //Settle 已结帐派彩
    private Object settle(AwcApiRequestParentData awcApiRequestData,String ip) {
        AwcApiRequestData<List<SettleTxns>> apiRequestData = JSON.parseObject(String.valueOf(awcApiRequestData.getMessage()), new TypeReference<AwcApiRequestData<List<SettleTxns>>>() {
        });
        List<SettleTxns> settleTxnsList = apiRequestData.getTxns();
        if (null != settleTxnsList && settleTxnsList.size() > 0) {
            for (SettleTxns settleTxns : settleTxnsList) {
                if(!checkIp(ip,settleTxns.getPlatform())){
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1029");
                    callBacekFail.setDesc("invalid IP address.");
                    return callBacekFail;
                }
                String userId = settleTxns.getUserId();
                MemBaseinfo memBaseinfo = gameCommonService.getByAccountNo(userId);
                if (null == memBaseinfo) {
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1002");
                    callBacekFail.setDesc("Account is not exists");
                    return callBacekFail;
                } else {
                    LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
                    wrapper.and(c -> c.eq(Txns::getMethod,"Place Bet").or().eq(Txns::getMethod,"Unsettle").or().eq(Txns::getMethod,"Adjust Bet"));
                    wrapper.eq(Txns::getStatus,"Running");
                    wrapper.eq(Txns::getPlatformTxId,settleTxns.getPlatformTxId());
                    wrapper.eq(Txns::getPlatform,settleTxns.getPlatform());
                    wrapper.eq(Txns::getGameType,settleTxns.getGameType());
                    wrapper.eq(Txns::getUserId,settleTxns.getUserId());
                    Txns oldTxns = txnsMapper.selectOne(wrapper);
                    if(null==oldTxns){
                        AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                        callBacekFail.setStatus("1017");
                        callBacekFail.setDesc("TxCode is not exist");
                        return callBacekFail;
                    }else {

                        BigDecimal winAmount = settleTxns.getWinAmount();
                        BigDecimal balance = memBaseinfo.getBalance().add(winAmount);
                        gameCommonService.updateUserBalance(memBaseinfo, winAmount, GoldchangeEnum.SETTLE, TradingEnum.INCOME);
                        String dateStr = DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT);

                        Txns txns = new Txns();
                        BeanUtils.copyProperties(settleTxns, txns);
                        txns.setId(null);
                        txns.setBalance(balance);
                        txns.setMethod("Settle");
                        txns.setStatus("Running");
                        txns.setCreateTime(dateStr);
                        txnsMapper.insert(txns);
                        oldTxns.setStatus("Settle");
                        txnsMapper.updateById(oldTxns);
                        AwcCallBackParentRespSuccess callBackSuccess = new AwcCallBackParentRespSuccess();
                        callBackSuccess.setStatus("0000");
                        return callBackSuccess;
                    }
                }
            }
        } else {
            AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
            callBacekFail.setStatus("1036");
            callBacekFail.setDesc("Invalid parameters.");
            return callBacekFail;
        }
        return null;
    }

    //Unsettle 取消结帐派彩
    private Object unsettle(AwcApiRequestParentData awcApiRequestData,String ip) {
        AwcApiRequestData<List<UnsettleTxns>> apiRequestData = JSON.parseObject(String.valueOf(awcApiRequestData.getMessage()), new TypeReference<AwcApiRequestData<List<UnsettleTxns>>>() {
        });
        List<UnsettleTxns> unsettleTxnsList = apiRequestData.getTxns();
        if (null != unsettleTxnsList && unsettleTxnsList.size() > 0) {
            for (UnsettleTxns unsettleTxns : unsettleTxnsList) {
                if(!checkIp(ip,unsettleTxns.getPlatform())){
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1029");
                    callBacekFail.setDesc("invalid IP address.");
                    return callBacekFail;
                }
                String userId = unsettleTxns.getUserId();
                MemBaseinfo memBaseinfo = gameCommonService.getByAccountNo(userId);
                if (null == memBaseinfo) {
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1002");
                    callBacekFail.setDesc("Account is not exists");
                    return callBacekFail;
                } else {
                    LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
                    wrapper.eq(Txns::getMethod,"Settle");
                    wrapper.eq(Txns::getStatus,"Running");
                    wrapper.eq(Txns::getPlatformTxId,unsettleTxns.getPlatformTxId());
                    wrapper.eq(Txns::getPlatform,unsettleTxns.getPlatform());
                    wrapper.eq(Txns::getGameType,unsettleTxns.getGameType());
                    wrapper.eq(Txns::getUserId,unsettleTxns.getUserId());
                    Txns oldTxns = txnsMapper.selectOne(wrapper);
                    if(null==oldTxns){
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
                    BeanUtils.copyProperties(oldTxns,txns);
                    txns.setId(null);
                    txns.setBalance(balance);
                    txns.setMethod("Unsettle");
                    txns.setStatus("Running");
                    txns.setCreateTime(dateStr);
                    txnsMapper.insert(txns);
                    oldTxns.setStatus("Unsettle");
                    oldTxns.setUpdateTime(dateStr);
                    txnsMapper.updateById(oldTxns);
                    AwcCallBackParentRespSuccess callBackSuccess = new AwcCallBackParentRespSuccess();
                    callBackSuccess.setStatus("0000");
                    return callBackSuccess;
                }
            }
        } else {
            AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
            callBacekFail.setStatus("1036");
            callBacekFail.setDesc("Invalid parameters.");
            return callBacekFail;
        }
        return null;
    }

    //Void Settle 结帐单转为无效
    private Object voidSettle(AwcApiRequestParentData awcApiRequestData,String ip) {
        AwcApiRequestData<List<VoidSettleTxns>> apiRequestData = JSON.parseObject(String.valueOf(awcApiRequestData.getMessage()), new TypeReference<AwcApiRequestData<List<VoidSettleTxns>>>() {
        });
        List<VoidSettleTxns> voidSettleTxnsList = apiRequestData.getTxns();
        if (null != voidSettleTxnsList && voidSettleTxnsList.size() > 0) {
            for (VoidSettleTxns voidSettleTxns : voidSettleTxnsList) {
                if(!checkIp(ip,voidSettleTxns.getPlatform())){
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1029");
                    callBacekFail.setDesc("invalid IP address.");
                    return callBacekFail;
                }
                String userId = voidSettleTxns.getUserId();
                MemBaseinfo memBaseinfo = gameCommonService.getByAccountNo(userId);
                if (null == memBaseinfo) {
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1002");
                    callBacekFail.setDesc("Account is not exists");
                    return callBacekFail;
                } else {
                    LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
                    wrapper.eq(Txns::getMethod,"Settle");
                    wrapper.eq(Txns::getStatus,"Running");
                    wrapper.eq(Txns::getPlatformTxId,voidSettleTxns.getPlatformTxId());
                    wrapper.eq(Txns::getPlatform,voidSettleTxns.getPlatform());
                    wrapper.eq(Txns::getGameType,voidSettleTxns.getGameType());
                    wrapper.eq(Txns::getUserId,voidSettleTxns.getUserId());
                    Txns oldTxns = txnsMapper.selectOne(wrapper);
                    if(null==oldTxns){
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
                    BeanUtils.copyProperties(oldTxns,txns);
                    txns.setId(null);
                    txns.setBalance(balance);
                    txns.setMethod("Void Settle");
                    txns.setStatus("Running");
                    txns.setCreateTime(dateStr);
                    txnsMapper.insert(txns);
                    oldTxns.setStatus("Void Settle");
                    oldTxns.setUpdateTime(dateStr);
                    txnsMapper.updateById(oldTxns);
                    AwcCallBackParentRespSuccess callBackSuccess = new AwcCallBackParentRespSuccess();
                    callBackSuccess.setStatus("0000");
                    return callBackSuccess;
                }
            }
        } else {
            AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
            callBacekFail.setStatus("1036");
            callBacekFail.setDesc("Invalid parameters.");
            return callBacekFail;
        }
        return null;
    }

    //Unvoid Settle 无效单结账
    private Object unvoidSettle(AwcApiRequestParentData awcApiRequestData,String ip) {
        AwcApiRequestData<List<UnvoidSettleTxns>> apiRequestData = JSON.parseObject(String.valueOf(awcApiRequestData.getMessage()), new TypeReference<AwcApiRequestData<List<UnvoidSettleTxns>>>() {
        });
        List<UnvoidSettleTxns> unvoidSettleTxnsList = apiRequestData.getTxns();
        if (null != unvoidSettleTxnsList && unvoidSettleTxnsList.size() > 0) {
            for (UnvoidSettleTxns unvoidSettleTxns : unvoidSettleTxnsList) {
                if(!checkIp(ip,unvoidSettleTxns.getPlatform())){
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1029");
                    callBacekFail.setDesc("invalid IP address.");
                    return callBacekFail;
                }
                String userId = unvoidSettleTxns.getUserId();
                MemBaseinfo memBaseinfo = gameCommonService.getByAccountNo(userId);
                if (null == memBaseinfo) {
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1002");
                    callBacekFail.setDesc("Account is not exists");
                    return callBacekFail;
                } else {
                    LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
                    wrapper.eq(Txns::getMethod,"Void Settle");
                    wrapper.eq(Txns::getStatus,"Running");
                    wrapper.eq(Txns::getPlatformTxId,unvoidSettleTxns.getPlatformTxId());
                    wrapper.eq(Txns::getPlatform,unvoidSettleTxns.getPlatform());
                    wrapper.eq(Txns::getGameType,unvoidSettleTxns.getGameType());
                    wrapper.eq(Txns::getUserId,unvoidSettleTxns.getUserId());
                    Txns oldTxns = txnsMapper.selectOne(wrapper);
                    if(null==oldTxns){
                        AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                        callBacekFail.setStatus("1017");
                        callBacekFail.setDesc("TxCode is not exist");
                        return callBacekFail;
                    }
                    BigDecimal winAmount = oldTxns.getWinAmount();
                    BigDecimal balance = memBaseinfo.getBalance().add(winAmount);
                    gameCommonService.updateUserBalance(memBaseinfo, winAmount, GoldchangeEnum.UNVOID_SETTLE, TradingEnum.INCOME);
                    String dateStr = DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT);

                    Txns txns = new Txns();
                    BeanUtils.copyProperties(oldTxns,txns);
                    txns.setId(null);
                    txns.setBalance(balance);
                    txns.setMethod("Settle");
                    txns.setStatus("Running");
                    txns.setCreateTime(dateStr);
                    txnsMapper.insert(txns);
                    oldTxns.setStatus("Unvoid Settle");
                    oldTxns.setUpdateTime(dateStr);
                    txnsMapper.updateById(oldTxns);
                    AwcCallBackRespSuccess placeBetSuccess = new AwcCallBackRespSuccess();
                    placeBetSuccess.setStatus("0000");
                    placeBetSuccess.setBalance(balance);
                    placeBetSuccess.setBalanceTs(DateUtils.getTimeAndZone());
                    return placeBetSuccess;
                }
            }
        } else {
            AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
            callBacekFail.setStatus("1036");
            callBacekFail.setDesc("Invalid parameters.");
            return callBacekFail;
        }
        return null;
    }

    // BetNSettle 下注并直接结算
    private Object betNSettle(AwcApiRequestParentData awcApiRequestData,String ip) {
        AwcApiRequestData<List<BetNSettleTxns>> apiRequestData = JSON.parseObject(String.valueOf(awcApiRequestData.getMessage()), new TypeReference<AwcApiRequestData<List<BetNSettleTxns>>>() {
        });
        List<BetNSettleTxns> betNSettleTxnsList = apiRequestData.getTxns();
        if (null != betNSettleTxnsList && betNSettleTxnsList.size() > 0) {
            for (BetNSettleTxns betNSettleTxns : betNSettleTxnsList) {
                if(!checkIp(ip,betNSettleTxns.getPlatform())){
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1029");
                    callBacekFail.setDesc("invalid IP address.");
                    return callBacekFail;
                }
                String userId = betNSettleTxns.getUserId();
                MemBaseinfo memBaseinfo = gameCommonService.getByAccountNo(userId);
                if (null == memBaseinfo) {
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1002");
                    callBacekFail.setDesc("Account is not exists");
                    return callBacekFail;
                } else {
                    BigDecimal winAmount = betNSettleTxns.getWinAmount();
                    if(memBaseinfo.getBalance().compareTo(winAmount) == -1){
                        AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                        callBacekFail.setStatus("1018");
                        callBacekFail.setDesc("Not Enough Balance");
                        return callBacekFail;
                    }
                    LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
                    wrapper.and(c -> c.eq(Txns::getMethod,"BetNSettle").or().eq(Txns::getMethod,"Cancel BetNSettle"));
                    wrapper.eq(Txns::getStatus,"Running");
                    wrapper.eq(Txns::getPlatformTxId,betNSettleTxns.getPlatformTxId());
                    wrapper.eq(Txns::getPlatform,betNSettleTxns.getPlatform());
                    wrapper.eq(Txns::getGameType,betNSettleTxns.getGameType());
                    wrapper.eq(Txns::getUserId,betNSettleTxns.getUserId());
                    Txns oldTxns = txnsMapper.selectOne(wrapper);
                    if(null!=oldTxns){
                        if("Cancel BetNSettle".equals(oldTxns.getMethod())){
                            AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                            callBacekFail.setStatus("1043");
                            callBacekFail.setDesc("Bet has canceled.");
                            return callBacekFail;
                        }else {
                            AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                            callBacekFail.setStatus("1038");
                            callBacekFail.setDesc("Duplicate transaction.");
                            return callBacekFail;
                        }
                    }

                    BigDecimal balance = memBaseinfo.getBalance().add(winAmount);
                    gameCommonService.updateUserBalance(memBaseinfo, winAmount, GoldchangeEnum.BETNSETTLE, TradingEnum.INCOME);
                    String dateStr = DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT);
                    Txns txns = new Txns();
                    BeanUtils.copyProperties(betNSettleTxns,txns);
                    txns.setId(null);
                    txns.setBalance(balance);
                    txns.setMethod("BetNSettle");
                    txns.setStatus("Running");
                    txns.setCreateTime(dateStr);
                    txnsMapper.insert(txns);

                    AwcCallBackRespSuccess placeBetSuccess = new AwcCallBackRespSuccess();
                    placeBetSuccess.setStatus("0000");
                    placeBetSuccess.setBalance(balance);
                    placeBetSuccess.setBalanceTs(DateUtils.getTimeAndZone());
                    return placeBetSuccess;
                }
            }
        } else {
            AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
            callBacekFail.setStatus("1036");
            callBacekFail.setDesc("Invalid parameters.");
            return callBacekFail;
        }
        return null;
    }

    // Cancel BetNSettle 取消结算并取消注单
    private Object cancelBetNSettle(AwcApiRequestParentData awcApiRequestData,String ip) {
        AwcApiRequestData<List<CancelBetNSettleTxns>> apiRequestData = JSON.parseObject(String.valueOf(awcApiRequestData.getMessage()), new TypeReference<AwcApiRequestData<List<CancelBetNSettleTxns>>>() {
        });
        List<CancelBetNSettleTxns> cancelBetNSettleTxnsList = apiRequestData.getTxns();
        if (null != cancelBetNSettleTxnsList && cancelBetNSettleTxnsList.size() > 0) {
            for (CancelBetNSettleTxns cancelBetNSettleTxns : cancelBetNSettleTxnsList) {
                if(!checkIp(ip,cancelBetNSettleTxns.getPlatform())){
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1029");
                    callBacekFail.setDesc("invalid IP address.");
                    return callBacekFail;
                }
                String userId = cancelBetNSettleTxns.getUserId();
                MemBaseinfo memBaseinfo = gameCommonService.getByAccountNo(userId);
                if (null == memBaseinfo) {
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1002");
                    callBacekFail.setDesc("Account is not exists");
                    return callBacekFail;
                } else {
                    LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
                    wrapper.eq(Txns::getMethod,"BetNSettle");
                    wrapper.eq(Txns::getStatus,"Running");
                    wrapper.eq(Txns::getPlatformTxId,cancelBetNSettleTxns.getPlatformTxId());
                    wrapper.eq(Txns::getPlatform,cancelBetNSettleTxns.getPlatform());
                    wrapper.eq(Txns::getGameType,cancelBetNSettleTxns.getGameType());
                    wrapper.eq(Txns::getUserId,cancelBetNSettleTxns.getUserId());
                    Txns oldTxns = txnsMapper.selectOne(wrapper);
                    if(null==oldTxns){
                        AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                        callBacekFail.setStatus("1017");
                        callBacekFail.setDesc("TxCode is not exist");
                        return callBacekFail;
                    }
                    BigDecimal winAmount = oldTxns.getWinAmount();
                    BigDecimal balance = memBaseinfo.getBalance().add(winAmount);
                    gameCommonService.updateUserBalance(memBaseinfo, winAmount, GoldchangeEnum.CANCEL_BETNSETTLE, TradingEnum.SPENDING);
                    String dateStr = DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT);

                    Txns txns = new Txns();
                    BeanUtils.copyProperties(oldTxns,txns);
                    txns.setId(null);
                    txns.setBalance(balance);
                    txns.setMethod("Cancel BetNSettle");
                    txns.setStatus("Running");
                    txns.setCreateTime(dateStr);
                    txnsMapper.insert(txns);
                    oldTxns.setStatus("Cancel BetNSettle");
                    oldTxns.setUpdateTime(dateStr);
                    txnsMapper.updateById(oldTxns);
                    AwcCallBackRespSuccess placeBetSuccess = new AwcCallBackRespSuccess();
                    placeBetSuccess.setStatus("0000");
                    placeBetSuccess.setBalance(balance);
                    placeBetSuccess.setBalanceTs(DateUtils.getTimeAndZone());
                    return placeBetSuccess;
                }
            }
        } else {
            AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
            callBacekFail.setStatus("1036");
            callBacekFail.setDesc("Invalid parameters.");
            return callBacekFail;
        }
        return null;
    }

    // Free Spin 免费旋转
    private Object freeSpin(AwcApiRequestParentData awcApiRequestData,String ip) {
        AwcApiRequestData<List<FreeSpinTxns>> apiRequestData = JSON.parseObject(String.valueOf(awcApiRequestData.getMessage()), new TypeReference<AwcApiRequestData<List<FreeSpinTxns>>>() {
        });
        List<FreeSpinTxns> freeSpinTxnsList = apiRequestData.getTxns();
        if (null != freeSpinTxnsList && freeSpinTxnsList.size() > 0) {
            for (FreeSpinTxns freeSpinTxns : freeSpinTxnsList) {
                if(!checkIp(ip,freeSpinTxns.getPlatform())){
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1029");
                    callBacekFail.setDesc("invalid IP address.");
                    return callBacekFail;
                }
                String userId = freeSpinTxns.getUserId();
                MemBaseinfo memBaseinfo = gameCommonService.getByAccountNo(userId);
                if (null == memBaseinfo) {
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1002");
                    callBacekFail.setDesc("Account is not exists");
                    return callBacekFail;
                } else {
                    BigDecimal winAmount = freeSpinTxns.getWinAmount();
                    if(memBaseinfo.getBalance().compareTo(winAmount) == -1){
                        AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                        callBacekFail.setStatus("1018");
                        callBacekFail.setDesc("Not Enough Balance");
                        return callBacekFail;
                    }
                    LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
                    wrapper.eq(Txns::getMethod,"Free Spin");
                    wrapper.eq(Txns::getStatus,"Running");
                    wrapper.eq(Txns::getPlatformTxId,freeSpinTxns.getPlatformTxId());
                    wrapper.eq(Txns::getPlatform,freeSpinTxns.getPlatform());
                    wrapper.eq(Txns::getGameType,freeSpinTxns.getGameType());
                    wrapper.eq(Txns::getUserId,freeSpinTxns.getUserId());
                    Txns oldTxns = txnsMapper.selectOne(wrapper);
                    if(null!=oldTxns){
                        AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                        callBacekFail.setStatus("1038");
                        callBacekFail.setDesc("Duplicate transaction.");
                        return callBacekFail;
                    }

                    BigDecimal balance = memBaseinfo.getBalance().add(winAmount);
                    gameCommonService.updateUserBalance(memBaseinfo, winAmount, GoldchangeEnum.FREE_SPIN, TradingEnum.INCOME);
                    String dateStr = DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT);
                    Txns txns = new Txns();
                    BeanUtils.copyProperties(freeSpinTxns,txns);
                    txns.setId(null);
                    txns.setBalance(balance);
                    txns.setMethod("Free Spin");
                    txns.setStatus("Running");
                    txns.setCreateTime(dateStr);
                    txnsMapper.insert(txns);
                    AwcCallBackParentRespSuccess callBackSuccess = new AwcCallBackParentRespSuccess();
                    callBackSuccess.setStatus("0000");
                    return callBackSuccess;
                }
            }
        } else {
            AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
            callBacekFail.setStatus("1036");
            callBacekFail.setDesc("Invalid parameters.");
            return callBacekFail;
        }
        return null;
    }

    //  Give (Promotion Bonus) 活动派彩
    private Object give(AwcApiRequestParentData awcApiRequestData,String ip) {
        AwcApiRequestData<List<GiveTxns>> apiRequestData = JSON.parseObject(String.valueOf(awcApiRequestData.getMessage()), new TypeReference<AwcApiRequestData<List<GiveTxns>>>() {
        });
        List<GiveTxns> giveTxnsList = apiRequestData.getTxns();
        if (null != giveTxnsList && giveTxnsList.size() > 0) {
            for (GiveTxns giveTxns : giveTxnsList) {
                if(!checkIp(ip,giveTxns.getPlatform())){
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1029");
                    callBacekFail.setDesc("invalid IP address.");
                    return callBacekFail;
                }
                String userId = giveTxns.getUserId();
                MemBaseinfo memBaseinfo = gameCommonService.getByAccountNo(userId);
                if (null == memBaseinfo) {
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1002");
                    callBacekFail.setDesc("Account is not exists");
                    return callBacekFail;
                } else {
                    LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
                    wrapper.eq(Txns::getMethod,"Give");
                    wrapper.eq(Txns::getStatus,"Running");
                    wrapper.eq(Txns::getPromotionTxId,giveTxns.getPromotionTxId());
                    wrapper.eq(Txns::getPlatform,giveTxns.getPlatform());
                    wrapper.eq(Txns::getPromotionId,giveTxns.getPromotionId());
                    wrapper.eq(Txns::getUserId,giveTxns.getUserId());
                    Txns oldTxns = txnsMapper.selectOne(wrapper);
                    if(null!=oldTxns){
                        AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                        callBacekFail.setStatus("1038");
                        callBacekFail.setDesc("Duplicate transaction.");
                        return callBacekFail;
                    }
                    BigDecimal amount = BigDecimal.valueOf(Double.valueOf(giveTxns.getAmount()));
                    BigDecimal balance = memBaseinfo.getBalance().add(amount);
                    gameCommonService.updateUserBalance(memBaseinfo, amount, GoldchangeEnum.ACTIVITY_GIVE, TradingEnum.INCOME);
                    String dateStr = DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT);
                    Txns txns = new Txns();
                    BeanUtils.copyProperties(giveTxns,txns);
                    txns.setBalance(balance);
                    txns.setMethod("Give");
                    txns.setStatus("Running");
                    txns.setCreateTime(dateStr);
                    txnsMapper.insert(txns);
                    AwcCallBackRespSuccess placeBetSuccess = new AwcCallBackRespSuccess();
                    placeBetSuccess.setStatus("0000");
                    placeBetSuccess.setBalance(balance);
                    placeBetSuccess.setBalanceTs(DateUtils.getTimeAndZone());
                    return placeBetSuccess;
                }
            }
        } else {
            AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
            callBacekFail.setStatus("1036");
            callBacekFail.setDesc("Invalid parameters.");
            return callBacekFail;
        }
        return null;
    }

    //  Tip 打赏
    private Object tip(AwcApiRequestParentData awcApiRequestData,String ip) {
        AwcApiRequestData<List<TipTxns>> apiRequestData = JSON.parseObject(String.valueOf(awcApiRequestData.getMessage()), new TypeReference<AwcApiRequestData<List<TipTxns>>>() {
        });
        List<TipTxns> tipTxnsList = apiRequestData.getTxns();
        if (null != tipTxnsList && tipTxnsList.size() > 0) {
            for (TipTxns tipTxns : tipTxnsList) {
                if(!checkIp(ip,tipTxns.getPlatform())){
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1029");
                    callBacekFail.setDesc("invalid IP address.");
                    return callBacekFail;
                }
                String userId = tipTxns.getUserId();
                MemBaseinfo memBaseinfo = gameCommonService.getByAccountNo(userId);
                if (null == memBaseinfo) {
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1002");
                    callBacekFail.setDesc("Account is not exists");
                    return callBacekFail;
                } else {
                    //打赏给直播主的金额
                    BigDecimal tip = tipTxns.getTip();
                    if(memBaseinfo.getBalance().compareTo(tip) == -1){
                        AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                        callBacekFail.setStatus("1018");
                        callBacekFail.setDesc("Not Enough Balance");
                        return callBacekFail;
                    }
                    LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
                    wrapper.and(c -> c.eq(Txns::getMethod,"Tip").or().eq(Txns::getMethod,"Cancel Tip"));
                    wrapper.eq(Txns::getStatus,"Running");
                    wrapper.eq(Txns::getPlatformTxId,tipTxns.getPlatformTxId());
                    wrapper.eq(Txns::getPlatform,tipTxns.getPlatform());
                    wrapper.eq(Txns::getGameType,tipTxns.getGameType());
                    wrapper.eq(Txns::getUserId,tipTxns.getUserId());
                    Txns oldTxns = txnsMapper.selectOne(wrapper);
                    if(null!=oldTxns){
                        if("Cancel Tip".equals(oldTxns.getMethod())){
                            AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                            callBacekFail.setStatus("1043");
                            callBacekFail.setDesc("Bet has canceled.");
                            return callBacekFail;
                        }else {
                            AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                            callBacekFail.setStatus("1038");
                            callBacekFail.setDesc("Duplicate transaction.");
                            return callBacekFail;
                        }
                    }
                    AwcCallBackRespSuccess placeBetSuccess = new AwcCallBackRespSuccess();
                    placeBetSuccess.setStatus("0000");

                    BigDecimal balance = memBaseinfo.getBalance().subtract(tip);
                    gameCommonService.updateUserBalance(memBaseinfo, tip, GoldchangeEnum.TIP, TradingEnum.SPENDING);
                    placeBetSuccess.setBalance(balance);
                    String dateStr = DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT);
                    placeBetSuccess.setBalanceTs(DateUtils.getTimeAndZone());
                    Txns txns = new Txns();
                    BeanUtils.copyProperties(tipTxns,txns);
                    txns.setBalance(balance);
                    txns.setMethod("Tip");
                    txns.setStatus("Running");
                    txns.setCreateTime(dateStr);
                    txnsMapper.insert(txns);

                    placeBetSuccess.setBalance(balance);
                    placeBetSuccess.setBalanceTs(DateUtils.getTimeAndZone());
                    return placeBetSuccess;
                }
            }
        } else {
            AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
            callBacekFail.setStatus("1036");
            callBacekFail.setDesc("Invalid parameters.");
            return callBacekFail;
        }
        return null;
    }

    //  Cancel Tip 取消打赏
    private Object cancelTip(AwcApiRequestParentData awcApiRequestData,String ip) {
        AwcApiRequestData<List<CancelTipTxns>> apiRequestData = JSON.parseObject(String.valueOf(awcApiRequestData.getMessage()), new TypeReference<AwcApiRequestData<List<CancelTipTxns>>>() {
        });
        List<CancelTipTxns> cancelTipTxnsList = apiRequestData.getTxns();
        if (null != cancelTipTxnsList && cancelTipTxnsList.size() > 0) {
            for (CancelTipTxns cancelTipTxns : cancelTipTxnsList) {
                if(!checkIp(ip,cancelTipTxns.getPlatform())){
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1029");
                    callBacekFail.setDesc("invalid IP address.");
                    return callBacekFail;
                }
                String userId = cancelTipTxns.getUserId();
                MemBaseinfo memBaseinfo = gameCommonService.getByAccountNo(userId);
                if (null == memBaseinfo) {
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1002");
                    callBacekFail.setDesc("Account is not exists");
                    return callBacekFail;
                } else {
                    LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
                    wrapper.and(c -> c.eq(Txns::getMethod,"Tip").or().eq(Txns::getMethod,"Cancel Tip"));
                    wrapper.eq(Txns::getStatus,"Running");
                    wrapper.eq(Txns::getPlatformTxId,cancelTipTxns.getPlatformTxId());
                    wrapper.eq(Txns::getPlatform,cancelTipTxns.getPlatform());
                    wrapper.eq(Txns::getGameType,cancelTipTxns.getGameType());
                    wrapper.eq(Txns::getUserId,cancelTipTxns.getUserId());
                    Txns oldTxns = txnsMapper.selectOne(wrapper);
                    if(null==oldTxns){
                        AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                        callBacekFail.setStatus("1017");
                        callBacekFail.setDesc("TxCode is not exist");
                        return callBacekFail;
                    }else if("Cancel Tip".equals(oldTxns.getMethod())){
                        AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                        callBacekFail.setStatus("1043");
                        callBacekFail.setDesc("Bet has canceled.");
                        return callBacekFail;
                    }

                    //打赏给直播主的金额
                    BigDecimal tip = oldTxns.getBetAmount();
                    BigDecimal balance = memBaseinfo.getBalance().add(tip);
                    gameCommonService.updateUserBalance(memBaseinfo, tip, GoldchangeEnum.CANCEL_TIP, TradingEnum.INCOME);
                    String dateStr = DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT);

                    Txns txns = new Txns();
                    BeanUtils.copyProperties(oldTxns,txns);
                    txns.setId(null);
                    txns.setBalance(balance);
                    txns.setMethod("Cancel Tip");
                    txns.setStatus("Running");
                    txns.setCreateTime(dateStr);
                    txnsMapper.insert(txns);
                    oldTxns.setStatus("Cancel Tip");
                    oldTxns.setUpdateTime(dateStr);
                    txnsMapper.updateById(oldTxns);
                    AwcCallBackRespSuccess placeBetSuccess = new AwcCallBackRespSuccess();
                    placeBetSuccess.setStatus("0000");
                    placeBetSuccess.setBalance(balance);
                    placeBetSuccess.setBalanceTs(DateUtils.getTimeAndZone());
                    return placeBetSuccess;
                }
            }
        } else {
            AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
            callBacekFail.setStatus("1036");
            callBacekFail.setDesc("Invalid parameters.");
            return callBacekFail;
        }
        return null;
    }

    private boolean checkIp(String ip,String platform){
        GamePlatform gamePlatform = gameCommonService.getGamePlatformByplatformCode(platform);
        if(null==gamePlatform){
            return false;
        }else if(null==gamePlatform.getIpAddr()||"".equals(gamePlatform.getIpAddr())){
            return true;
        }else if(gamePlatform.getIpAddr().equals(ip)) {
            return true;
        }
        return false;
    }

}
