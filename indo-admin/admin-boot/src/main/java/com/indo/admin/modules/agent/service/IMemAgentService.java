package com.indo.admin.modules.agent.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.admin.pojo.entity.MemAgent;
import com.indo.admin.pojo.vo.AgentDetailVO;
import com.indo.admin.pojo.vo.AgentVo;
import com.indo.admin.pojo.vo.SubordinateMemVo;
import com.indo.user.pojo.dto.AgentDTO;

import java.util.List;

/**
 * <p>
 * 会员下级表 服务类
 * </p>
 *
 * @author puff
 *
 *
 * @since 2021-10-23
 */
public interface IMemAgentService extends IService<MemAgent> {

    List<AgentVo> agentlist(Page<AgentVo> page, AgentDTO agentDTO);

    List<SubordinateMemVo> subordinateMemList(Long agentId);

    String queryAgentByNickName(String nickName);

    boolean addAgent(Long memId);

    boolean updateAgent(Long agentId);

    AgentDetailVO agentDetail(Long agentId);



}
