package com.indo.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.indo.common.constant.GlobalConstants;
import com.indo.common.constant.RedisKeys;
import com.indo.common.enums.GoldchangeEnum;
import com.indo.common.enums.TradingEnum;
import com.indo.common.redis.utils.RedisUtils;
import com.indo.common.utils.SnowflakeIdWorker;
import com.indo.common.web.exception.BizException;
import com.indo.core.base.service.impl.SuperServiceImpl;
import com.indo.core.mapper.MemGoldChangeMapper;
import com.indo.core.mapper.MemTradingMapper;
import com.indo.core.pojo.bo.MemBaseinfoBo;
import com.indo.core.pojo.dto.MemGoldChangeDto;
import com.indo.core.pojo.entity.MemGoldChange;
import com.indo.core.pojo.vo.MemTradingVO;
import com.indo.core.service.IMemGoldChangeService;
import com.indo.core.service.IMemTradingService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.indo.common.utils.ViewUtil.getTradeOffAmount;


@Service
@Slf4j
public class MemTradingServiceImpl implements IMemTradingService {

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private MemTradingMapper memTradingMapper;


    @Override
    public MemTradingVO tradingInfo(String account) {
        return memTradingMapper.tradingInfo(account);
    }
}
