package com.indo.admin.modules.mem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.indo.user.pojo.dto.MemGrowthPointsConfigDTO;
import com.indo.user.pojo.entity.MemGrowthPointsConfig;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 成长积分配置表 Mapper 接口
 * </p>
 *
 * @author puff
 * @since 2021-08-30
 */
@Mapper
public interface MemGrowthPointsConfigMapper extends BaseMapper<MemGrowthPointsConfig> {

    /**
     * 修改成长积分配置
     */
    int updateConfig(List<MemGrowthPointsConfigDTO> list);
}
