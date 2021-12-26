package com.indo.admin.modules.mem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.modules.mem.entity.MemRebateRecord;
import com.indo.admin.modules.mem.req.MemRebateRecordPageReq;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author xxx
 * @since 2021-12-26
 */
@Mapper
public interface MemRebateRecordMapper extends BaseMapper<MemRebateRecord> {

    List<MemRebateRecord> queryList(@Param("page") Page<MemRebateRecord> page, @Param("dto") MemRebateRecordPageReq req);
}
