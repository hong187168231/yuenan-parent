package com.indo.game.services.gamecommon;

import com.indo.game.pojo.entity.manage.GamePlatform;

public interface GameCommonService {


    /**
     * 判断游戏（彩种）是否开启
     *
     * @param platformCode
     * @return
     */
    GamePlatform getGamePlatformByplatformCode(String platformCode);

}
