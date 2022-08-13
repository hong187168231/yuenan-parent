package com.indo.admin.modules.agent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.core.pojo.req.agent.AgentRebateRecordReq;
import com.indo.core.pojo.vo.agent.AgentRebateRecordVO;
import com.indo.core.pojo.entity.AgentRebateRecord;
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

    List<AgentRebateRecordVO> queryList(@Param("page") Page<AgentRebateRecordVO> page, @Param("req") AgentRebateRecordReq req);
}
