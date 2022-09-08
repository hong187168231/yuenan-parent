package com.indo.game.service.ob;

import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;

public interface ObService {

    /**
     * 登录游戏OB
     */
    public Result obGame(LoginInfo loginUser, String isMobileLogin, String ip, String platform, String parentName,String countryCode);

    /**
     * OB游戏 强迫登出玩家
     */
    public Result logout(LoginInfo loginUser, String platform, String ip,String countryCode);

}
