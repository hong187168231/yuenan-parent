package com.indo.admin.modules.agent.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.admin.modules.agent.mapper.AgentCashApplyMapper;
import com.indo.admin.modules.agent.mapper.AgentCashRecordMapper;
import com.indo.admin.modules.agent.service.IAgentCashRecordService;
import com.indo.admin.pojo.req.agnet.AgentCashReq;
import com.indo.admin.pojo.vo.agent.AgentCashRecordVO;
import com.indo.user.pojo.entity.AgentCashRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author xxx
 * @since 2022-01-13
 */
@Service
public class AgentCashRecordServiceImpl extends ServiceImpl<AgentCashRecordMapper, AgentCashRecord> implements IAgentCashRecordService {

    @Autowired
    private AgentCashRecordMapper agentCashRecordMapper;

    @Override
    public Page<AgentCashRecordVO> recordList(AgentCashReq req) {
        Page<AgentCashRecordVO> page = new Page<>(req.getPage(), req.getLimit());
        List<AgentCashRecordVO> list = agentCashRecordMapper.recordList(page, req);
        page.setRecords(list);
        return page;
    }
}
