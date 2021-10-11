package com.live.admin.modules.mem.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.live.admin.modules.mem.mapper.MemGrowthPointsConfigMapper;
import com.live.admin.modules.mem.service.IMemGrowthPointsConfigService;
import com.live.user.pojo.dto.MemGrowthPointsConfigDTO;
import com.live.user.pojo.entity.MemGrowthPointsConfig;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 成长积分配置表 服务实现类
 * </p>
 *
 * @author puff
 * @since 2021-08-30
 */
@Service
public class MemGrowthPointsConfigServiceImpl extends ServiceImpl<MemGrowthPointsConfigMapper, MemGrowthPointsConfig> implements IMemGrowthPointsConfigService {

    @Autowired
    private MemGrowthPointsConfigMapper configMapper;

    @Override
    public int updateGrowthPoints(List<MemGrowthPointsConfigDTO> list) {
        return configMapper.updateConfig(list);
    }
}
