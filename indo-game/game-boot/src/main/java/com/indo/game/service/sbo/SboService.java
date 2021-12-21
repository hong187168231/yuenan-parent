package com.indo.game.service.sbo;

import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.game.pojo.vo.callback.sbo.SboApiResponseData;

import java.util.Map;

public interface SboService {


    /**
     * SBO Sports
     */
    public Result sboGame(LoginInfo loginUser, String ip,String platform) ;


    SboApiResponseData commonRequest(Map<String, String> paramsMap, String url, Integer userId, String ip, String type) throws Exception;

}
