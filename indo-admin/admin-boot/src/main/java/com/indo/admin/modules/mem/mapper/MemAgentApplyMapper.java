package com.indo.admin.modules.mem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.modules.mem.entity.MemAgentApply;
import com.indo.admin.modules.mem.req.MemAgentApplyPageReq;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 会员下级表 Mapper 接口
 * </p>
 *
 * @author xxx
 * @since 2021-11-19
 */
@Mapper
public interface MemAgentApplyMapper extends BaseMapper<MemAgentApply> {

    List<MemAgentApply> queryList(@Param("page") Page<MemAgentApply> page,@Param("req")  MemAgentApplyPageReq req);
}
