package com.indo.game.service.common;

import com.indo.game.pojo.entity.manage.GamePlatform;
import com.indo.user.pojo.entity.MemBaseinfo;

public interface GameCommonService {


    /**
     * 判断游戏（彩种）是否开启
     *
     * @param platformCode
     * @return
     */
    GamePlatform getGamePlatformByplatformCode(String platformCode);

    /**
     * 查询用户信息
     *
     * @param userId
     * @return
     */
    MemBaseinfo getMemBaseInfo(String userId);


}
