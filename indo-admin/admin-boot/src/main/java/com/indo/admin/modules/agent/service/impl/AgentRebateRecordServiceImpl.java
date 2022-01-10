package com.indo.admin.modules.agent.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.admin.modules.agent.service.IAgentRebateRecordService;
import com.indo.admin.modules.mem.mapper.AgentRebateRecordMapper;
import com.indo.admin.modules.mem.req.MemRebateRecordPageReq;
import com.indo.admin.pojo.entity.AgentRebateRecord;
import com.indo.admin.pojo.req.agnet.AgentRebateRecordReq;
import com.indo.admin.pojo.vo.agent.AgentRebateRecordVO;
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
public class AgentRebateRecordServiceImpl extends ServiceImpl<AgentRebateRecordMapper, AgentRebateRecord> implements IAgentRebateRecordService {

    @Autowired
    private AgentRebateRecordMapper memRebateRecordMapper;

    @Override
    public Page<AgentRebateRecordVO> queryList(AgentRebateRecordReq req) {
        Page<AgentRebateRecordVO> page = new Page<>(req.getPage(), req.getLimit());
        List<AgentRebateRecordVO> list = memRebateRecordMapper.queryList(page, req);
        page.setRecords(list);
        return page;
    }
}
