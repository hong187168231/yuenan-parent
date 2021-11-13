package com.indo.game.services.mg;


import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;

public interface MgService {


    Result<String> mgGame(LoginInfo loginUser, Integer gameCode);

    void initAccountInfo(LoginInfo loginUser, Integer gameCode);

    Result<String> mgExit(LoginInfo loginUser, String ip);

    void pullMgBetOrder();
}
