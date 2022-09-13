package com.indo.game.service.km;

import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;

public interface KmService {

    /**
     * 登录游戏KingMaker
     */
    public Result kmGame(LoginInfo loginUser, String isMobileLogin, String ip, String platform, String parentName,String countryCode);

    /**
     * KingMaker 强迫登出玩家
     */
    public Result logout(String account,String platform, String ip,String countryCode);

}
