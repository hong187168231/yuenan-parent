package com.indo.game.service.awc;

import com.indo.game.pojo.dto.awc.AwcApiRequestParentData;

public interface AwcCallbackService {
    Object awcCallback(AwcApiRequestParentData awcApiRequestData, String ip);
}
