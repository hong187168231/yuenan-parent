package com.indo.game.service.bti;

import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;

public interface BtiService {

    /**
     * 登录游戏bti
     */
    Result btiGame(LoginInfo loginUser, String isMobileLogin, String ip, String platform, String parentName,String countryCode);

    /**
     * wm游戏 强迫登出玩家
     */
    Result logout(LoginInfo loginUser, String platform, String ip);
}
