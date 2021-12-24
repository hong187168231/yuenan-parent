package com.indo.admin.modules.mem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.admin.modules.mem.entity.MemAgentApply;
import com.indo.admin.modules.mem.req.MemAgentApplyPageReq;
import com.indo.admin.modules.mem.req.MemApplyAuditReq;
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

    PageResult<MemAgentApply> getPage(MemAgentApplyPageReq req);

    void applyAudit(MemApplyAuditReq req);
}
