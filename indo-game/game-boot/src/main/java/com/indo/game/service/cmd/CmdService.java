package com.indo.game.service.cmd;

import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;

public interface CmdService {
    /**
     * 登录游戏cmd
     */
    Result cmdGame(LoginInfo loginUser, String isMobileLogin, String ip, String platform, String parentName,String countryCode);

    /**
     * cmd游戏 强迫登出玩家
     */
    Result logout(LoginInfo loginUser,String platform, String ip);
}
