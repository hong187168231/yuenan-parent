package com.indo.job.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.common.constant.RedisKeys;
import com.indo.common.redis.utils.RedisUtils;
import com.indo.common.utils.CollectionUtil;
import com.indo.core.mapper.MemLevelMapper;
import com.indo.core.pojo.entity.MemLevel;
import com.indo.job.service.IMemLevelService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 会员等级表 服务实现类
 * </p>
 *
 * @author xxx
 * @since 2021-11-21
 */
@Service
public class MemLevelServiceImpl extends ServiceImpl<MemLevelMapper, MemLevel> implements IMemLevelService {


    @Override
    public Integer getLevelByCondition(BigDecimal totalDeposit, BigDecimal totalBet) {
        Integer totalD = totalDeposit.intValue() / 10000;
        Integer totalB = totalBet.intValue() / 10000;
        Integer level = 0;
        if (totalD >= 5 && totalB >= 5) {
            level = 1;

        }
        if (totalD >= 3000 && totalB >= 300) {
            level = 2;

        }
        if (totalD >= 15000 && totalB >= 1500) {
            level = 3;

        }
        if (totalD >= 300000 && totalB >= 30000) {
            level = 4;

        }
        if (totalD >= 1500000 && totalB >= 150000) {
            level = 5;

        }
        if (totalD >= 3000000 && totalB >= 300000) {
            level = 6;

        }
        if (totalD >= 9000000 && totalB >= 900000) {
            level = 7;

        }
        if (totalD >= 15000000 && totalB >= 1500000) {
            level = 8;

        }
        if (totalD >= 30000000 && totalB >= 2400000) {
            level = 9;

        }
        if (totalD >= 150000000 || totalB >= 4500000) {
            level = 10;

        }
        return level;
    }


    private List<MemLevel> getLevelList() {
        List<MemLevel> cacheList = RedisUtils.get(RedisKeys.SYS_LEVEL_KEY);
        if (CollectionUtil.isNotEmpty(cacheList)) {
            return cacheList;
        }
        LambdaQueryWrapper<MemLevel> wrapper = new LambdaQueryWrapper<>();
        List<MemLevel> list = this.baseMapper.selectList(wrapper);
        return list;
    }
}
