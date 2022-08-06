package com.indo.game.service.ob;


import com.indo.game.controller.ob.ObCallBackTransferstatusReq;
import com.indo.game.pojo.dto.ob.ObCallBackParentReq;

public interface ObCallbackService {


    Object obBalanceCallback(ObCallBackParentReq obCallBackParentReq, String ip);

    Object obTransfer(ObCallBackParentReq obCallBackParentReq, String ip);

    Object transferStatus(ObCallBackTransferstatusReq obCallBackTransferstatusReq, String ip);
}
