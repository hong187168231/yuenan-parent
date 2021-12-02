package com.indo.game.service.gamecommon.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.indo.common.constant.RedisKeys;
import com.indo.game.game.RedisBaseUtil;
import com.indo.game.mapper.manage.GamePlatformMapper;
import com.indo.game.pojo.entity.manage.GamePlatform;
import com.indo.game.service.gamecommon.GameCommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameCommonServiceImpl implements GameCommonService {

    private static final Logger logger = LoggerFactory.getLogger(GameCommonServiceImpl.class);

    @Autowired
    private GamePlatformMapper gamePlatformMapper;

    @Override
    public GamePlatform getGamePlatformByplatformCode(String platformCode) {
        GamePlatform gamePlatform = RedisBaseUtil.get(RedisKeys.GAME_PLATFORM_KEY + platformCode);
        if (null == gamePlatform) {
            LambdaQueryWrapper<GamePlatform> wrapper = new LambdaQueryWrapper<GamePlatform>();
            wrapper.eq(GamePlatform::getIsStart,platformCode);
            gamePlatform = gamePlatformMapper.selectOne(wrapper);
        }
        return gamePlatform;
    }



}
