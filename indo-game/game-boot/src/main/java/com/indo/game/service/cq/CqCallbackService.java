package com.indo.game.service.cq;


import com.indo.game.pojo.dto.cq.*;

public interface CqCallbackService {


    Object cqBetCallback(CqBetCallBackReq cqApiRequestData, String ip, String wtoken);

    public Object endround(CqEndroundCallBackReq endroundDataCallBackReq, String ip, String wtoken);

    public Object cqRolloutCallback(CqBetCallBackReq cqApiRequestData, String ip, String wtoken);

    public Object cqTakeallCallback(CqBetCallBackReq cqApiRequestData, String ip, String wtoken);

    Object cqPayOffCallback(CqBetCallBackReq cqApiRequestData, String ip, String wtoken);

    Object cqBonusCallback(CqBetCallBackReq cqApiRequestData, String ip, String wtoken);

    Object cqCreditCallback(CqBetCallBackReq cqApiRequestData, String ip, String wtoken);

    Object cqDebitCallback(CqBetCallBackReq cqApiRequestData, String ip, String wtoken);

    Object cqRollinCallback(CqBetCallBackReq cqApiRequestData, String ip, String wtoken);

    public Object cqRefundCallback(CqBetCallBackReq cqApiRequestData, String ip, String wtoken);

    Object cqBalanceCallback(String account, String ip, String wtoken);

    Object cqCheckPlayerCallback(String account, String ip, String wtoken);

    Object cqRecordCallback(String mtcode, String ip, String wtoken);

    public Object cqBetsCallback(CqSportsCallBackReq<CqSportsInfoCallBackReq> cqSportsCallBackReq, String ip, String wtoken);

    public Object cqRefundsCallback(CqSportsRefudsCallBackReq cqSportsRefudsCallBackReq, String ip, String wtoken);

    public Object cqCancelCallback(CqSportsRefudsCallBackReq cqSportsRefudsCallBackReq, String ip, String wtoken);

    public Object cqWinsCallback(CqSportsWinsCallBackReq<CqSportsEventCallBackReq> callBackReqCqSportsWinsCallBackReq, String ip, String wtoken);

    public Object cqAmendCallback(CqSportsAmendCallBackReq<CqSportsAmendDataCallBackReq> cqSportsAmendCallBackReq, String ip, String wtoken);

    public Object cqAmendsCallback(CqSportsAmendCallBackReq<CqSportsAmendDataCallBackReq> cqSportsAmendCallBackReq, String ip, String wtoken);
}
