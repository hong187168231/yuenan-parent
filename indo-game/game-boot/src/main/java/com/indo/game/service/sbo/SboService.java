package com.indo.game.service.sbo;

import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;

public interface SboService {


    /**
     * SBO Sports
     */
    public Result sboGame(LoginInfo loginUser, String ip,String platform,String parentName) ;

    /**
     * SBO Sports登出玩家
     */
    public Result logout(LoginInfo loginUser,String ip);


}
