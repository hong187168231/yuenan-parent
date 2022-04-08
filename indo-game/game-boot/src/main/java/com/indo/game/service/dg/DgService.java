package com.indo.game.service.dg;

import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;

public interface DgService {

    /**
     * 登录游戏DG
     */
    public Result dgGame(LoginInfo loginUser, String isMobileLogin, String ip, String platform, String parentName);

    /**
     * DG 强迫登出玩家
     */
    public Result logout(LoginInfo loginUser, String platform, String ip);

}
