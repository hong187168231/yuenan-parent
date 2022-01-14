package com.indo.admin.modules.mem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.indo.core.pojo.entity.MemRebate;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 返点配置表 Mapper 接口
 * </p>
 *
 * @author kevin
 * @since 2021-11-04
 */
@Mapper
@Repository
public interface MemRebateMapper extends BaseMapper<MemRebate> {

}
