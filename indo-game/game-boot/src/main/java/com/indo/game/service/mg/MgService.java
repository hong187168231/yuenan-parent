package com.indo.game.service.mg;

import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;

public interface MgService {

    /**
     * 登录游戏Mg电子
     */
    public Result mgGame(LoginInfo loginUser, String isMobileLogin, String ip, String platform, String parentName);

    /**
     * mg电子游戏 强迫登出玩家
     */
    public Result logout(LoginInfo loginUser, String platform, String ip);

}
