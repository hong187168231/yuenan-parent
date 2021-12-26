package com.indo.admin.modules.mem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.modules.mem.entity.MemPendingRebate;
import com.indo.admin.modules.mem.req.MemPendingRebatePageReq;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xxx
 * @since 2021-12-26
 */
@Mapper
public interface MemPendingRebateMapper extends BaseMapper<MemPendingRebate> {
    List<MemPendingRebate> queryList(@Param("page") Page<MemPendingRebate> page, @Param("dto") MemPendingRebatePageReq dto);
}
