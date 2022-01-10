package com.indo.admin.modules.mem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.modules.mem.req.MemRebateRecordPageReq;
import com.indo.admin.pojo.entity.AgentRebateRecord;
import com.indo.admin.pojo.req.agnet.AgentRebateRecordReq;
import com.indo.admin.pojo.vo.agent.AgentRebateRecordVO;
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
public interface AgentRebateRecordMapper extends BaseMapper<AgentRebateRecord> {

    List<AgentRebateRecordVO> queryList(@Param("page") Page<AgentRebateRecordVO> page, @Param("dto") AgentRebateRecordReq req);
}
