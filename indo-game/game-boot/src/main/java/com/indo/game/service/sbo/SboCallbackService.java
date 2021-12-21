package com.indo.game.service.sbo;

import com.indo.game.pojo.entity.sbo.*;

public interface SboCallbackService {

    public String getBalance(SboCallBackParentReq sboCallBackParentReq);
    //扣除投注金额
    public String deduct(SboCallBackDeductReq sboCallBackDeductReq);
    //结算投注
    public String settle(SboCallBackSettleReq sboCallBackSettleReq);
    //回滚
    public String rollback(SboCallBackRollbackReq sboCallBackRollbackReq);
    //取消投注
    public String cancel(SboCallBackCancelReq sboCallBackCancelReq);
    //小费
    public String tip(SboCallBackTipReq sboCallBackTipReq);
    //红利
    public String bonus(SboCallBackBonusReq sboCallBackBonusReq);
    //归还注额
    public String returnStake(SboCallBackReturnStakeReq sboCallBackBonusReq);
    //取得投注状态
    public String getBetStatus(SboCallBackGetBetStatusReq sboCallBackGetBetStatusReq);
    //转帐交易
    public String transfer(SboCallBackTransferReq sboCallBackTransferReq);
    //转帐交易回滚
    public String rollbackTransfer(SboCallBackRollbackTransferReq sboCallBackRollbackTransferReq);
    //取得转帐交易状态
    public String getTransferStatus(SboCallBackGetTransferStautsReq sboCallBackGetTransferStautsReq);
    //LiveCoin購買
    public String liveCoinTransaction(SboCallBackLiveCoinTransactionReq sboCallBackLiveCoinTransactionReq);
}
