package com.indo.game.service.common;

import com.indo.common.enums.GoldchangeEnum;
import com.indo.common.enums.TradingEnum;
import com.indo.core.pojo.bo.MemTradingBO;
import com.indo.core.pojo.entity.game.GameCategory;
import com.indo.core.pojo.entity.game.GameParentPlatform;
import com.indo.core.pojo.entity.game.GamePlatform;

import java.math.BigDecimal;
import java.util.List;

public interface GameCommonService {

    /**
     * 查询所有游戏平台
     * @return
     */
    public List<GameParentPlatform> queryAllGameParentPlatform();

    /**
     * 依据平台代码查询平台
     * @param platformCode
     * @return
     */
    public GameParentPlatform getGameParentPlatformByplatformCode(String platformCode);
    /**
     * 查询所有游戏
     *
     * @return
     */
    List<GamePlatform> queryAllGamePlatform();


    /**
     * 依据平台代码查询游戏
     *
     * @param platformCode
     * @return
     */
    GamePlatform getGamePlatformByplatformCode(String platformCode);

    /**
     * 依据平台代码和游戏代码查询游戏
     * @param platformCode
     * @param parentName
     * @return
     */
    public GamePlatform getGamePlatformByplatformCodeAndParentName(String platformCode,String parentName);

    /**
     * 依据总平台查询平台
     *
     * @param parentName
     * @return
     */
    List<GamePlatform> getGamePlatformByParentName(String parentName);

    /**
     * 查询用交易信息
     *
     * @param account 用户名
     * @return 用户信息
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
    GameCategory getGameCategoryById(Long id);


}
