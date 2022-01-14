package com.indo.admin.modules.agent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.pojo.req.MemAgentApplyPageReq;
import com.indo.admin.pojo.vo.agent.AgentApplyVO;
import com.indo.core.pojo.entity.AgentApply;
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
public interface AgentApplyMapper extends BaseMapper<AgentApply> {

    List<AgentApplyVO> queryList(@Param("page") Page<AgentApplyVO> page, @Param("req")  MemAgentApplyPageReq req);
}
