package com.indo.game.service.km;

import com.alibaba.fastjson.JSONObject;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;

public interface KmCallbackService {


    Object kmBalanceCallback(JSONObject jsonObject, String ip);

    Object kmDebitCallback(JSONObject jsonObject, String ip);

    Object kmCreditCallback(JSONObject jsonObject, String ip);
}
