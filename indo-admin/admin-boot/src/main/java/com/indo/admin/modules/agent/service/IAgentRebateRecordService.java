package com.indo.admin.modules.agent.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.admin.modules.mem.req.MemRebateRecordPageReq;
import com.indo.admin.pojo.entity.AgentRebateRecord;
import com.indo.admin.pojo.req.agnet.AgentRebateRecordReq;
import com.indo.admin.pojo.vo.agent.AgentRebateRecordVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xxx
 * @since 2021-12-26
 */
public interface IAgentRebateRecordService extends IService<AgentRebateRecord> {

    Page<AgentRebateRecordVO> queryList(AgentRebateRecordReq req);
}
