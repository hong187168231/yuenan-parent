package com.indo.game.service.sgwin;

import com.indo.game.pojo.vo.callback.sgwin.*;

public interface SGWinCallbackService {

    Object getVerifyApi(SGWinVerifyApiCallBackReq sgWinVerifyApiCallBackReq, String ip);

    Object getUserBalance(SGWinGetBalanceCallBackReq sgWinGetBalanceCallBackParentReq, String ip);

    Object sgwinBetCallback(SGWinBetsCallBackReq sgWinBetsCallBackParentReq, String ip);

    public Object notifySettle(SGWinNotifySettleCallBackReq sgWinNotifySettleCallBackReq, String ip);

    public Object refund(SGWinRefundCallBackReq<SGWinTransactionsCallBackReq> sgWinRefundCallBackReq, String ip);
}
