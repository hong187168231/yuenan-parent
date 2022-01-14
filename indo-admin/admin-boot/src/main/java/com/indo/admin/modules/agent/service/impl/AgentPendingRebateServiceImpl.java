package com.indo.admin.modules.agent.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.admin.modules.agent.mapper.AgentRebateMapper;
import com.indo.admin.modules.agent.service.IAgentPendingRebateService;
import com.indo.admin.pojo.req.agnet.AgentPendingRebateReq;
import com.indo.admin.pojo.vo.agent.AgentPendingRebateVO;
import com.indo.common.web.util.JwtUtils;
import com.indo.core.pojo.entity.AgentPendingRebate;
import com.indo.core.pojo.entity.AgentRebate;
import com.indo.core.pojo.entity.AgentRebateRecord;
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
    private AgentRebateMapper agentRebateMapper;

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
                AgentRebate agentRebate = agentRebateMapper.selectAgentRebateByMemId(agentRebateRecord.getMemId());
                if (ObjectUtil.isNull(agentRebate)) {
                    agentRebate = new AgentRebate();
                    agentRebate.setMemId(agentRebateRecord.getMemId());
                    agentRebate.setRebateAmount(agentRebateRecord.getRebateAmout());
                    agentRebate.setCreateUser(JwtUtils.getUsername());
                } else {
                    agentRebateMapper.modifyRebateAmount(agentRebateRecord.getMemId(), agentRebateRecord.getRebateAmout());
                }
                return true;
            }
        }
        return false;
    }
}
