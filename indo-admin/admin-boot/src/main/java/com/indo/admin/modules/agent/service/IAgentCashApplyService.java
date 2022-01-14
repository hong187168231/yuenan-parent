package com.indo.admin.modules.agent.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.admin.pojo.req.agnet.AgentCashReq;
import com.indo.admin.pojo.vo.agent.AgentCashApplyVO;
import com.indo.core.pojo.entity.AgentCashApply;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author xxx
 * @since 2022-01-13
 */
public interface IAgentCashApplyService extends IService<AgentCashApply> {


    Page<AgentCashApplyVO> cashApplyList(AgentCashReq req);
}
