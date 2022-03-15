package com.indo.game.service.ae;


import com.indo.game.pojo.dto.ae.AeCallBackParentReq;
import com.indo.game.pojo.dto.ae.AeCallBackTransferReq;

public interface AeCallbackService {


    Object aeBalanceCallback(AeCallBackParentReq aeApiRequestData, String ip);

    Object aeTransfer(AeCallBackTransferReq aeApiRequestData, String ip);

    Object query(AeCallBackTransferReq aeApiRequestData, String ip);
}
