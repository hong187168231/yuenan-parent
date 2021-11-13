package com.indo.game.services.es;

import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;

public interface ESGameService {

    /**
     * ky跳转
     *
     * @param ip IP
     */
    Result<String> go(LoginInfo loginUser, String ip);

    /**
     * 初始化游戏账号信息
     *
     * @param ip
     */
    void initAccountInfo(LoginInfo loginUser, String ip);


    /**
     * 退出--下分
     */
    Result<String> exit(LoginInfo loginUser, String ip);


    /**
     * 电竞结算注单获取
     */
    void pullOrder();
}
