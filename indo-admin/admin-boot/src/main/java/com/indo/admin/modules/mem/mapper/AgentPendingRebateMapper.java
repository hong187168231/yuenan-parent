package com.indo.admin.modules.mem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.pojo.req.agnet.AgentPendingRebateReq;
import com.indo.admin.pojo.vo.agent.AgentPendingRebateVO;
import com.indo.core.pojo.entity.AgentPendingRebate;
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
public interface AgentPendingRebateMapper extends BaseMapper<AgentPendingRebate> {
    List<AgentPendingRebateVO> queryList(@Param("page") Page<AgentPendingRebateVO> page, @Param("req") AgentPendingRebateReq req);
}
