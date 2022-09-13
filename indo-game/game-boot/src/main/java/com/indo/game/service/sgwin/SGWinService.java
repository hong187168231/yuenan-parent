package com.indo.game.service.sgwin;

import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;

public interface SGWinService {

    /**
     * 登录游戏sa
     */
    Result sgwinGame(LoginInfo loginUser, String isMobileLogin, String ip, String platform, String parentName,String countryCode);

    /**
     * sa游戏 强迫登出玩家
     */
    Result logout(String account,String platform, String ip,String countryCode);
}
