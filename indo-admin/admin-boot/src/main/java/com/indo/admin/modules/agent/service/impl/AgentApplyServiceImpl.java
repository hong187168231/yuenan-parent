package com.indo.admin.modules.agent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.admin.common.enums.AgentApplyEnum;
import com.indo.admin.modules.agent.mapper.AgentApplyMapper;
import com.indo.admin.modules.agent.service.IAgentApplyService;
import com.indo.admin.pojo.dto.AgentApplyDTO;
import com.indo.admin.pojo.entity.AgentApply;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 会员下级表 服务实现类
 * </p>
 *
 * @author puff
 * @since 2021-10-23
 */
@Service
public class AgentApplyServiceImpl extends ServiceImpl<AgentApplyMapper, AgentApply> implements IAgentApplyService {

    @Override
    public Page<AgentApply> agentApplylist(AgentApplyDTO agentApplyDTO) {
        Page<AgentApply> agentApplyPage = new Page<>(agentApplyDTO.getPage(), agentApplyDTO.getLimit());
        LambdaQueryWrapper<AgentApply> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AgentApply::getMemId, agentApplyDTO.getMemId()).
                eq(AgentApply::getMobile, agentApplyDTO.getMobile());
        Page<AgentApply> pageList = this.baseMapper.selectPage(agentApplyPage, wrapper);
        return pageList;
    }

    @Override
    public void applyOperate(AgentApplyEnum agentApplyEnum) {
        AgentApply agentApply = new AgentApply();
        agentApply.setStatus(agentApplyEnum.getValue());
        baseMapper.updateById(agentApply);
    }
}
