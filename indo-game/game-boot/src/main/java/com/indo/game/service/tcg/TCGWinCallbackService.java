package com.indo.game.service.tcg;

import com.indo.game.pojo.vo.callback.sgwin.*;
import com.indo.game.pojo.vo.callback.tcgwin.TCGWinDebitCallBackReq;
import com.indo.game.pojo.vo.callback.tcgwin.TCGWinGetBalanceCallBackReq;
import com.indo.game.pojo.vo.callback.tcgwin.TransactionData;

public interface TCGWinCallbackService {


    Object getUserBalance(TCGWinGetBalanceCallBackReq tcgWinGetBalanceCallBackReq, String ip);

    Object debit(TCGWinDebitCallBackReq<TransactionData> tcgWinDebitCallBackReq, String ip);

    public Object credit(TCGWinDebitCallBackReq<TransactionData> tcgWinDebitCallBackReq, String ip);

}
