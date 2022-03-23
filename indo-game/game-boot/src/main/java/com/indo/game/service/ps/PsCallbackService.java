package com.indo.game.service.ps;


import com.indo.game.pojo.dto.ps.PsCallBackParentReq;
import com.indo.game.pojo.vo.callback.ps.PsCallBackResponse;

public interface PsCallbackService {


    PsCallBackResponse psVerifyCallback(PsCallBackParentReq psCallBackParentReq, String ip);

    Object psBetCallback(PsCallBackParentReq psbetCallBackReq, String ip);

    Object psResultCallback(PsCallBackParentReq psbetCallBackReq, String ip);

    Object psRefundtCallback(PsCallBackParentReq psbetCallBackReq, String ip);

    Object psBonusCallback(PsCallBackParentReq psbetCallBackReq, String ip);

    Object psGetBalanceCallback(PsCallBackParentReq psbetCallBackReq, String ip);
}
