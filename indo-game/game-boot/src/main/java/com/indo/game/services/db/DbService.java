package com.indo.game.services.db;


import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;

public interface DbService {

    /**
     * 进入游戏
     */
    Result<String> dbGame(LoginInfo loginUser, Integer gameCode, Integer gameType);

    /**
     * 初始化信息
     */
    void initAccountInfo(LoginInfo loginUser, Integer gameCode);

    /**
     * 下分
     */
    Result<String> dbExit(LoginInfo loginUser, String ip);

    /**
     * 拉取注单
     */
    void pullDbBetOrder();
}
