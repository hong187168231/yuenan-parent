package com.live.admin.modules.mem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.live.user.pojo.dto.MemGrowthPointsConfigDTO;
import com.live.user.pojo.entity.MemGrowthPointsConfig;
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
