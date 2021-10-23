package com.indo.admin.modules.agent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.pojo.entity.MemAgent;
import com.indo.admin.pojo.vo.AgentVo;
import com.indo.user.pojo.dto.AgentDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 会员下级表 Mapper 接口
 * </p>
 *
 * @author puff
 * @since 2021-10-23
 */

@Mapper
public interface MemAgentMapper extends BaseMapper<MemAgent> {

    List<AgentVo> agentList(Page<AgentVo> page, AgentDTO agentDTO);
}
