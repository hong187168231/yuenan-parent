package com.indo.game.service.fc;

import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;

public interface FCService {

    /**
     * 登录游戏FC
     */
    Result fcGame(LoginInfo loginUser, String isMobileLogin, String ip, String platform, String parentName);

    /**
     * FC游戏 强迫登出玩家 (第三方不提供此服务)
     */
    Result logout(LoginInfo loginUser, String platform, String ip);
}
