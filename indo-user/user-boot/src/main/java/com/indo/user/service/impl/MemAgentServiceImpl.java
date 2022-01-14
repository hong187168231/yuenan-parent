package com.indo.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.pojo.vo.agent.AgentSubVO;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.web.exception.BizException;
import com.indo.core.base.service.impl.SuperServiceImpl;
import com.indo.core.pojo.entity.MemAgent;
import com.indo.core.pojo.entity.MemAgentApply;
import com.indo.user.mapper.MemAgentApplyMapper;
import com.indo.user.mapper.MemAgentMapper;
import com.indo.user.pojo.req.mem.MemAgentApplyReq;
import com.indo.user.pojo.req.mem.SubordinateAppReq;
import com.indo.user.service.IMemAgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 会员下级表 服务实现类
 * </p>
 *
 * @author xxx
 * @since 2021-12-11
 */
@Service
public class MemAgentServiceImpl extends SuperServiceImpl<MemAgentMapper,MemAgent> implements IMemAgentService {


    @Autowired
    private MemAgentApplyMapper memAgentApplyMapper;


    @Autowired
    private MemAgentMapper memAgentMapper;


    @Override
    public boolean apply(MemAgentApplyReq req, LoginInfo loginInfo) {
        MemAgentApply memAgentApply = new MemAgentApply();
        memAgentApply.setMemId(loginInfo.getId());
        memAgentApply.setStatus(0);
        return memAgentApplyMapper.insert(memAgentApply) > 0;
    }

    @Override
    public Page<AgentSubVO> subordinatePage(SubordinateAppReq req, LoginInfo loginInfo) {
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
        Page<AgentSubVO> page = new Page<>(req.getPage(), req.getLimit());
        List<AgentSubVO> agentList = memAgentMapper.subordinateList(page, req);
        page.setRecords(agentList);
        return page;
    }
}
