package com.indo.admin.modules.agent.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.admin.pojo.entity.AgentApply;
import com.indo.admin.pojo.entity.MemAgent;
import com.indo.admin.pojo.vo.AgentVo;
import com.indo.common.mybatis.base.PageResult;
import com.indo.user.pojo.dto.AgentApplyDTO;
import com.indo.user.pojo.dto.AgentDTO;

import java.util.List;

/**
 * <p>
 * 会员下级表 服务类
 * </p>
 *
 * @author puff
 * @since 2021-10-23
 */
public interface IMemAgentService extends IService<MemAgent> {

    List<AgentVo> agentlist(Page<AgentVo> page, AgentDTO agentDTO);

    String queryAgentByNickName(String nickName);

    boolean addAgent(Long memId);

}
