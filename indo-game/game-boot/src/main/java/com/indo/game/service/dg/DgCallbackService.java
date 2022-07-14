package com.indo.game.service.dg;


import com.indo.game.pojo.dto.dg.DgCallBackReq;
import com.indo.game.pojo.dto.dg.DgMemberCallBackReq;


public interface DgCallbackService {

    Object dgBalanceCallback(String agentName, DgCallBackReq<DgMemberCallBackReq> dgCallBackReq, String ip);

    Object dgTransferCallback(DgCallBackReq<DgMemberCallBackReq> dgCallBackReq, String ip, String agentName);

    Object dgCheckTransferCallback(DgCallBackReq<DgMemberCallBackReq> dgCallBackReq, String ip, String agentName);

    Object djInformCallback(DgCallBackReq<DgMemberCallBackReq> dgCallBackReq, String ip, String agentName);

    Object mgOrderCallback(DgCallBackReq<DgMemberCallBackReq> dgCallBackReq, String ip, String agentName);

    Object mgUnsettleCallback(DgCallBackReq<DgMemberCallBackReq> dgCallBackReq, String ip, String agentName);
}
