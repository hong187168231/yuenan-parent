package com.indo.game.service.t9;

import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;

public interface T9Service {

    /**
     * 登录游戏T9电子
     */
    Result t9Game(LoginInfo loginUser, String isMobileLogin, String ip, String platform, String parentName,String countryCode);

    /**
     * T9电子游戏 强迫登出玩家
     */
    Result logout(String account,String platform, String ip,String countryCode);
}
