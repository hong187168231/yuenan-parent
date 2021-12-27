package com.indo.game.service.ug;

import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.game.pojo.vo.callback.ug.UgApiResponseData;

import java.util.Map;

public interface UgService {


    /**
     * UG Sports
     */
    public Result ugGame(LoginInfo loginUser, String ip,String platform) ;

    /**
     * UG Sports登出玩家
     */
    public Result logout(LoginInfo loginUser,String ip);


    UgApiResponseData commonRequest(Map<String, String> paramsMap, String url, Integer userId, String ip, String type) throws Exception;

}
