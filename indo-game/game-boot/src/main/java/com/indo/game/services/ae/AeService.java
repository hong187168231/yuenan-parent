package com.indo.game.services.ae;

import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.game.pojo.entity.ae.AeApiResponse;

import java.util.Map;

public interface AeService{

    /**
     * 登录游戏
     */
    Result<String> aeGame(LoginInfo loginUser, Integer aeCodeId, String ip);

    /**
     * 下分
     */
    Result<String> aeSignOut(LoginInfo loginUser, String ip);


    void initAccountInfo(LoginInfo loginUser, Integer aeCodeId, String ip);

    /**
     * 同步注单
     */
    void aePullOrder();

    AeApiResponse commonRequest(Map<String, String> paramsMap, String aeAccount, String url, Integer userId, String ip, String type) throws Exception;

}
