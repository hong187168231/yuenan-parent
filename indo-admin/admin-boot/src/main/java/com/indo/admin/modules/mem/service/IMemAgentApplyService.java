package com.indo.admin.modules.mem.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.admin.modules.mem.entity.MemAgentApply;
import com.indo.admin.modules.mem.req.MemAgentApplyPageReq;
import com.indo.admin.modules.mem.req.MemApplyAuditReq;
import com.indo.admin.pojo.vo.agent.AgentApplyVO;
import com.indo.common.result.PageResult;

/**
 * <p>
 * 会员下级表 服务类
 * </p>
 *
 * @author xxx
 * @since 2021-11-19
 */
public interface IMemAgentApplyService extends IService<MemAgentApply> {

    Page<AgentApplyVO> getPage(MemAgentApplyPageReq req);

    boolean applyAudit(MemApplyAuditReq req);
}
