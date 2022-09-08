package com.indo.game.service.redtiger;

import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;

public interface RedtigerService {
    /**
     * 登录游戏redtiger
     */
    Result redtigerGame(LoginInfo loginUser, String isMobileLogin, String ip, String platform, String parentName,String countryCode);

    /**
     * redtiger 强迫登出玩家
     */
    Result logout(LoginInfo loginUser,String platform, String ip,String countryCode);
}
