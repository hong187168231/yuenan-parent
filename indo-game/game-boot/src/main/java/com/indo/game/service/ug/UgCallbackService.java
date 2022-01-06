package com.indo.game.service.ug;

import com.indo.game.pojo.dto.ug.UgCallBackCancelReq;
import com.indo.game.pojo.dto.ug.UgCallBackGetBalanceReq;
import com.indo.game.pojo.dto.ug.UgCallBackTransactionItemReq;
import com.indo.game.pojo.dto.ug.UgCallBackTransferReq;

public interface UgCallbackService {

    //取得用户的余额
    public Object getBalance(UgCallBackGetBalanceReq ugCallBackGetBalanceReq);

    //加余额/扣除余额
    public Object transfer(UgCallBackTransferReq<UgCallBackTransactionItemReq> ugCallBackTransactionItemReqUgCallBackTransferReq);

    // 取消交易
    public Object cancel(UgCallBackCancelReq ugCallBackCancelReq);

    //检查交易结果
    public Object check(UgCallBackCancelReq ugCallBackCancelReq);

}
