package com.indo.game.service.common.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.indo.common.constant.RedisKeys;
import com.indo.common.enums.GoldchangeEnum;
import com.indo.common.enums.TradingEnum;
import com.indo.common.result.Result;
import com.indo.common.web.exception.BizException;
import com.indo.game.common.util.GameBusinessRedisUtils;
import com.indo.game.mapper.frontend.GamePlatformMapper;
import com.indo.game.pojo.entity.manage.GamePlatform;
import com.indo.game.service.common.GameCommonService;
import com.indo.user.api.MemBaseInfoFeignClient;
import com.indo.user.api.MemGoldFeignClient;
import com.indo.user.pojo.dto.MemGoldChangeDTO;
import com.indo.user.pojo.entity.MemBaseinfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Slf4j
@Service(value = "gameCommonService")
public class GameCommonServiceImpl implements GameCommonService {


    @Autowired
    private GamePlatformMapper gamePlatformMapper;

    @Resource
    private MemBaseInfoFeignClient memBaseInfoFeignClient;
    @Resource
    private MemGoldFeignClient memGoldFeignClient;


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
        Result<MemBaseinfo> result = memBaseInfoFeignClient.getByAccount(accountNo);
        MemBaseinfo memBaseinfo = null;
        if (null != result && Result.success().getCode().equals(result.getCode())) {
            memBaseinfo = result.getData();

        } else {
            throw new BizException("No client with requested accountNo: " + accountNo);
        }
        return memBaseinfo;
    }

    @Override
    public void updateUserBalance(MemBaseinfo memBaseinfo, BigDecimal changeAmount, GoldchangeEnum goldchangeEnum, TradingEnum tradingEnum) {
        MemGoldChangeDTO goldChangeDO = new MemGoldChangeDTO();
        goldChangeDO.setChangeAmount(changeAmount);
        goldChangeDO.setTradingEnum(tradingEnum);

        goldChangeDO.setGoldchangeEnum(goldchangeEnum);
        goldChangeDO.setUserId(memBaseinfo.getId());
        goldChangeDO.setUpdateUser(memBaseinfo.getAccount());
//      goldChangeDO.setRefId(rechargeOrder.getRechargeOrderId());
        Boolean flag;
        Result result = memGoldFeignClient.updateMemGoldChange(goldChangeDO);
        if (null != result && Result.success().getCode().equals(result.getCode())) {
            flag = (Boolean) result.getData();
            if (!flag) {
                throw new BizException("修改余额失败");
            }
        } else {
            throw new BizException("No client with 8requested goldChangeDO: " + JSON.toJSONString(goldChangeDO));
        }
    }


}
