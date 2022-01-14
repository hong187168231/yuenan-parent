package com.indo.admin.modules.agent.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.admin.pojo.req.agnet.AgentPendingRebateReq;
import com.indo.admin.pojo.vo.agent.AgentPendingRebateVO;
import com.indo.core.pojo.entity.AgentPendingRebate;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xxx
 * @since 2021-12-26
 */
public interface IAgentPendingRebateService extends IService<AgentPendingRebate> {

    Page<AgentPendingRebateVO> queryList(AgentPendingRebateReq req);



}
