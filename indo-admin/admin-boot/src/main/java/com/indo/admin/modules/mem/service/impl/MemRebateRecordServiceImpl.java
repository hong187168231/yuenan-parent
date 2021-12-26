package com.indo.admin.modules.mem.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.admin.modules.mem.entity.MemPendingRebate;
import com.indo.admin.modules.mem.entity.MemRebateRecord;
import com.indo.admin.modules.mem.mapper.MemRebateRecordMapper;
import com.indo.admin.modules.mem.req.MemRebateRecordPageReq;
import com.indo.admin.modules.mem.service.IMemRebateRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author xxx
 * @since 2021-12-26
 */
@Service
public class MemRebateRecordServiceImpl extends ServiceImpl<MemRebateRecordMapper, MemRebateRecord> implements IMemRebateRecordService {

    @Autowired
    private MemRebateRecordMapper memRebateRecordMapper;

    @Override
    public Page<MemRebateRecord> queryList(MemRebateRecordPageReq req) {
        Page<MemRebateRecord> page = new Page<>(req.getPage(), req.getLimit());
        List<MemRebateRecord> list = memRebateRecordMapper.queryList(page, req);
        page.setRecords(list);
        return page;
    }
}
