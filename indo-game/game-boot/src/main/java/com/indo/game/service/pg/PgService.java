package com.indo.game.service.pg;

import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;

public interface PgService {

    /**
     * 登录游戏AE电子
     */
    public Result pgGame(LoginInfo loginUser, String isMobileLogin, String ip, String platform, String parentName,String countryCode);

    /**
     * AE电子游戏 强迫登出玩家
     */
    public Result logout(String account,String platform, String ip,String countryCode);

}
