package com.indo.game.service.jdb;

import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;

import java.util.Map;

public interface JdbService {

    /**
     * 登录游戏AWC-AE真人、SV388斗鸡
     */
    public Result jdbGame(LoginInfo loginUser, String isMobileLogin,String ip,String platform,String parentName,String countryCode);

    /**
     * AE真人、SV388斗鸡游戏 强迫登出玩家
     */
    public Result logout(String account,String platform, String ip,String countryCode);

}
