package com.indo.admin.modules.agent.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.admin.pojo.req.agnet.AgentCashReq;
import com.indo.admin.pojo.vo.agent.AgentCashRecordVO;
import com.indo.core.pojo.entity.AgentCashRecord;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author xxx
 * @since 2022-01-13
 */
public interface IAgentCashRecordService extends IService<AgentCashRecord> {


    Page<AgentCashRecordVO> recordList(AgentCashReq req);
}
