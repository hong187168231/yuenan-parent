package com.indo.admin.modules.activity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.core.mapper.ActivityConfigMapper;
import com.indo.admin.modules.activity.service.IActivityConfigService;
import com.indo.core.pojo.entity.ActivityConfig;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 活动配置表 服务实现类
 * </p>
 *
 * @author xxx
 * @since 2022-07-13
 */
@Service
public class ActivityConfigServiceImpl extends ServiceImpl<ActivityConfigMapper, ActivityConfig> implements IActivityConfigService {

    @Override
    public ActivityConfig findActivityConfigByType(Integer types) {
        LambdaQueryWrapper<ActivityConfig> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ActivityConfig::getTypes,types);
        return baseMapper.selectOne(wrapper);
    }
}
