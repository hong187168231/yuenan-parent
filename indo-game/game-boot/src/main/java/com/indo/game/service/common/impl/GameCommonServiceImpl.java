package com.indo.game.service.common.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.indo.common.constant.RedisKeys;
import com.indo.common.result.Result;
import com.indo.common.web.exception.BizException;
import com.indo.game.common.util.GameBusinessRedisUtils;
import com.indo.game.mapper.manage.GamePlatformMapper;
import com.indo.game.pojo.entity.manage.GamePlatform;
import com.indo.game.service.common.GameCommonService;
import com.indo.user.api.MemBaseInfoFeignClient;
import com.indo.user.pojo.entity.MemBaseinfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class GameCommonServiceImpl implements GameCommonService {


    @Autowired
    private GamePlatformMapper gamePlatformMapper;

    @Resource
    private MemBaseInfoFeignClient memBaseInfoFeignClient;

    @Override
    public GamePlatform getGamePlatformByplatformCode(String platformCode) {
        GamePlatform gamePlatform = GameBusinessRedisUtils.get(RedisKeys.GAME_PLATFORM_KEY + platformCode);
        if (null == gamePlatform) {
            LambdaQueryWrapper<GamePlatform> wrapper = new LambdaQueryWrapper<GamePlatform>();
            wrapper.eq(GamePlatform::getPlatformCode,platformCode);
            gamePlatform = gamePlatformMapper.selectOne(wrapper);
        }
        return gamePlatform;
    }


    public MemBaseinfo getMemBaseInfo(String userId) {
        Result<MemBaseinfo> result = memBaseInfoFeignClient.getMemBaseInfo(Long.parseLong(userId));
        if (Result.success().getCode().equals(result.getCode())) {
            MemBaseinfo memBaseinfo = result.getData();
            return memBaseinfo;
        } else {
            throw new BizException("No client with requested id: " + userId);
        }
    }

    @Override
    public MemBaseinfo getByAccountNo(String accountNo) {
        Result<MemBaseinfo> result = memBaseInfoFeignClient.getByAccountNo(accountNo);
        if (Result.success().getCode().equals(result.getCode())) {
            MemBaseinfo memBaseinfo = result.getData();
            return memBaseinfo;
        } else {
            throw new BizException("No client with requested id: " + accountNo);
        }
    }


}
