package com.indo.game.service.dj;

import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;

public interface DjService {

    /**
     * 登录游戏DJ
     */
    public Result djGame(LoginInfo loginUser, String isMobileLogin, String ip, String platform, String parentName,String countryCode);

    /**
     * DJ 强迫登出玩家
     */
    public Result logout(String account,String platform, String ip,String countryCode);

}
