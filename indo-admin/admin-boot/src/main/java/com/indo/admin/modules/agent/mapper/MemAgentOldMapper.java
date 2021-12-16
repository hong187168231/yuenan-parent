package com.indo.admin.modules.agent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.pojo.entity.MemAgent;
import com.indo.admin.pojo.vo.AgentVo;
import com.indo.user.pojo.dto.AgentDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MemAgentOldMapper extends BaseMapper<MemAgent> {
    List<AgentVo> agentList(Page<AgentVo> page, AgentDTO agentDTO);
}
