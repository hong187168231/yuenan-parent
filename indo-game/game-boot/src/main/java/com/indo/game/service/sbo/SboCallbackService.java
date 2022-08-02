package com.indo.game.service.sbo;

import com.indo.game.pojo.dto.sbo.*;

public interface SboCallbackService {

    public Object getBalance(SboCallBackParentReq sboCallBackParentReq,String ip);
    //扣除投注金额
    public Object deduct(SboCallBackDeductReq sboCallBackDeductReq,String ip);
    //结算投注
    public Object settle(SboCallBackSettleReq sboCallBackSettleReq,String ip);
    //回滚
    public Object rollback(SboCallBackRollbackReq sboCallBackRollbackReq,String ip);
    //取消投注
    public Object cancel(SboCallBackCancelReq sboCallBackCancelReq,String ip);
    //小费
    public Object tip(SboCallBackTipReq sboCallBackTipReq,String ip);
    //红利
    public Object bonus(SboCallBackBonusReq sboCallBackBonusReq,String ip);
    //归还注额
    public Object returnStake(SboCallBackReturnStakeReq sboCallBackBonusReq,String ip);
    //取得投注状态
    public Object getBetStatus(SboCallBackGetBetStatusReq sboCallBackGetBetStatusReq,String ip);
//    //转帐交易
//    public Object transfer(SboCallBackTransferReq sboCallBackTransferReq);
//    //转帐交易回滚
//    public Object rollbackTransfer(SboCallBackRollbackTransferReq sboCallBackRollbackTransferReq);
//    //取得转帐交易状态
//    public Object getTransferStatus(SboCallBackGetTransferStautsReq sboCallBackGetTransferStautsReq);
    //LiveCoin購買
    public Object liveCoinTransaction(SboCallBackLiveCoinTransactionReq sboCallBackLiveCoinTransactionReq,String ip);
}
