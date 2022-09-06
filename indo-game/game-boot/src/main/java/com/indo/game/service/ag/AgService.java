package com.indo.game.service.ag;

import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;

public interface AgService {

    /**
     * 登录游戏AG
     */
    public Result agGame(LoginInfo loginUser, String isMobileLogin,String ip,String platform,String parentName,String countryCode) ;

    /**
     * AG
     */
    public Result logout(LoginInfo loginUser,String ip);


}
