package com.indo.game.service.ug;

import com.indo.game.pojo.dto.ug.*;

public interface UgCallbackService {

    //玩家登入验证URL
    public Object checkLogin(UgCallBackCheckLoginReq ugCallBackCheckLoginReq);

    //取得用户的余额
    public Object getBalance(UgCallBackGetBalanceReq ugCallBackGetBalanceReq);

    //加余额/扣除余额
    public Object transfer(UgCallBackTransferReq<UgCallBackTransactionItemReq> ugCallBackTransactionItemReqUgCallBackTransferReq);

    // 取消交易
    public Object cancel(UgCallBackCancelReq<UgCallBackCancelItemReq> ugCallBackCancelReq);

    //检查交易结果
    public Object check(UgCallBackCheckTxnReq<UgCallBackCheckTxnItemReq> ugCallBackCheckTxnReq);

}
