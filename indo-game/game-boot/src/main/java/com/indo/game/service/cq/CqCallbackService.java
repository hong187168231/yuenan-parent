package com.indo.game.service.cq;


import com.indo.game.pojo.dto.cq.CqBetCallBackReq;

public interface CqCallbackService {


    Object cqBetCallback(CqBetCallBackReq cqApiRequestData, String ip, String wtoken);

    Object cqPayOffCallback(CqBetCallBackReq cqApiRequestData, String ip, String wtoken);

    Object cqBonusCallback(CqBetCallBackReq cqApiRequestData, String ip, String wtoken);

    Object cqCreditCallback(CqBetCallBackReq cqApiRequestData, String ip, String wtoken);

    Object cqDebitCallback(CqBetCallBackReq cqApiRequestData, String ip, String wtoken);

    Object cqRollinCallback(CqBetCallBackReq cqApiRequestData, String ip, String wtoken);

    Object cqBalanceCallback(String account, String ip, String wtoken);

    Object cqCheckPlayerCallback(String account, String ip, String wtoken);
}
