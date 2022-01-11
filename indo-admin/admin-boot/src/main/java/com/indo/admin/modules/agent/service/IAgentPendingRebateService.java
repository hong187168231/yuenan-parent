package com.indo.admin.modules.agent.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.admin.modules.mem.entity.MemPendingRebate;
import com.indo.admin.modules.mem.req.MemGrantRebateReq;
import com.indo.admin.modules.mem.req.MemPendingRebatePageReq;
import com.indo.admin.pojo.entity.AgentPendingRebate;
import com.indo.admin.pojo.req.agnet.AgentPendingRebateReq;
import com.indo.admin.pojo.vo.agent.AgentPendingRebateVO;

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

    boolean grantRebate(Long id);

}
