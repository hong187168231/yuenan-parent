package com.indo.game.service.pg;


import com.indo.game.pojo.dto.pg.PgAdjustmentOutCallBackReq;
import com.indo.game.pojo.dto.pg.PgGetBalanceCallBackReq;
import com.indo.game.pojo.dto.pg.PgTransferInOutCallBackReq;
import com.indo.game.pojo.dto.pg.PgVerifySessionCallBackReq;

public interface PgCallbackService {


    Object pgVerifyCallback(PgVerifySessionCallBackReq pgVerifySessionCallBackReq, String ip);

    Object pgBalanceCallback(PgGetBalanceCallBackReq pgGetBalanceCallBackReq, String ip);

    Object pgTransferInCallback(PgTransferInOutCallBackReq pgTransferInOutCallBackReq, String ip);

    Object pgAdjustmentCallback(PgAdjustmentOutCallBackReq pgAdjustmentOutCallBackReq, String ip);
}
