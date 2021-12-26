package com.indo.admin.modules.mem.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.admin.modules.mem.entity.MemPendingRebate;
import com.indo.admin.modules.mem.req.MemGrantRebateReq;
import com.indo.admin.modules.mem.req.MemPendingRebatePageReq;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xxx
 * @since 2021-12-26
 */
public interface IMemPendingRebateService extends IService<MemPendingRebate> {

    Page<MemPendingRebate> queryList(MemPendingRebatePageReq req);

    void grantRebate(MemGrantRebateReq req);

}
