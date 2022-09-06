package com.indo.game.service.mt;

import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;

import java.math.BigDecimal;

public interface MtService {

    /**
     * 登录游戏MT
     */
    Result mtGame(LoginInfo loginUser, String isMobileLogin, String ip, String platform, String parentName,String countryCode);

    /**
     * MT电子游戏 强迫登出玩家
     */
    Result logout(LoginInfo loginUser,String platform, String ip);

    /**
     * 从MT中全部提出游戏币
     * @param loginUser
     * @param platform
     * @param ip
     * @return
     */
    Result allWithdraw(LoginInfo loginUser,String platform, String ip);

    /**
     * 查询玩家MT游戏中账户余额
     * @param loginUser
     * @param platform
     * @param ip
     * @return
     */
    Result getPlayerBalance(LoginInfo loginUser,String platform, String ip);

    /**
     * 充值到MT游戏
     * @param loginUser
     * @param platform
     * @param ip
     * @return
     */
    Result deposit(LoginInfo loginUser, String platform, String ip, BigDecimal coins);

    /**
     * 从MT游戏提取
     * @param loginUser
     * @param platform
     * @param ip
     * @return
     */
    Result withdraw(LoginInfo loginUser,String platform, String ip, BigDecimal coins);

}
