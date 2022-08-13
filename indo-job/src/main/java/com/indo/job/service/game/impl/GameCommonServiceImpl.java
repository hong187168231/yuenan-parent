package com.indo.job.service.game.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.indo.common.constant.RedisConstants;
import com.indo.common.enums.GoldchangeEnum;
import com.indo.common.enums.TradingEnum;
import com.indo.common.redis.utils.RedisUtils;
import com.indo.common.result.Result;
import com.indo.common.utils.CollectionUtil;
import com.indo.common.web.exception.BizException;
import com.indo.core.mapper.game.GameCategoryMapper;
import com.indo.core.mapper.game.GameParentPlatformMapper;
import com.indo.core.mapper.game.GamePlatformMapper;
import com.indo.core.pojo.bo.MemTradingBO;
import com.indo.core.pojo.dto.MemGoldChangeDTO;
import com.indo.core.pojo.entity.game.GameCategory;
import com.indo.core.pojo.entity.game.GameParentPlatform;
import com.indo.core.pojo.entity.game.GamePlatform;
import com.indo.core.service.IMemGoldChangeService;
import com.indo.core.util.BusinessRedisUtils;
import com.indo.job.service.game.GameCommonService;
import com.indo.user.api.MemBaseInfoFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service(value = "gameCommonService")
public class GameCommonServiceImpl implements GameCommonService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private GamePlatformMapper gamePlatformMapper;
    @Autowired
    private GameParentPlatformMapper gameParentPlatformMapper;
    @Autowired
    private GameCategoryMapper gameCategoryMapper;

    @Resource
    private MemBaseInfoFeignClient memBaseInfoFeignClient;

    @Resource
    private IMemGoldChangeService iMemGoldChangeService;

    @Override
    public List<GameParentPlatform> queryAllGameParentPlatform() {
        List<GameParentPlatform> categoryList = null;
        try{
            Map<Object, Object> map = RedisUtils.hmget(RedisConstants.GAME_PARENT_PLATFORM_KEY);
            categoryList = (List<GameParentPlatform>)map.get(RedisConstants.GAME_PARENT_PLATFORM_KEY);
//            RedisUtils.hdel(RedisConstants.GAME_PARENT_PLATFORM_KEY);
        }catch (Exception e) {
            logger.error("queryAllGameParentPlatform.getcache.error "+e.getMessage(), e);
        }

        if (CollectionUtil.isEmpty(categoryList)) {
            LambdaQueryWrapper<GameParentPlatform> wrapper = new LambdaQueryWrapper<>();
            categoryList = gameParentPlatformMapper.selectList(wrapper);
            Map<String, Object> map = new HashMap<>();
            map.put(RedisConstants.GAME_PARENT_PLATFORM_KEY, categoryList);
            RedisUtils.hmset(RedisConstants.GAME_PARENT_PLATFORM_KEY, map);
        }
        categoryList.sort(Comparator.comparing(GameParentPlatform::getSortNumber));
        return categoryList;
    }

    @Override
    public GameParentPlatform getGameParentPlatformByplatformCode(String platformCode) {
        List<GameParentPlatform> platformList = queryAllGameParentPlatform();
        if (CollectionUtil.isNotEmpty(platformList)) {
            platformList = platformList.stream()
                    .filter(platform -> platform.getPlatformCode().equals(platformCode))
                    .collect(Collectors.toList());
        }
        return CollectionUtil.isEmpty(platformList) ? null : platformList.get(0);
    }

    @Override
    public GamePlatform getGamePlatformByplatformCode(String platformCode) {
        List<GamePlatform> platformList = queryAllGamePlatform();
        if (CollectionUtil.isNotEmpty(platformList)) {
            platformList = platformList.stream()
                    .filter(platform -> platform.getPlatformCode().equals(platformCode))
                    .collect(Collectors.toList());
        }
        return CollectionUtil.isEmpty(platformList) ? null : platformList.get(0);
    }
    @Override
    public GamePlatform getGamePlatformByplatformCodeAndParentName(String platformCode,String parentName) {
        LambdaQueryWrapper<GamePlatform> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GamePlatform::getPlatformCode, platformCode);
        wrapper.eq(GamePlatform::getParentName, parentName);
        return gamePlatformMapper.selectOne(wrapper);
    }

    public List<GamePlatform> queryAllGamePlatform() {
        List<GamePlatform> platformList = null;
        try{
            Map<Object, Object> map = RedisUtils.hmget(RedisConstants.GAME_PLATFORM_KEY);
            platformList = (List<GamePlatform>)map.get(RedisConstants.GAME_PLATFORM_KEY);
//            RedisUtils.hdel(RedisConstants.GAME_PLATFORM_KEY);
        }catch (Exception e) {
            logger.error("queryAllGamePlatform.getcache.error "+e.getMessage(), e);
        }
        if (CollectionUtil.isEmpty(platformList)) {
            LambdaQueryWrapper<GamePlatform> wrapper = new LambdaQueryWrapper<>();
            platformList = gamePlatformMapper.selectList(wrapper);
            Map<String, Object> map = new HashMap<>();
            map.put(RedisConstants.GAME_PLATFORM_KEY, platformList);
            RedisUtils.hmset(RedisConstants.GAME_PLATFORM_KEY, map);
        }
        platformList.sort(Comparator.comparing(GamePlatform::getSortNumber));
        return platformList;
    }

    @Override
    public List<GamePlatform> getGamePlatformByParentName(String parentName) {
        List<GamePlatform> platformList = queryAllGamePlatform();
        if (CollectionUtil.isNotEmpty(platformList)) {
            platformList = platformList.stream()
                    .filter(platform -> platform.getParentName().equals(parentName))
                    .collect(Collectors.toList());
        }
        return platformList;
    }

    @Override
    public MemTradingBO getMemTradingInfo(String account) {
        Result<MemTradingBO> result = memBaseInfoFeignClient.getMemTradingInfo(account);
        if (Result.success().getCode().equals(result.getCode())) {
            MemTradingBO memBaseinfo = result.getData();
            return memBaseinfo;
        } else {
            throw new BizException("No client with requested id: " + account);
        }
    }

    @Override
    public GameCategory getGameCategoryById(Long id) {
        GameCategory gameCategory = (GameCategory) BusinessRedisUtils.hget(RedisConstants.GAME_CATEGORY_KEY, id + "");
        if (null == gameCategory) {
            gameCategory = gameCategoryMapper.selectById(id);
        }
        return gameCategory;
    }


    @Override
    public void updateUserBalance(MemTradingBO memTradingBO, BigDecimal changeAmount, GoldchangeEnum goldchangeEnum, TradingEnum tradingEnum) {
        logger.info("修改用户余额:memTradingBO{},changeAmount{},goldchangeEnum{},tradingEnum{}" + memTradingBO, changeAmount, goldchangeEnum, tradingEnum);
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
