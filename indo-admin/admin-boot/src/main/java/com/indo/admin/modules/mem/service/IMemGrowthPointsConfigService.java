package com.indo.admin.modules.mem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.user.pojo.dto.MemGrowthPointsConfigDTO;
import com.indo.user.pojo.entity.MemGrowthPointsConfig;

import java.util.List;

/**
 * <p>
 * 成长积分配置表 服务类
 * </p>
 *
 * @author puff
 * @since 2021-08-30
 */
public interface IMemGrowthPointsConfigService extends IService<MemGrowthPointsConfig> {

    /**
     * 修改成长积分配置
     */
    int updateGrowthPoints(List<MemGrowthPointsConfigDTO> list);
}
