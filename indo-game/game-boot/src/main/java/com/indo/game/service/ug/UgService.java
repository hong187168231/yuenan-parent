package com.indo.game.service.ug;

import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;


public interface UgService {


    /**
     * UG Sports
     */
    public Result ugGame(LoginInfo loginUser, String ip,String platform,String WebType) ;

    /**
     * UG Sports登出玩家
     */
    public Result logout(LoginInfo loginUser,String ip);


    /**
     * 同步注单
     */
    public void ugPullOrder();

    /**
     * 同步单一注单
     */
    public void ugPullOrderByBetID();

}
