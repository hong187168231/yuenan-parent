package com.indo.game.service.yl;


import com.alibaba.fastjson.JSONObject;
import com.indo.game.pojo.dto.yl.YlCallBackReq;

public interface YlCallbackService {

    Object ylGetBalanceCallback(JSONObject jsonObject);

    Object psBetCallback(JSONObject jsonObject);

    Object ylVoidFishBetCallback(JSONObject jsonObject);
}
