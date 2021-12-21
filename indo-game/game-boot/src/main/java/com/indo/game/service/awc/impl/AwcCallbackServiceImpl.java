package com.indo.game.service.awc.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.indo.common.enums.GoldchangeEnum;
import com.indo.common.enums.TradingEnum;
import com.indo.common.utils.DateUtils;
import com.indo.game.config.OpenAPIProperties;
import com.indo.game.pojo.entity.awc.*;
import com.indo.game.pojo.entity.manage.GamePlatform;
import com.indo.game.pojo.vo.callback.awc.AwcCallBackParentRespSuccess;
import com.indo.game.pojo.vo.callback.awc.AwcCallBackRespFail;
import com.indo.game.pojo.vo.callback.awc.AwcCallBackRespSuccess;
import com.indo.game.pojo.vo.callback.awc.AwcGetBalanceRespSuccess;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.awc.AwcCallbackService;
import com.indo.user.pojo.entity.MemBaseinfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class AwcCallbackServiceImpl implements AwcCallbackService {

    @Autowired
    private GameCommonService gameCommonService;

    public String awcCallback(AwcApiRequestParentData awcApiRequestData,String ip) {
        if(OpenAPIProperties.AWC_API_SECRET_KEY.equals(awcApiRequestData.getKey())){
            AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
            callBacekFail.setStatus("1008");
            callBacekFail.setDesc("Invalid token!");
            return JSONObject.toJSONString(callBacekFail);
        }
        JSONObject jsonObject = JSONObject.parseObject(String.valueOf(awcApiRequestData.getMessage()));
        String action = jsonObject.getString("action");
        //Get Balance 取得玩家余额
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
        return "";
    }

    //Get Balance 取得玩家余额
    private String getBalance(JSONObject jsonObject) {
        String userId = jsonObject.getString("userId");
        MemBaseinfo memBaseinfo = gameCommonService.getByAccountNo(userId);
        if (null == memBaseinfo) {
            AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
            callBacekFail.setStatus("1002");
            callBacekFail.setDesc("Account is not exists");
            return JSONObject.toJSONString(callBacekFail);
        } else {
            AwcGetBalanceRespSuccess getBalanceSuccess = new AwcGetBalanceRespSuccess();
            getBalanceSuccess.setStatus("0000");
            getBalanceSuccess.setBalance(memBaseinfo.getBalance().toString());
            getBalanceSuccess.setBalanceTs(DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT));
            getBalanceSuccess.setUserId(userId);
            return JSONObject.toJSONString(getBalanceSuccess);
        }


    }


    //Place Bet 下注
    private String bet(AwcApiRequestParentData awcApiRequestData,String ip) {
        AwcApiRequestData<List<PlaceBetTxns>> apiRequestData = JSON.parseObject(String.valueOf(awcApiRequestData.getMessage()), new TypeReference<AwcApiRequestData<List<PlaceBetTxns>>>() {
        });
        List<PlaceBetTxns> placeBetTxnsList = apiRequestData.getTxns();
        if (null != placeBetTxnsList && placeBetTxnsList.size() > 0) {
            for (PlaceBetTxns placeBetTxns : placeBetTxnsList) {
                if(!checkIp(ip,placeBetTxns.getPlatform())){
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1029");
                    callBacekFail.setDesc("invalid IP address.");
                    return JSONObject.toJSONString(callBacekFail);
                }
                String userId = placeBetTxns.getUserId();

                MemBaseinfo memBaseinfo = gameCommonService.getByAccountNo(userId);
                if (null == memBaseinfo) {
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1002");
                    callBacekFail.setDesc("Account is not exists");
                    return JSONObject.toJSONString(callBacekFail);
                } else {
                    AwcCallBackRespSuccess placeBetSuccess = new AwcCallBackRespSuccess();
                    placeBetSuccess.setStatus("0000");
                    BigDecimal betAmount = BigDecimal.valueOf(Double.valueOf(placeBetTxns.getBetAmount()));
                    BigDecimal balance = memBaseinfo.getBalance().subtract(betAmount);
                    gameCommonService.updateUserBalance(memBaseinfo, betAmount, GoldchangeEnum.PLACE_BET, TradingEnum.SPENDING);
                    placeBetSuccess.setBalance(balance.toString());
                    placeBetSuccess.setBalanceTs(DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT));
                    return JSONObject.toJSONString(placeBetSuccess);
                }
            }
        } else {
            AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
            callBacekFail.setStatus("1036");
            callBacekFail.setDesc("Invalid parameters.");
            return JSONObject.toJSONString(callBacekFail);
        }
        return null;
    }

//        gameCommonService.inOrOutBalanceCommon(GoldchangeEnum.AWCAESEXYBCRT_IN.getValue(), balance, memBaseinfo, content, cptOpenMember, Constants.AWC_AESEXYBCRT_ACCOUNT_TYPE);

    //Cancel Bet 取消注单
    private String cancelBet(AwcApiRequestParentData awcApiRequestData,String ip) {
        AwcApiRequestData<List<CancelBetTxns>> apiRequestData = JSON.parseObject(String.valueOf(awcApiRequestData.getMessage()), new TypeReference<AwcApiRequestData<List<CancelBetTxns>>>() {
        });
        List<CancelBetTxns> cancelBetTxnsList = apiRequestData.getTxns();
        if (null != cancelBetTxnsList && cancelBetTxnsList.size() > 0) {
            for (CancelBetTxns cancelBetTxns : cancelBetTxnsList) {
                if(!checkIp(ip,cancelBetTxns.getPlatform())){
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1029");
                    callBacekFail.setDesc("invalid IP address.");
                    return JSONObject.toJSONString(callBacekFail);
                }
                String userId = cancelBetTxns.getUserId();
                MemBaseinfo memBaseinfo = gameCommonService.getByAccountNo(userId);
                if (null == memBaseinfo) {
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1002");
                    callBacekFail.setDesc("Account is not exists");
                    return JSONObject.toJSONString(callBacekFail);
                } else {
                    AwcCallBackRespSuccess placeBetSuccess = new AwcCallBackRespSuccess();
                    placeBetSuccess.setStatus("0000");
                    //查询下注订单
                    BigDecimal betAmount = BigDecimal.valueOf(Double.valueOf(0));
                    BigDecimal balance = memBaseinfo.getBalance().add(betAmount);
                    placeBetSuccess.setBalance(balance.toString());
                    placeBetSuccess.setBalanceTs(DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT));
                    return JSONObject.toJSONString(placeBetSuccess);
                }
            }
        } else {
            AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
            callBacekFail.setStatus("1036");
            callBacekFail.setDesc("Invalid parameters.");
            return JSONObject.toJSONString(callBacekFail);
        }
        return null;
    }

    //Adjust Bet 调整投注
    private String adjustBet(AwcApiRequestParentData awcApiRequestData,String ip) {
        AwcApiRequestData<List<AdjustBetTxns>> apiRequestData = JSON.parseObject(String.valueOf(awcApiRequestData.getMessage()), new TypeReference<AwcApiRequestData<List<AdjustBetTxns>>>() {
        });
        List<AdjustBetTxns> adjustBetTxnsList = apiRequestData.getTxns();
        if (null != adjustBetTxnsList && adjustBetTxnsList.size() > 0) {
            for (AdjustBetTxns adjustBetTxns : adjustBetTxnsList) {
                if(!checkIp(ip,adjustBetTxns.getPlatform())){
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1029");
                    callBacekFail.setDesc("invalid IP address.");
                    return JSONObject.toJSONString(callBacekFail);
                }
                String userId = adjustBetTxns.getUserId();
                MemBaseinfo memBaseinfo = gameCommonService.getByAccountNo(userId);
                if (null == memBaseinfo) {
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1002");
                    callBacekFail.setDesc("Account is not exists");
                    return JSONObject.toJSONString(callBacekFail);
                } else {
                    AwcCallBackRespSuccess placeBetSuccess = new AwcCallBackRespSuccess();
                    placeBetSuccess.setStatus("0000");
                    // 下注金额
                    BigDecimal adjustAmount = BigDecimal.valueOf(Double.valueOf(adjustBetTxns.getAdjustAmount()));
                    //需要调整的超收金额，返还因为赔率超收金额 下注金额减去实际的下注金额
                    BigDecimal realWinAmount = adjustAmount.subtract(BigDecimal.valueOf(Double.valueOf(adjustBetTxns.getBetAmount())));
                    BigDecimal balance = memBaseinfo.getBalance().add(realWinAmount);
                    placeBetSuccess.setBalance(balance.toString());
                    placeBetSuccess.setBalanceTs(DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT));
                    return JSONObject.toJSONString(placeBetSuccess);
                }
            }
        } else {
            AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
            callBacekFail.setStatus("1036");
            callBacekFail.setDesc("Invalid parameters.");
            return JSONObject.toJSONString(callBacekFail);
        }
        return null;
    }

    //Void Bet 交易作废
    private String voidBet(AwcApiRequestParentData awcApiRequestData,String ip) {
        AwcApiRequestData<List<VoidBetTxns>> apiRequestData = JSON.parseObject(String.valueOf(awcApiRequestData.getMessage()), new TypeReference<AwcApiRequestData<List<VoidBetTxns>>>() {
        });
        List<VoidBetTxns> voidBetTxnsList = apiRequestData.getTxns();
        if (null != voidBetTxnsList && voidBetTxnsList.size() > 0) {
            for (VoidBetTxns voidBetTxns : voidBetTxnsList) {
                if(!checkIp(ip,voidBetTxns.getPlatform())){
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1029");
                    callBacekFail.setDesc("invalid IP address.");
                    return JSONObject.toJSONString(callBacekFail);
                }
                String userId = voidBetTxns.getUserId();
                MemBaseinfo memBaseinfo = gameCommonService.getByAccountNo(userId);
                if (null == memBaseinfo) {
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1002");
                    callBacekFail.setDesc("Account is not exists");
                    return JSONObject.toJSONString(callBacekFail);
                } else {
                    AwcCallBackParentRespSuccess callBackSuccess = new AwcCallBackParentRespSuccess();
                    callBackSuccess.setStatus("0000");
                    return JSONObject.toJSONString(callBackSuccess);
                }
            }
        } else {
            AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
            callBacekFail.setStatus("1036");
            callBacekFail.setDesc("Invalid parameters.");
            return JSONObject.toJSONString(callBacekFail);
        }
        return null;
    }

    //Unvoid Bet 取消交易作废
    private String unvoidBet(AwcApiRequestParentData awcApiRequestData,String ip) {
        AwcApiRequestData<List<UnvoidBetTxns>> apiRequestData = JSON.parseObject(String.valueOf(awcApiRequestData.getMessage()), new TypeReference<AwcApiRequestData<List<UnvoidBetTxns>>>() {
        });
        List<UnvoidBetTxns> unvoidBetTxnsList = apiRequestData.getTxns();
        if (null != unvoidBetTxnsList && unvoidBetTxnsList.size() > 0) {
            for (UnvoidBetTxns unvoidBetTxns : unvoidBetTxnsList) {
                if(!checkIp(ip,unvoidBetTxns.getPlatform())){
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1029");
                    callBacekFail.setDesc("invalid IP address.");
                    return JSONObject.toJSONString(callBacekFail);
                }
                String userId = unvoidBetTxns.getUserId();
                MemBaseinfo memBaseinfo = gameCommonService.getByAccountNo(userId);
                if (null == memBaseinfo) {
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1002");
                    callBacekFail.setDesc("Account is not exists");
                    return JSONObject.toJSONString(callBacekFail);
                } else {
                    AwcCallBackParentRespSuccess callBackSuccess = new AwcCallBackParentRespSuccess();
                    callBackSuccess.setStatus("0000");
                    return JSONObject.toJSONString(callBackSuccess);
                }
            }
        } else {
            AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
            callBacekFail.setStatus("1036");
            callBacekFail.setDesc("Invalid parameters.");
            return JSONObject.toJSONString(callBacekFail);
        }
        return null;
    }

    //Refund 返还金额
    private String refund(AwcApiRequestParentData awcApiRequestData,String ip) {
        AwcApiRequestData<List<RefundTxns>> apiRequestData = JSON.parseObject(String.valueOf(awcApiRequestData.getMessage()), new TypeReference<AwcApiRequestData<List<RefundTxns>>>() {
        });
        List<RefundTxns> refundTxnsList = apiRequestData.getTxns();
        if (null != refundTxnsList && refundTxnsList.size() > 0) {
            for (RefundTxns refundTxns : refundTxnsList) {
                if(!checkIp(ip,refundTxns.getPlatform())){
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1029");
                    callBacekFail.setDesc("invalid IP address.");
                    return JSONObject.toJSONString(callBacekFail);
                }
                String userId = refundTxns.getUserId();
                MemBaseinfo memBaseinfo = gameCommonService.getByAccountNo(userId);
                if (null == memBaseinfo) {
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1002");
                    callBacekFail.setDesc("Account is not exists");
                    return JSONObject.toJSONString(callBacekFail);
                } else {
                    AwcCallBackParentRespSuccess callBackSuccess = new AwcCallBackParentRespSuccess();
                    callBackSuccess.setStatus("0000");
                    return JSONObject.toJSONString(callBackSuccess);
                }
            }
        } else {
            AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
            callBacekFail.setStatus("1036");
            callBacekFail.setDesc("Invalid parameters.");
            return JSONObject.toJSONString(callBacekFail);
        }
        return null;
    }

    //Settle 已结帐派彩
    private String settle(AwcApiRequestParentData awcApiRequestData,String ip) {
        AwcApiRequestData<List<SettleTxns>> apiRequestData = JSON.parseObject(String.valueOf(awcApiRequestData.getMessage()), new TypeReference<AwcApiRequestData<List<SettleTxns>>>() {
        });
        List<SettleTxns> settleTxnsList = apiRequestData.getTxns();
        if (null != settleTxnsList && settleTxnsList.size() > 0) {
            for (SettleTxns settleTxns : settleTxnsList) {
                if(!checkIp(ip,settleTxns.getPlatform())){
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1029");
                    callBacekFail.setDesc("invalid IP address.");
                    return JSONObject.toJSONString(callBacekFail);
                }
                String userId = settleTxns.getUserId();
                MemBaseinfo memBaseinfo = gameCommonService.getByAccountNo(userId);
                if (null == memBaseinfo) {
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1002");
                    callBacekFail.setDesc("Account is not exists");
                    return JSONObject.toJSONString(callBacekFail);
                } else {
                    AwcCallBackParentRespSuccess callBackSuccess = new AwcCallBackParentRespSuccess();
                    callBackSuccess.setStatus("0000");
                    return JSONObject.toJSONString(callBackSuccess);
                }
            }
        } else {
            AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
            callBacekFail.setStatus("1036");
            callBacekFail.setDesc("Invalid parameters.");
            return JSONObject.toJSONString(callBacekFail);
        }
        return null;
    }

    //Unsettle 取消结帐派彩
    private String unsettle(AwcApiRequestParentData awcApiRequestData,String ip) {
        AwcApiRequestData<List<UnsettleTxns>> apiRequestData = JSON.parseObject(String.valueOf(awcApiRequestData.getMessage()), new TypeReference<AwcApiRequestData<List<UnsettleTxns>>>() {
        });
        List<UnsettleTxns> unsettleTxnsList = apiRequestData.getTxns();
        if (null != unsettleTxnsList && unsettleTxnsList.size() > 0) {
            for (UnsettleTxns unsettleTxns : unsettleTxnsList) {
                if(!checkIp(ip,unsettleTxns.getPlatform())){
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1029");
                    callBacekFail.setDesc("invalid IP address.");
                    return JSONObject.toJSONString(callBacekFail);
                }
                String userId = unsettleTxns.getUserId();
                MemBaseinfo memBaseinfo = gameCommonService.getByAccountNo(userId);
                if (null == memBaseinfo) {
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1002");
                    callBacekFail.setDesc("Account is not exists");
                    return JSONObject.toJSONString(callBacekFail);
                } else {
                    AwcCallBackParentRespSuccess callBackSuccess = new AwcCallBackParentRespSuccess();
                    callBackSuccess.setStatus("0000");
                    return JSONObject.toJSONString(callBackSuccess);
                }
            }
        } else {
            AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
            callBacekFail.setStatus("1036");
            callBacekFail.setDesc("Invalid parameters.");
            return JSONObject.toJSONString(callBacekFail);
        }
        return null;
    }

    //Void Settle 结帐单转为无效
    private String voidSettle(AwcApiRequestParentData awcApiRequestData,String ip) {
        AwcApiRequestData<List<VoidSettleTxns>> apiRequestData = JSON.parseObject(String.valueOf(awcApiRequestData.getMessage()), new TypeReference<AwcApiRequestData<List<VoidSettleTxns>>>() {
        });
        List<VoidSettleTxns> voidSettleTxnsList = apiRequestData.getTxns();
        if (null != voidSettleTxnsList && voidSettleTxnsList.size() > 0) {
            for (VoidSettleTxns voidSettleTxns : voidSettleTxnsList) {
                if(!checkIp(ip,voidSettleTxns.getPlatform())){
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1029");
                    callBacekFail.setDesc("invalid IP address.");
                    return JSONObject.toJSONString(callBacekFail);
                }
                String userId = voidSettleTxns.getUserId();
                MemBaseinfo memBaseinfo = gameCommonService.getByAccountNo(userId);
                if (null == memBaseinfo) {
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1002");
                    callBacekFail.setDesc("Account is not exists");
                    return JSONObject.toJSONString(callBacekFail);
                } else {
                    AwcCallBackParentRespSuccess callBackSuccess = new AwcCallBackParentRespSuccess();
                    callBackSuccess.setStatus("0000");
                    return JSONObject.toJSONString(callBackSuccess);
                }
            }
        } else {
            AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
            callBacekFail.setStatus("1036");
            callBacekFail.setDesc("Invalid parameters.");
            return JSONObject.toJSONString(callBacekFail);
        }
        return null;
    }

    //Unvoid Settle 无效单结账
    private String unvoidSettle(AwcApiRequestParentData awcApiRequestData,String ip) {
        AwcApiRequestData<List<UnvoidSettleTxns>> apiRequestData = JSON.parseObject(String.valueOf(awcApiRequestData.getMessage()), new TypeReference<AwcApiRequestData<List<UnvoidSettleTxns>>>() {
        });
        List<UnvoidSettleTxns> unvoidSettleTxnsList = apiRequestData.getTxns();
        if (null != unvoidSettleTxnsList && unvoidSettleTxnsList.size() > 0) {
            for (UnvoidSettleTxns unvoidSettleTxns : unvoidSettleTxnsList) {
                if(!checkIp(ip,unvoidSettleTxns.getPlatform())){
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1029");
                    callBacekFail.setDesc("invalid IP address.");
                    return JSONObject.toJSONString(callBacekFail);
                }
                String userId = unvoidSettleTxns.getUserId();
                MemBaseinfo memBaseinfo = gameCommonService.getByAccountNo(userId);
                if (null == memBaseinfo) {
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1002");
                    callBacekFail.setDesc("Account is not exists");
                    return JSONObject.toJSONString(callBacekFail);
                } else {
                    AwcCallBackRespSuccess placeBetSuccess = new AwcCallBackRespSuccess();
                    placeBetSuccess.setStatus("0000");
                    placeBetSuccess.setBalance(userId);
                    placeBetSuccess.setBalanceTs(DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT));
                    return JSONObject.toJSONString(placeBetSuccess);
                }
            }
        } else {
            AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
            callBacekFail.setStatus("1036");
            callBacekFail.setDesc("Invalid parameters.");
            return JSONObject.toJSONString(callBacekFail);
        }
        return null;
    }

    // BetNSettle 下注并直接结算
    private String betNSettle(AwcApiRequestParentData awcApiRequestData,String ip) {
        AwcApiRequestData<List<BetNSettleTxns>> apiRequestData = JSON.parseObject(String.valueOf(awcApiRequestData.getMessage()), new TypeReference<AwcApiRequestData<List<BetNSettleTxns>>>() {
        });
        List<BetNSettleTxns> betNSettleTxnsList = apiRequestData.getTxns();
        if (null != betNSettleTxnsList && betNSettleTxnsList.size() > 0) {
            for (BetNSettleTxns betNSettleTxns : betNSettleTxnsList) {
                if(!checkIp(ip,betNSettleTxns.getPlatform())){
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1029");
                    callBacekFail.setDesc("invalid IP address.");
                    return JSONObject.toJSONString(callBacekFail);
                }
                String userId = betNSettleTxns.getUserId();
                MemBaseinfo memBaseinfo = gameCommonService.getByAccountNo(userId);
                if (null == memBaseinfo) {
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1002");
                    callBacekFail.setDesc("Account is not exists");
                    return JSONObject.toJSONString(callBacekFail);
                } else {
                    AwcCallBackRespSuccess placeBetSuccess = new AwcCallBackRespSuccess();
                    placeBetSuccess.setStatus("0000");
                    placeBetSuccess.setBalance(userId);
                    placeBetSuccess.setBalanceTs(DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT));
                    return JSONObject.toJSONString(placeBetSuccess);
                }
            }
        } else {
            AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
            callBacekFail.setStatus("1036");
            callBacekFail.setDesc("Invalid parameters.");
            return JSONObject.toJSONString(callBacekFail);
        }
        return null;
    }

    // Cancel BetNSettle 取消结算并取消注单
    private String cancelBetNSettle(AwcApiRequestParentData awcApiRequestData,String ip) {
        AwcApiRequestData<List<CancelBetNSettleTxns>> apiRequestData = JSON.parseObject(String.valueOf(awcApiRequestData.getMessage()), new TypeReference<AwcApiRequestData<List<CancelBetNSettleTxns>>>() {
        });
        List<CancelBetNSettleTxns> cancelBetNSettleTxnsList = apiRequestData.getTxns();
        if (null != cancelBetNSettleTxnsList && cancelBetNSettleTxnsList.size() > 0) {
            for (CancelBetNSettleTxns cancelBetNSettleTxns : cancelBetNSettleTxnsList) {
                if(!checkIp(ip,cancelBetNSettleTxns.getPlatform())){
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1029");
                    callBacekFail.setDesc("invalid IP address.");
                    return JSONObject.toJSONString(callBacekFail);
                }
                String userId = cancelBetNSettleTxns.getUserId();
                MemBaseinfo memBaseinfo = gameCommonService.getByAccountNo(userId);
                if (null == memBaseinfo) {
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1002");
                    callBacekFail.setDesc("Account is not exists");
                    return JSONObject.toJSONString(callBacekFail);
                } else {
                    AwcCallBackRespSuccess placeBetSuccess = new AwcCallBackRespSuccess();
                    placeBetSuccess.setStatus("0000");
                    placeBetSuccess.setBalance(userId);
                    placeBetSuccess.setBalanceTs(DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT));
                    return JSONObject.toJSONString(placeBetSuccess);
                }
            }
        } else {
            AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
            callBacekFail.setStatus("1036");
            callBacekFail.setDesc("Invalid parameters.");
            return JSONObject.toJSONString(callBacekFail);
        }
        return null;
    }

    // Free Spin 免费旋转
    private String freeSpin(AwcApiRequestParentData awcApiRequestData,String ip) {
        AwcApiRequestData<List<FreeSpinTxns>> apiRequestData = JSON.parseObject(String.valueOf(awcApiRequestData.getMessage()), new TypeReference<AwcApiRequestData<List<FreeSpinTxns>>>() {
        });
        List<FreeSpinTxns> freeSpinTxnsList = apiRequestData.getTxns();
        if (null != freeSpinTxnsList && freeSpinTxnsList.size() > 0) {
            for (FreeSpinTxns freeSpinTxns : freeSpinTxnsList) {
                if(!checkIp(ip,freeSpinTxns.getPlatform())){
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1029");
                    callBacekFail.setDesc("invalid IP address.");
                    return JSONObject.toJSONString(callBacekFail);
                }
                String userId = freeSpinTxns.getUserId();
                MemBaseinfo memBaseinfo = gameCommonService.getByAccountNo(userId);
                if (null == memBaseinfo) {
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1002");
                    callBacekFail.setDesc("Account is not exists");
                    return JSONObject.toJSONString(callBacekFail);
                } else {
                    AwcCallBackParentRespSuccess callBackSuccess = new AwcCallBackParentRespSuccess();
                    callBackSuccess.setStatus("0000");
                    return JSONObject.toJSONString(callBackSuccess);
                }
            }
        } else {
            AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
            callBacekFail.setStatus("1036");
            callBacekFail.setDesc("Invalid parameters.");
            return JSONObject.toJSONString(callBacekFail);
        }
        return null;
    }

    //  Give (Promotion Bonus) 活动派彩
    private String give(AwcApiRequestParentData awcApiRequestData,String ip) {
        AwcApiRequestData<List<GiveTxns>> apiRequestData = JSON.parseObject(String.valueOf(awcApiRequestData.getMessage()), new TypeReference<AwcApiRequestData<List<GiveTxns>>>() {
        });
        List<GiveTxns> giveTxnsList = apiRequestData.getTxns();
        if (null != giveTxnsList && giveTxnsList.size() > 0) {
            for (GiveTxns giveTxns : giveTxnsList) {
                if(!checkIp(ip,giveTxns.getPlatform())){
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1029");
                    callBacekFail.setDesc("invalid IP address.");
                    return JSONObject.toJSONString(callBacekFail);
                }
                String userId = giveTxns.getUserId();
                MemBaseinfo memBaseinfo = gameCommonService.getByAccountNo(userId);
                if (null == memBaseinfo) {
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1002");
                    callBacekFail.setDesc("Account is not exists");
                    return JSONObject.toJSONString(callBacekFail);
                } else {
                    AwcCallBackRespSuccess placeBetSuccess = new AwcCallBackRespSuccess();
                    placeBetSuccess.setStatus("0000");
                    placeBetSuccess.setBalance(userId);
                    placeBetSuccess.setBalanceTs(DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT));
                    return JSONObject.toJSONString(placeBetSuccess);
                }
            }
        } else {
            AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
            callBacekFail.setStatus("1036");
            callBacekFail.setDesc("Invalid parameters.");
            return JSONObject.toJSONString(callBacekFail);
        }
        return null;
    }

    //  Tip 打赏
    private String tip(AwcApiRequestParentData awcApiRequestData,String ip) {
        AwcApiRequestData<List<TipTxns>> apiRequestData = JSON.parseObject(String.valueOf(awcApiRequestData.getMessage()), new TypeReference<AwcApiRequestData<List<TipTxns>>>() {
        });
        List<TipTxns> tipTxnsList = apiRequestData.getTxns();
        if (null != tipTxnsList && tipTxnsList.size() > 0) {
            for (TipTxns tipTxns : tipTxnsList) {
                if(!checkIp(ip,tipTxns.getPlatform())){
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1029");
                    callBacekFail.setDesc("invalid IP address.");
                    return JSONObject.toJSONString(callBacekFail);
                }
                String userId = tipTxns.getUserId();
                MemBaseinfo memBaseinfo = gameCommonService.getByAccountNo(userId);
                if (null == memBaseinfo) {
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1002");
                    callBacekFail.setDesc("Account is not exists");
                    return JSONObject.toJSONString(callBacekFail);
                } else {
                    AwcCallBackRespSuccess placeBetSuccess = new AwcCallBackRespSuccess();
                    placeBetSuccess.setStatus("0000");
                    //打赏给直播主的金额
                    BigDecimal tip = BigDecimal.valueOf(Double.valueOf(tipTxns.getTip()));
                    BigDecimal balance = memBaseinfo.getBalance().subtract(tip);
                    placeBetSuccess.setBalance(balance.toString());
                    placeBetSuccess.setBalance(userId);
                    placeBetSuccess.setBalanceTs(DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT));
                    return JSONObject.toJSONString(placeBetSuccess);
                }
            }
        } else {
            AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
            callBacekFail.setStatus("1036");
            callBacekFail.setDesc("Invalid parameters.");
            return JSONObject.toJSONString(callBacekFail);
        }
        return null;
    }

    //  Cancel Tip 取消打赏
    private String cancelTip(AwcApiRequestParentData awcApiRequestData,String ip) {
        AwcApiRequestData<List<CancelTipTxns>> apiRequestData = JSON.parseObject(String.valueOf(awcApiRequestData.getMessage()), new TypeReference<AwcApiRequestData<List<CancelTipTxns>>>() {
        });
        List<CancelTipTxns> cancelTipTxnsList = apiRequestData.getTxns();
        if (null != cancelTipTxnsList && cancelTipTxnsList.size() > 0) {
            for (CancelTipTxns cancelTipTxns : cancelTipTxnsList) {
                if(!checkIp(ip,cancelTipTxns.getPlatform())){
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1029");
                    callBacekFail.setDesc("invalid IP address.");
                    return JSONObject.toJSONString(callBacekFail);
                }
                String userId = cancelTipTxns.getUserId();
                MemBaseinfo memBaseinfo = gameCommonService.getByAccountNo(userId);
                if (null == memBaseinfo) {
                    AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
                    callBacekFail.setStatus("1002");
                    callBacekFail.setDesc("Account is not exists");
                    return JSONObject.toJSONString(callBacekFail);
                } else {
                    AwcCallBackRespSuccess placeBetSuccess = new AwcCallBackRespSuccess();
                    placeBetSuccess.setStatus("0000");
                    //打赏给直播主的金额
                    BigDecimal tip = BigDecimal.valueOf(Double.valueOf(0));
                    BigDecimal balance = memBaseinfo.getBalance().subtract(tip);
                    placeBetSuccess.setBalance(balance.toString());
                    placeBetSuccess.setBalanceTs(DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT));
                    return JSONObject.toJSONString(placeBetSuccess);
                }
            }
        } else {
            AwcCallBackRespFail callBacekFail = new AwcCallBackRespFail();
            callBacekFail.setStatus("1036");
            callBacekFail.setDesc("Invalid parameters.");
            return JSONObject.toJSONString(callBacekFail);
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
