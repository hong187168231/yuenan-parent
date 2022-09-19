package com.indo.admin.modules.agent.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.admin.pojo.req.agnet.MemAgentApplyReq;
import com.indo.admin.pojo.req.agnet.MemApplyAuditReq;
import com.indo.admin.pojo.vo.agent.AgentApplyVO;
import com.indo.core.pojo.entity.AgentApply;
import com.indo.core.pojo.entity.MemBaseinfo;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员下级表 服务类
 * </p>
 *
 * @author xxx
 * @since 2021-11-19
 */
public interface IAgentApplyService extends IService<AgentApply> {

    Page<AgentApplyVO> getPage(MemAgentApplyReq req);

    boolean applyAudit(MemApplyAuditReq req, HttpServletRequest request);
}
