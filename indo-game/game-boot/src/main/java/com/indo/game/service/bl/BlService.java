package com.indo.game.service.bl;

import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;

public interface BlService {

    /**
     * 登录游戏BOLE
     */
    public Result blGame(LoginInfo loginUser, String isMobileLogin, String ip, String platform, String parentName);

    /**
     * BOLE 强迫登出玩家
     */
    public Result logout(LoginInfo loginUser, String platform, String ip);

}
