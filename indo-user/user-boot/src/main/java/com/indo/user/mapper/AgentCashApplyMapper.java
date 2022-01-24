package com.indo.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.pojo.req.agnet.AgentCashReq;
import com.indo.admin.pojo.vo.agent.AgentCashApplyVO;
import com.indo.core.pojo.entity.AgentCashApply;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author xxx
 * @since 2022-01-13
 */
@Mapper
public interface AgentCashApplyMapper extends BaseMapper<AgentCashApply> {


}
