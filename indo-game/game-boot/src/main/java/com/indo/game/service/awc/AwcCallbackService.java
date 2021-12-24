package com.indo.game.service.awc;

import com.indo.game.pojo.entity.awc.AwcApiRequestParentData;

public interface AwcCallbackService {
    public String awcCallback(AwcApiRequestParentData awcApiRequestData,String ip);
}
