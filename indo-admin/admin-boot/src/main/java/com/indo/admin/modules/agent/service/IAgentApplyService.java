package com.indo.admin.modules.agent.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.admin.common.enums.AgentApplyEnum;
import com.indo.admin.pojo.entity.AgentApply;
import com.indo.common.mybatis.base.PageResult;
import com.indo.user.pojo.dto.AgentApplyDTO;
import com.indo.user.pojo.dto.AgentReportDto;

import java.util.List;

/**
 * <p>
 * 会员下级表 服务类
 * </p>
 *
 * @author puff
 * @since 2021-10-23
 */
public interface IAgentApplyService extends IService<AgentApply> {


     Page<AgentApply> agentApplylist(AgentApplyDTO agentApplyDTO);


     void  applyOperate(AgentApplyEnum agentApplyEnum);


}
