package com.indo.game.service.common.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.indo.common.constant.RedisKeys;
import com.indo.common.enums.GoldchangeEnum;
import com.indo.common.enums.TradingEnum;
import com.indo.common.result.Result;
import com.indo.common.web.exception.BizException;
import com.indo.core.pojo.dto.MemGoldChangeDTO;
import com.indo.core.service.IMemGoldChangeService;
import com.indo.game.common.util.GameBusinessRedisUtils;
import com.indo.game.mapper.frontend.GameCategoryMapper;
import com.indo.game.mapper.frontend.GamePlatformMapper;
import com.indo.game.pojo.entity.manage.GameCategory;
import com.indo.game.pojo.entity.manage.GamePlatform;
import com.indo.game.service.common.GameCommonService;
import com.indo.user.api.MemBaseInfoFeignClient;
import com.indo.user.pojo.bo.MemTradingBO;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service(value = "gameCommonService")
public class GameCommonServiceImpl implements GameCommonService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private GamePlatformMapper gamePlatformMapper;

    @Autowired
    private GameCategoryMapper gameCategoryMapper;

    @Resource
    private MemBaseInfoFeignClient memBaseInfoFeignClient;
    @Resource
    private IMemGoldChangeService iMemGoldChangeService;

    @Override
    public GamePlatform getGamePlatformByplatformCode(String platformCode) {
        GamePlatform gamePlatform = GameBusinessRedisUtils.get(RedisKeys.GAME_PLATFORM_KEY + platformCode);
        if (null == gamePlatform) {
            LambdaQueryWrapper<GamePlatform> wrapper = new LambdaQueryWrapper<GamePlatform>();
            wrapper.eq(GamePlatform::getPlatformCode, platformCode);
            gamePlatform = gamePlatformMapper.selectOne(wrapper);
        }
        return gamePlatform;
    }

    @Override
    public List<GamePlatform> getGamePlatformByParentName(String parentName) {
        List<GamePlatform> gamePlatform = GameBusinessRedisUtils.get(RedisKeys.GAME_PLATFORM_PARENT_KEY + parentName);
        if (null == gamePlatform) {
            LambdaQueryWrapper<GamePlatform> wrapper = new LambdaQueryWrapper<GamePlatform>();
            wrapper.eq(GamePlatform::getParentName, parentName);
            wrapper.orderByAsc(GamePlatform::getId);
            gamePlatform = gamePlatformMapper.selectList(wrapper);
        }
        return gamePlatform;
    }

    @Override
    public MemTradingBO getMemTradingInfo(String account) {
        Result<MemTradingBO> result = memBaseInfoFeignClient.getMemTradingInfo(account);
        if (Result.success().getCode().equals(result.getCode())) {
            MemTradingBO  memBaseinfo = result.getData();
            return memBaseinfo;
        } else {
            throw new BizException("No client with requested id: " + account);
        }
    }

    @Override
    public GameCategory getGameCategoryById(Long id) {
        GameCategory gameCategory = GameBusinessRedisUtils.get(RedisKeys.GAME_PLATFORM_KEY + id);
        if (null == gameCategory) {
            gameCategory = gameCategoryMapper.selectById(id);
        }
        return gameCategory;
    }




    @Override
    public void updateUserBalance(MemTradingBO memTradingBO, BigDecimal changeAmount, GoldchangeEnum goldchangeEnum, TradingEnum tradingEnum) {
        logger.info("修改用户余额:memTradingBO{},changeAmount{},goldchangeEnum{},tradingEnum{}"+memTradingBO,changeAmount,goldchangeEnum,tradingEnum);
        MemGoldChangeDTO goldChangeDO = new MemGoldChangeDTO();
        goldChangeDO.setChangeAmount(changeAmount);
        goldChangeDO.setTradingEnum(tradingEnum);
        goldChangeDO.setGoldchangeEnum(goldchangeEnum);
        goldChangeDO.setUserId(memTradingBO.getId());
        goldChangeDO.setUpdateUser(memTradingBO.getAccount());
//      goldChangeDO.setRefId(rechargeOrder.getRechargeOrderId());
        boolean flag = iMemGoldChangeService.updateMemGoldChange(goldChangeDO);
        if (!flag) {
            throw new BizException("修改余额失败");
        }
    }


}
