package com.indo.game.service.awc;

import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.game.pojo.dto.awc.AwcApiResponseData;

import java.util.Map;

public interface AwcService {

    /**
     * 登录游戏AWC-AE真人、SV388斗鸡
     */
    public Result awcGame(LoginInfo loginUser, String isMobileLogin,String ip,String platform,String parentName,String countryCode) ;

    /**
     * AE真人、SV388斗鸡游戏 强迫登出玩家
     */
    public Result logout(LoginInfo loginUser,String ip,String countryCode);

    /**
     * 同步注单
     */
    void awcPullOrder(String platform);

    AwcApiResponseData commonRequest(Map<String, String> paramsMap,String url, Integer userId, String ip, String type) throws Exception;

}
