package com.indo.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.admin.pojo.entity.AgentRebateRecord;
import com.indo.admin.pojo.req.agnet.AgentRebateRecordReq;
import com.indo.admin.pojo.vo.agent.AgentRebateInfoVO;
import com.indo.admin.pojo.vo.agent.AgentRebateRecordVO;
import com.indo.admin.pojo.vo.agent.AgentSubVO;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.utils.StringUtils;
import com.indo.common.web.exception.BizException;
import com.indo.core.base.service.impl.SuperServiceImpl;
import com.indo.user.mapper.AgentRebateMapper;
import com.indo.user.mapper.AgentRebateRecordMapper;
import com.indo.user.mapper.MemAgentMapper;
import com.indo.user.pojo.entity.AgentRebate;
import com.indo.user.pojo.entity.MemAgent;
import com.indo.user.service.IAgentRebateRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
public class AgentRebateRecordServiceImpl extends SuperServiceImpl<AgentRebateRecordMapper, AgentRebateRecord> implements IAgentRebateRecordService {

    @Autowired
    private AgentRebateRecordMapper memRebateRecordMapper;
    @Autowired
    private AgentRebateMapper agentRebateMapper;
    @Autowired
    private MemAgentMapper memAgentMapper;

    @Override
    public AgentRebateInfoVO rebateInof(LoginInfo loginInfo) {
        AgentRebateInfoVO infoVO = new AgentRebateInfoVO();
        BigDecimal rebateAmount = new BigDecimal("0.00");
        LambdaQueryWrapper<AgentRebate> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AgentRebate::getMemId, loginInfo.getId());
        AgentRebate agentRebate = agentRebateMapper.selectOne(wrapper);
        if (null == agentRebate) {
            infoVO.setRebateAmount(rebateAmount);
        } else {
            infoVO.setRebateAmount(agentRebate.getRebateAmount());
        }
        return infoVO;
    }

    @Override
    public Page<AgentRebateRecordVO> queryList(AgentRebateRecordReq req, LoginInfo loginInfo) {
        req.setAccount(loginInfo.getAccount());
        Page<AgentRebateRecordVO> page = new Page<>(req.getPage(), req.getLimit());
        List<AgentRebateRecordVO> list = memRebateRecordMapper.queryList(page, req);
        page.setRecords(list);
        return page;
    }

    @Override
    public Page<AgentRebateRecordVO> subRebateList(AgentRebateRecordReq req, LoginInfo loginInfo) {
        LambdaQueryWrapper<MemAgent> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MemAgent::getSuperior, loginInfo.getAccount());
        Integer agentCount = memAgentMapper.selectCount(wrapper);
        if (agentCount < 1) {
            throw new BizException("你还没有下级");
        }
        wrapper.eq(MemAgent::getAccount, req.getAccount());
        agentCount = memAgentMapper.selectCount(wrapper);
        if (agentCount < 1) {
            throw new BizException("请输入正确的下级账号");
        }
        req.setAccount(loginInfo.getAccount());
        Page<AgentRebateRecordVO> page = new Page<>(req.getPage(), req.getLimit());
        List<AgentRebateRecordVO> list = memRebateRecordMapper.queryList(page, req);
        page.setRecords(list);
        return page;
    }
}
