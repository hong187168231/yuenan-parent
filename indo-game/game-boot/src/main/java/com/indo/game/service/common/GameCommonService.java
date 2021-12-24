package com.indo.game.service.common;

import com.indo.common.enums.GoldchangeEnum;
import com.indo.common.enums.TradingEnum;
import com.indo.game.pojo.entity.manage.GamePlatform;
import com.indo.user.pojo.dto.MemGoldChangeDTO;
import com.indo.user.pojo.entity.MemBaseinfo;

import java.math.BigDecimal;

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


    /**
     * 查询用户信息
     *
     * @param accountNo
     * @return
     */
    MemBaseinfo getByAccountNo(String accountNo);

    /**
     * 修改用户余额
     * @param memBaseinfo
     * @param changeAmount
     * @param goldchangeEnum
     * @param tradingEnum
     * @return
     */
    void updateUserBalance(MemBaseinfo memBaseinfo, BigDecimal changeAmount, GoldchangeEnum goldchangeEnum, TradingEnum tradingEnum);


}
