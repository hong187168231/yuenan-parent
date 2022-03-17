package com.indo.game.service.ps;

import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;

public interface PsService {

    /**
     * 登录游戏JK电子
     */
    public Result psGame(LoginInfo loginUser, String isMobileLogin, String ip, String platform, String parentName);

    /**
     * JK电子游戏 强迫登出玩家
     */
    public Result logout(LoginInfo loginUser, String platform, String ip);

}
