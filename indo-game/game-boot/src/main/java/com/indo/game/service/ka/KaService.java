package com.indo.game.service.ka;

import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;

public interface KaService {
    /**
     * 登录游戏KA
     */
    Result kaGame(LoginInfo loginUser, String isMobileLogin, String ip, String platform, String parentName);

    /**
     * KA游戏 强迫登出玩家 (第三方不提供此服务)
     */
    Result logout(LoginInfo loginUser, String platform, String ip);
}
