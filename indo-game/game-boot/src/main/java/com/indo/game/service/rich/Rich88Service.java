package com.indo.game.service.rich;

import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;

public interface Rich88Service {

    /**
     * 登录游戏rich88
     */
    Result rich88Game(LoginInfo loginUser, String isMobileLogin, String ip, String platform, String parentName,String countryCode);

    /**
     * rich88游戏 强迫登出玩家
     */
    Result logout(String account,String platform, String ip,String countryCode);
}
