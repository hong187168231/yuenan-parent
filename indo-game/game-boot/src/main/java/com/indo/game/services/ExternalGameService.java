package com.indo.game.services;

import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;

import java.math.BigDecimal;

public interface ExternalGameService {

    /**
     * 第三方游戏下分公共接口
     */
    Result<BigDecimal> exit(LoginInfo loginUser, String ip);

}
