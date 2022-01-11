package com.indo.admin.modules.agent.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.admin.modules.agent.service.IAgentPendingRebateService;
import com.indo.admin.modules.mem.mapper.AgentPendingRebateMapper;
import com.indo.admin.modules.mem.mapper.AgentRebateRecordMapper;
import com.indo.admin.pojo.entity.AgentPendingRebate;
import com.indo.admin.pojo.entity.AgentRebateRecord;
import com.indo.admin.pojo.req.agnet.AgentPendingRebateReq;
import com.indo.admin.pojo.vo.agent.AgentPendingRebateVO;
import com.indo.common.web.util.JwtUtils;
import com.indo.core.service.IMemGoldChangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class AgentPendingRebateServiceImpl extends ServiceImpl<AgentPendingRebateMapper, AgentPendingRebate> implements IAgentPendingRebateService {

    @Autowired
    private AgentPendingRebateMapper memPendingRebateMapper;
    @Autowired
    private AgentRebateRecordMapper agentRebateRecordMapper;

    @Autowired
    private IMemGoldChangeService iMemGoldChangeService;

    @Override
    public Page<AgentPendingRebateVO> queryList(AgentPendingRebateReq req) {
        Page<AgentPendingRebateVO> page = new Page<>(req.getPage(), req.getLimit());
        List<AgentPendingRebateVO> list = memPendingRebateMapper.queryList(page, req);
        page.setRecords(list);
        return page;
    }

    @Override
    @Transactional
    public boolean grantRebate(Long id) {
        AgentPendingRebate memPendingRebate = baseMapper.selectById(id);
        memPendingRebate.setStatus(1);
        if (baseMapper.updateById(memPendingRebate) > 0) {
            AgentRebateRecord agentRebateRecord = new AgentRebateRecord();
            agentRebateRecord.setMemId(memPendingRebate.getMemId());
            agentRebateRecord.setRebateAmout(memPendingRebate.getRebateAmount());
            agentRebateRecord.setYesterdayRemain(memPendingRebate.getYesterdayRemain());
            agentRebateRecord.setCreateUser(JwtUtils.getUsername());
            int row = agentRebateRecordMapper.insert(agentRebateRecord);
            if (row > 0) {
                iMemGoldChangeService.updateMemGoldChange(null);
                return true;
            }
        }
        return false;
    }
}
