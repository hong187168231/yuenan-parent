package com.indo.game.service.pg;


import com.indo.game.pojo.dto.pg.PgVerifyCallBackReq;

public interface PgCallbackService {


    Object pgVerifyCallback(PgVerifyCallBackReq pgVerifyCallBackReq, String ip);

    Object pgBalanceCallback(PgVerifyCallBackReq pgVerifyCallBackReq, String ip);

    Object pgTransferInCallback(PgVerifyCallBackReq pgVerifyCallBackReq, String ip);

    Object pgAdjustmentCallback(PgVerifyCallBackReq pgVerifyCallBackReq, String ip);
}
