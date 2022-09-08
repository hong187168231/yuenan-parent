package com.indo.game.service.tp;

import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;

public interface TpService {

    /**
     * 登录游戏AWC-AE真人、SV388斗鸡
     */
    public Result tpGame(LoginInfo loginUser, String isMobileLogin,String ip,String platform,String parentName,String countryCode);

    /**
     * AE真人、SV388斗鸡游戏 强迫登出玩家
     */
    public Result logout(LoginInfo loginUser,String ip,String countryCode);

}
