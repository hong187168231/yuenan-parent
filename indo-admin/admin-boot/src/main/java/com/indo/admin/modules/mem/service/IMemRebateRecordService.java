package com.indo.admin.modules.mem.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.admin.modules.mem.entity.MemRebateRecord;
import com.indo.admin.modules.mem.req.MemRebateRecordPageReq;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xxx
 * @since 2021-12-26
 */
public interface IMemRebateRecordService extends IService<MemRebateRecord> {

    Page<MemRebateRecord> queryList(MemRebateRecordPageReq req);
}
