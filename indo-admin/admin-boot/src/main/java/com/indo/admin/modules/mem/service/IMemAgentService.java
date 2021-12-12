package com.indo.admin.modules.mem.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.admin.modules.mem.entity.MemAgent;
import com.indo.admin.modules.mem.req.MemAgentPageReq;
import com.indo.common.result.PageResult;

/**
 * <p>
 * 会员下级表 服务类
 * </p>
 *
 * @author xxx
 * @since 2021-12-11
 */
public interface IMemAgentService extends IService<MemAgent> {

    PageResult<MemAgent> getPage(MemAgentPageReq req);
}
