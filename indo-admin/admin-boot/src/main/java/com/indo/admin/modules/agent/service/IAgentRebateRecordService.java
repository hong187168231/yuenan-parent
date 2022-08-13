package com.indo.admin.modules.agent.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.core.pojo.req.agent.AgentRebateRecordReq;
import com.indo.core.pojo.vo.agent.AgentRebateRecordVO;
import com.indo.core.pojo.entity.AgentRebateRecord;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xxx
 * @since 2021-12-26
 */
public interface IAgentRebateRecordService extends IService<AgentRebateRecord> {

    boolean grantRebate(Long id);

    Page<AgentRebateRecordVO> queryList(AgentRebateRecordReq req);
}
