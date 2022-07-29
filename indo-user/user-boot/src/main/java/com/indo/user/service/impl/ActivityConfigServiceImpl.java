package com.indo.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.indo.common.constant.RedisKeys;
import com.indo.common.redis.utils.RedisUtils;
import com.indo.core.base.service.impl.SuperServiceImpl;
import com.indo.core.mapper.ActivityConfigMapper;
import com.indo.core.pojo.entity.ActivityConfig;
import com.indo.user.service.ActivityConfigService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ActivityConfigServiceImpl extends SuperServiceImpl<ActivityConfigMapper, ActivityConfig> implements ActivityConfigService {

    @Override
    public List<ActivityConfig> findActivityConfList() {
        if(RedisUtils.hasKey(RedisKeys.SYS_ACTIVITY_CONFIG_KEY)){
            return RedisUtils.get(RedisKeys.SYS_ACTIVITY_CONFIG_KEY);
        }
        LambdaQueryWrapper<ActivityConfig> wrapper = new LambdaQueryWrapper<>();
        return baseMapper.selectList(wrapper);
    }
}
