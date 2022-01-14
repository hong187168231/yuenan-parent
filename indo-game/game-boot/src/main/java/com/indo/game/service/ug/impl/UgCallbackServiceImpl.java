package com.indo.game.service.ug.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.indo.common.enums.GoldchangeEnum;
import com.indo.common.enums.TradingEnum;
import com.indo.common.utils.DateUtils;
import com.indo.game.mapper.TxnsMapper;
import com.indo.game.pojo.entity.manage.Txns;
import com.indo.game.pojo.dto.ug.*;
import com.indo.game.pojo.vo.callback.ug.UgCallBackBalanceResp;
import com.indo.game.pojo.vo.callback.ug.UgCallBackCancelResp;
import com.indo.game.pojo.vo.callback.ug.UgCallBackGetBalanceResp;
import com.indo.game.pojo.vo.callback.ug.UgCallBackTransferResp;
import com.indo.game.service.common.GameCommonService;
import com.indo.game.service.ug.UgCallbackService;
import com.indo.user.pojo.entity.MemBaseinfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UgCallbackServiceImpl implements UgCallbackService {

    @Autowired
    private GameCommonService gameCommonService;

    @Autowired
    private TxnsMapper txnsMapper;

    //取得用户的余额
    public Object getBalance(UgCallBackGetBalanceReq ugCallBackGetBalanceReq) {
        MemBaseinfo memBaseinfo = gameCommonService.getByAccountNo(ugCallBackGetBalanceReq.getAccount());
        UgCallBackGetBalanceResp ugCallBackGetBalanceResp = new UgCallBackGetBalanceResp();

        if (null == memBaseinfo) {
            ugCallBackGetBalanceResp.setErrorCode("010001");
            ugCallBackGetBalanceResp.setErrorMessage("会员帐号不存在");
        } else {
            ugCallBackGetBalanceResp.setErrorCode("000000");
            ugCallBackGetBalanceResp.setErrorMessage("操作成功");
            ugCallBackGetBalanceResp.setBalance(memBaseinfo.getBalance());
        }

        return ugCallBackGetBalanceResp;
    }


    //加余额/扣除余额
    public Object transfer(UgCallBackTransferReq<UgCallBackTransactionItemReq> ugCallBackTransactionItemReqUgCallBackTransferReq) {

        List<UgCallBackTransactionItemReq> ugCallBackTransactionItemReqList = ugCallBackTransactionItemReqUgCallBackTransferReq.getData();
        UgCallBackTransferResp ugCallBackTransferResp = new UgCallBackTransferResp();
        List<UgCallBackBalanceResp> ugCallBackBalanceRespList = new ArrayList<>();
        boolean b = false;
        String errorCode = "";
        String errorMessage = "";
        BigDecimal balance = BigDecimal.valueOf(0);
        for (UgCallBackTransactionItemReq ugCallBackTransactionItemReq : ugCallBackTransactionItemReqList) {
            MemBaseinfo memBaseinfo = gameCommonService.getByAccountNo(ugCallBackTransactionItemReq.getAccount());
            UgCallBackBalanceResp ugCallBackBalanceResp = new UgCallBackBalanceResp();
            ugCallBackBalanceResp.setAccount(ugCallBackTransactionItemReq.getAccount());
            ugCallBackBalanceResp.setTransactionNo(ugCallBackTransactionItemReq.getTransactionNo());

            if (null == memBaseinfo) {
                errorCode = "010001";
                errorMessage = "会员帐号不存在";
                ugCallBackBalanceResp.setErrorCode(errorCode);
                ugCallBackBalanceResp.setErrorMessage(errorMessage);

            } else {

                BigDecimal betAmount = ugCallBackTransactionItemReq.getAmount();
                if (memBaseinfo.getBalance().compareTo(betAmount) == -1) {
                    errorCode = "300004";
                    errorMessage = "会员余额不足";
                    ugCallBackBalanceResp.setErrorCode(errorCode);
                    ugCallBackBalanceResp.setErrorMessage(errorMessage);

                } else {
                    if (!b) {
                        balance = memBaseinfo.getBalance();
                    }
                    if (ugCallBackTransactionItemReq.isBet()) {//此交易是否是投注
                        if (BigDecimal.valueOf(0).compareTo(ugCallBackTransactionItemReq.getAmount()) == -1) {
                            gameCommonService.updateUserBalance(memBaseinfo, betAmount, GoldchangeEnum.PLACE_BET, TradingEnum.INCOME);
                        } else {
                            gameCommonService.updateUserBalance(memBaseinfo, betAmount.abs(), GoldchangeEnum.PLACE_BET, TradingEnum.SPENDING);
                        }
                    } else {
                        if (BigDecimal.valueOf(0).compareTo(ugCallBackTransactionItemReq.getAmount()) == -1) {
                            gameCommonService.updateUserBalance(memBaseinfo, betAmount, GoldchangeEnum.BETNSETTLE, TradingEnum.INCOME);
                        } else {
                            gameCommonService.updateUserBalance(memBaseinfo, betAmount.abs(), GoldchangeEnum.BETNSETTLE, TradingEnum.SPENDING);
                        }
                    }
                    gameCommonService.updateUserBalance(memBaseinfo, betAmount, GoldchangeEnum.PLACE_BET, TradingEnum.SPENDING);
                    ugCallBackBalanceResp.setErrorCode("000000");
                    ugCallBackBalanceResp.setErrorMessage("操作成功");

                    b = true;
                    balance = balance.add(betAmount);

                    Txns txns = new Txns();
                    txns.setMethod(ugCallBackTransactionItemReqUgCallBackTransferReq.getMethod());
//                    ugOperInfo.setBet(ugCallBackTransactionItemReq.isBet());
                    txns.setUserId(ugCallBackTransactionItemReq.getAccount());
                    txns.setPlatformTxId(ugCallBackTransactionItemReq.getTransactionNo());
                    String dateStr = DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT);
                    txns.setCreateTime(dateStr);
                    txns.setRoundId(ugCallBackTransactionItemReq.getBetID());
                    txns.setBalance(balance);
                    txns.setStatus("Running");
                    txnsMapper.insert(txns);
                }
            }
            ugCallBackBalanceRespList.add(ugCallBackBalanceResp);
        }
        if (b) {
            ugCallBackTransferResp.setErrorCode("000000");
            ugCallBackTransferResp.setErrorMessage("操作成功");
        } else {
            ugCallBackTransferResp.setErrorCode(errorCode);
            ugCallBackTransferResp.setErrorMessage(errorMessage);
        }

        ugCallBackTransferResp.setBalance(ugCallBackBalanceRespList);

        return ugCallBackTransferResp;

    }


    // 取消交易
    public Object cancel(UgCallBackCancelReq ugCallBackCancelReq) {

        MemBaseinfo memBaseinfo = gameCommonService.getByAccountNo(ugCallBackCancelReq.getAccount());
        UgCallBackCancelResp ugCallBackCancelResp = new UgCallBackCancelResp();
        ugCallBackCancelResp.setAccount(ugCallBackCancelReq.getAccount());
        ugCallBackCancelResp.setTransactionNo(ugCallBackCancelReq.getTransactionNo());

        if (null == memBaseinfo) {
            ugCallBackCancelResp.setErrorCode("010001");
            ugCallBackCancelResp.setErrorMessage("会员帐号不存在");
        } else {
            LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Txns::getStatus, "Running");
            wrapper.eq(Txns::getUserId, ugCallBackCancelReq.getAccount());
            wrapper.eq(Txns::getPlatformTxId, ugCallBackCancelReq.getTransactionNo());
            Txns oldOperInfo = txnsMapper.selectOne(wrapper);
            BigDecimal balance = memBaseinfo.getBalance();
            if (null != oldOperInfo && oldOperInfo.getMethod().equals("Transfer")) {
                BigDecimal betAmount = oldOperInfo.getAmount().abs();
                if (oldOperInfo.getBet()) {//此交易是否是投注
                    if (BigDecimal.valueOf(0).compareTo(oldOperInfo.getAmount()) == -1) {
                        gameCommonService.updateUserBalance(memBaseinfo, betAmount, GoldchangeEnum.PLACE_BET, TradingEnum.SPENDING);
                        balance = balance.subtract(betAmount);
                    } else {
                        gameCommonService.updateUserBalance(memBaseinfo, betAmount, GoldchangeEnum.PLACE_BET, TradingEnum.INCOME);
                        balance = balance.add(betAmount);
                    }
                } else {
                    if (BigDecimal.valueOf(0).compareTo(oldOperInfo.getAmount()) == -1) {
                        gameCommonService.updateUserBalance(memBaseinfo, betAmount, GoldchangeEnum.BETNSETTLE, TradingEnum.SPENDING);
                        balance = balance.subtract(betAmount);
                    } else {
                        gameCommonService.updateUserBalance(memBaseinfo, betAmount, GoldchangeEnum.BETNSETTLE, TradingEnum.INCOME);
                        balance = balance.add(betAmount);
                    }
                }
                Txns txns = new Txns();
                txns.setMethod(ugCallBackCancelReq.getMethod());
                txns.setBet(oldOperInfo.getBet());
                txns.setUserId(oldOperInfo.getUserId());
                txns.setPlatformTxId(ugCallBackCancelReq.getTransactionNo());
                String dateStr = DateUtils.format(new Date(), DateUtils.ISO8601_DATE_FORMAT);
                txns.setCreateTime(dateStr);
                txns.setRoundId(oldOperInfo.getRoundId());
                txns.setBalance(balance);
                txns.setStatus("Running");
                txnsMapper.insert(txns);
                oldOperInfo.setStatus("Cancel");
                txnsMapper.updateById(oldOperInfo);
            }
            ugCallBackCancelResp.setErrorCode("000000");
            ugCallBackCancelResp.setErrorMessage("操作成功");

        }

        return ugCallBackCancelResp;

    }


    //检查交易结果
    public Object check(UgCallBackCancelReq ugCallBackCancelReq) {

        MemBaseinfo memBaseinfo = gameCommonService.getByAccountNo(ugCallBackCancelReq.getAccount());
        UgCallBackBalanceResp ugCallBackBalanceResp = new UgCallBackBalanceResp();
        ugCallBackBalanceResp.setAccount(ugCallBackCancelReq.getAccount());
        ugCallBackBalanceResp.setTransactionNo(ugCallBackCancelReq.getTransactionNo());

        if (null == memBaseinfo) {
            ugCallBackBalanceResp.setErrorCode("010001");
            ugCallBackBalanceResp.setErrorMessage("会员帐号不存在");
        } else {
            LambdaQueryWrapper<Txns> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Txns::getStatus, "Running");
            wrapper.eq(Txns::getUserId, ugCallBackCancelReq.getAccount());
            wrapper.eq(Txns::getPlatformTxId, ugCallBackCancelReq.getTransactionNo());
            Txns oldOperInfo = txnsMapper.selectOne(wrapper);
            if (null == oldOperInfo) {
                ugCallBackBalanceResp.setErrorCode("300002");
                ugCallBackBalanceResp.setErrorMessage("存取款失败");
            }
            ugCallBackBalanceResp.setErrorCode("000000");
            ugCallBackBalanceResp.setErrorMessage("操作成功");

            ugCallBackBalanceResp.setBalance(memBaseinfo.getBalance());
        }

        return ugCallBackBalanceResp;

    }
}
