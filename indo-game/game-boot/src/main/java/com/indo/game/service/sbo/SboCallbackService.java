package com.indo.game.service.sbo;

import com.indo.game.pojo.entity.sbo.*;

public interface SboCallbackService {

    public Object getBalance(SboCallBackParentReq sboCallBackParentReq);
    //扣除投注金额
    public Object deduct(SboCallBackDeductReq sboCallBackDeductReq);
    //结算投注
    public Object settle(SboCallBackSettleReq sboCallBackSettleReq);
    //回滚
    public Object rollback(SboCallBackRollbackReq sboCallBackRollbackReq);
    //取消投注
    public Object cancel(SboCallBackCancelReq sboCallBackCancelReq);
    //小费
    public Object tip(SboCallBackTipReq sboCallBackTipReq);
    //红利
    public Object bonus(SboCallBackBonusReq sboCallBackBonusReq);
    //归还注额
    public Object returnStake(SboCallBackReturnStakeReq sboCallBackBonusReq);
    //取得投注状态
    public Object getBetStatus(SboCallBackGetBetStatusReq sboCallBackGetBetStatusReq);
    //转帐交易
    public Object transfer(SboCallBackTransferReq sboCallBackTransferReq);
    //转帐交易回滚
    public Object rollbackTransfer(SboCallBackRollbackTransferReq sboCallBackRollbackTransferReq);
    //取得转帐交易状态
    public Object getTransferStatus(SboCallBackGetTransferStautsReq sboCallBackGetTransferStautsReq);
    //LiveCoin購買
    public Object liveCoinTransaction(SboCallBackLiveCoinTransactionReq sboCallBackLiveCoinTransactionReq);
}
