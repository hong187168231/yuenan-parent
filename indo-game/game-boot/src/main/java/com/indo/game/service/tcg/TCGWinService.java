package com.indo.game.service.tcg;

import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;

public interface TCGWinService {

    /**
     * 登录游戏sa
     */
    Result tcgwinGame(LoginInfo loginUser, String isMobileLogin, String ip, String platform, String parentName,String countryCode);

    /**
     * sa游戏 强迫登出玩家
     */
    Result logout(LoginInfo loginUser, String platform, String ip,String countryCode);
}
