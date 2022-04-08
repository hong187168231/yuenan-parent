package com.indo.game.service.dg;


import com.indo.game.pojo.dto.dg.DgCallBackReq;

public interface DgCallbackService {

    Object dgBalanceCallback(String agentName, DgCallBackReq dgCallBackReq, String ip);

    Object dgTransferCallback(DgCallBackReq dgCallBackReq, String ip, String agentName);

    Object dgCheckTransferCallback(DgCallBackReq dgCallBackReq, String ip, String agentName);

    Object djInformCallback(DgCallBackReq dgCallBackReq, String ip, String agentName);

    Object mgOrderCallback(DgCallBackReq dgCallBackReq, String ip, String agentName);

    Object mgUnsettleCallback(DgCallBackReq dgCallBackReq, String ip, String agentName);
}
