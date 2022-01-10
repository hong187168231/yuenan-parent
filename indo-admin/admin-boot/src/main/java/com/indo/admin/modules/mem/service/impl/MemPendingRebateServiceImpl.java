package com.indo.admin.modules.mem.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.admin.modules.mem.entity.MemPendingRebate;
import com.indo.admin.modules.mem.entity.MemRebateRecord;
import com.indo.admin.modules.mem.mapper.MemPendingRebateMapper;
import com.indo.admin.modules.mem.mapper.MemRebateRecordMapper;
import com.indo.admin.modules.mem.req.MemGrantRebateReq;
import com.indo.admin.modules.mem.req.MemPendingRebatePageReq;
import com.indo.admin.modules.mem.service.IMemPendingRebateService;
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
public class MemPendingRebateServiceImpl extends ServiceImpl<MemPendingRebateMapper, MemPendingRebate> implements IMemPendingRebateService {

    @Autowired
    private MemPendingRebateMapper memPendingRebateMapper;
    @Autowired
    private MemRebateRecordMapper memRebateRecordMapper;

    @Override
    public Page<MemPendingRebate> queryList(MemPendingRebatePageReq req) {
        Page<MemPendingRebate> page = new Page<>(req.getPage(), req.getLimit());
        List<MemPendingRebate> list = memPendingRebateMapper.queryList(page, req);
        page.setRecords(list);
        return page;
    }

    @Override
    public void grantRebate(MemGrantRebateReq req) {
        MemPendingRebate memPendingRebate = new MemPendingRebate();
        memPendingRebate.setId(req.getId());
        memPendingRebate.setStatus(1);
        baseMapper.updateById(memPendingRebate);

        MemPendingRebate memPendingRebate1 = baseMapper.selectById(req.getId());
        MemRebateRecord memRebateRecord = new MemRebateRecord();
        memRebateRecord.setMemId(memPendingRebate1.getMemId());
        memRebateRecord.setMemGroupId(memPendingRebate1.getMemGroupId());
        memRebateRecord.setMemLevel(memPendingRebate1.getMemLevel());
        memRebateRecord.setTeamMembers(memPendingRebate1.getTeamMembers());
        memRebateRecord.setTeamBets(memPendingRebate1.getTeamBets());
        memRebateRecord.setRebateAmout(memPendingRebate1.getRebateAmount());
        memRebateRecord.setYesterdayBalance(memPendingRebate1.getLastWeekBalance());
        //todo
        memRebateRecord.setOperator("");
        memRebateRecordMapper.insert(memRebateRecord);
        return;
    }
}
