package com.indo.game.service.sbo;

import com.indo.game.pojo.dto.sbo.*;

public interface SboCallbackService {

    Object getBalance(SboCallBackParentReq sboCallBackParentReq);

    //扣除投注金额
    Object deduct(SboCallBackDeductReq sboCallBackDeductReq);

    //结算投注
    Object settle(SboCallBackSettleReq sboCallBackSettleReq);

    //回滚
    Object rollback(SboCallBackRollbackReq sboCallBackRollbackReq);

    //取消投注
    Object cancel(SboCallBackCancelReq sboCallBackCancelReq);

    //小费
    Object tip(SboCallBackTipReq sboCallBackTipReq);

    //红利
    Object bonus(SboCallBackBonusReq sboCallBackBonusReq);

    //归还注额
    Object returnStake(SboCallBackReturnStakeReq sboCallBackBonusReq);

    //取得投注状态
    Object getBetStatus(SboCallBackGetBetStatusReq sboCallBackGetBetStatusReq);

    //转帐交易
    Object transfer(SboCallBackTransferReq sboCallBackTransferReq);

    //转帐交易回滚
    Object rollbackTransfer(SboCallBackRollbackTransferReq sboCallBackRollbackTransferReq);

    //取得转帐交易状态
    Object getTransferStatus(SboCallBackGetTransferStautsReq sboCallBackGetTransferStautsReq);

    //LiveCoin購買
    Object liveCoinTransaction(SboCallBackLiveCoinTransactionReq sboCallBackLiveCoinTransactionReq);
}
