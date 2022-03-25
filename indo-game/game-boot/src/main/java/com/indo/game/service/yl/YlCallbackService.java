package com.indo.game.service.yl;


import com.indo.game.pojo.dto.yl.YlCallBackReq;

public interface YlCallbackService {

    Object ylGetBalanceCallback(YlCallBackReq ylCallBackReq, String ip);

    Object psBetCallback(YlCallBackReq ylCallBackReq, String ip);

    Object ylVoidFishBetCallback(YlCallBackReq ylCallBackReq, String ip);
}
