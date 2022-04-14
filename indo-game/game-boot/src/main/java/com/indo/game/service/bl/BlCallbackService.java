package com.indo.game.service.bl;


import com.indo.game.pojo.dto.bl.BlCallBackReq;

public interface BlCallbackService {

    Object blBlanceCallback(BlCallBackReq blCallBackReq, String ip);

    Object blPlayerCallback(BlCallBackReq blCallBackReq, String ip);
}
