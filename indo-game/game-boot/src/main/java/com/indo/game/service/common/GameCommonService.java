package com.indo.game.service.common;

import com.indo.common.enums.GoldchangeEnum;
import com.indo.common.enums.TradingEnum;
import com.indo.game.pojo.entity.manage.GameCategory;
import com.indo.game.pojo.entity.manage.GamePlatform;
import com.indo.user.pojo.bo.MemTradingBO;

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
     * 查询用交易信息
     *
     * @param account
     * @return
     */
    MemTradingBO getMemTradingInfo(String account);

    /**
     * 修改用户余额
     *
     * @param memBaseinfoBo
     * @param changeAmount
     * @param goldchangeEnum
     * @param tradingEnum
     * @return
     */
    void updateUserBalance(MemTradingBO memBaseinfoBo, BigDecimal changeAmount, GoldchangeEnum goldchangeEnum, TradingEnum tradingEnum);

    /**
     * 查询分类信息
     *
     * @param id
     * @return
     */
    public GameCategory getGameCategoryById(Long id);


}
