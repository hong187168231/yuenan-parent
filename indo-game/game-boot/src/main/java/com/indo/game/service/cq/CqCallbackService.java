package com.indo.game.service.cq;


import com.indo.game.pojo.dto.cq.CqBetCallBackReq;
import com.indo.game.pojo.dto.cq.CqEndroundCallBackReq;
import com.indo.game.pojo.dto.cq.CqEndroundDataCallBackReq;

public interface CqCallbackService {


    Object cqBetCallback(CqBetCallBackReq cqApiRequestData, String ip, String wtoken);

    public Object endround(CqEndroundCallBackReq endroundDataCallBackReq, String ip, String wtoken);

    Object cqPayOffCallback(CqBetCallBackReq cqApiRequestData, String ip, String wtoken);

    Object cqBonusCallback(CqBetCallBackReq cqApiRequestData, String ip, String wtoken);

    Object cqCreditCallback(CqBetCallBackReq cqApiRequestData, String ip, String wtoken);

    Object cqDebitCallback(CqBetCallBackReq cqApiRequestData, String ip, String wtoken);

    Object cqRollinCallback(CqBetCallBackReq cqApiRequestData, String ip, String wtoken);

    Object cqBalanceCallback(String account, String ip, String wtoken);

    Object cqCheckPlayerCallback(String account, String ip, String wtoken);

    Object cqRecordCallback(String mtcode, String ip, String wtoken);
}
