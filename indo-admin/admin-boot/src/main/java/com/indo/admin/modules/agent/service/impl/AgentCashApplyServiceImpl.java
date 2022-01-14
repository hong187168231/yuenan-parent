package com.indo.admin.modules.agent.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.admin.modules.agent.mapper.AgentCashApplyMapper;
import com.indo.admin.modules.agent.service.IAgentCashApplyService;
import com.indo.admin.pojo.req.agnet.AgentCashReq;
import com.indo.admin.pojo.vo.agent.AgentCashApplyVO;
import com.indo.core.pojo.entity.AgentCashApply;
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
public class AgentCashApplyServiceImpl extends ServiceImpl<AgentCashApplyMapper, AgentCashApply> implements IAgentCashApplyService {


    @Autowired
    private AgentCashApplyMapper agentCashApplyMapper;

    @Override
    public Page<AgentCashApplyVO> cashApplyList(AgentCashReq req) {
        Page<AgentCashApplyVO> page = new Page<>(req.getPage(), req.getLimit());
        List<AgentCashApplyVO> list = agentCashApplyMapper.cashApplyList(page, req);
        page.setRecords(list);
        return page;
    }
}
