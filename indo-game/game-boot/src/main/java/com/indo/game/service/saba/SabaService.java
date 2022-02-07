package com.indo.game.service.saba;

import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.game.pojo.vo.callback.saba.SabaApiResponseData;

import java.util.Map;

public interface SabaService {


    /**
     * saba Sports
     */
    Result sabaGame(LoginInfo loginUser, String ip, String platform);

    /**
     * saba Sports登出玩家
     */
    Result logout(LoginInfo loginUser, String ip);

    SabaApiResponseData commonRequest(Map<String, String> paramsMap, String url, Integer userId, String ip, String type) throws Exception;

}
