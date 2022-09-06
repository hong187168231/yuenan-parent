package com.indo.game.service.yl;

import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;

public interface YlService {

    /**
     * 登录游戏JYL 捕鱼
     */
    public Result ylGame(LoginInfo loginUser, String isMobileLogin, String ip, String platform, String parentName,String countryCode);

    /**
     * YL 捕鱼游戏 强迫登出玩家
     */
    public Result logout(LoginInfo loginUser, String platform, String ip);

}
