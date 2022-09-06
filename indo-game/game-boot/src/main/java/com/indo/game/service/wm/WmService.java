package com.indo.game.service.wm;

import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;

public interface WmService {
    /**
     * 登录游戏wm
     */
    Result wmGame(LoginInfo loginUser, String isMobileLogin, String ip, String platform, String parentName,String countryCode);

    /**
     * wm游戏 强迫登出玩家
     */
    Result logout(LoginInfo loginUser, String platform, String ip);
}
