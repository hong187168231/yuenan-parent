package com.indo.game.service.awc;

import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.game.pojo.entity.awc.AwcApiResponseData;

import java.util.Map;

public interface AwcService {

    /**
     * 登录游戏AWC-AE真人
     */
    public Result<String> awcGame(LoginInfo loginUser, String isMobileLogin,String gameCode, String ip,String platform) ;

    /**
     * 同步注单
     */
    void awcPullOrder(String platform);

    AwcApiResponseData commonRequest(Map<String, String> paramsMap,String url, Integer userId, String ip, String type) throws Exception;

}
