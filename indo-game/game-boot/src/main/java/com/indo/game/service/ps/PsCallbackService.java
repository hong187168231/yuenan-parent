package com.indo.game.service.ps;


import com.alibaba.fastjson.JSONObject;
import com.indo.game.pojo.dto.ps.PsCallBackParentReq;

public interface PsCallbackService {


    JSONObject psVerifyCallback(PsCallBackParentReq psCallBackParentReq, String ip);

    Object psBetCallback(PsCallBackParentReq psbetCallBackReq, String ip);

    Object psResultCallback(PsCallBackParentReq psbetCallBackReq, String ip);

    Object psRefundtCallback(PsCallBackParentReq psbetCallBackReq, String ip);

    Object psBonusCallback(PsCallBackParentReq psbetCallBackReq, String ip);

    Object psGetBalanceCallback(PsCallBackParentReq psbetCallBackReq, String ip);
}
