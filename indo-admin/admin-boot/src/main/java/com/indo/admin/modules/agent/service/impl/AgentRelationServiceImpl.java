package com.indo.admin.modules.agent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.admin.modules.agent.mapper.AgentRelationMapper;
import com.indo.admin.modules.agent.service.IAgentRelationService;
import com.indo.admin.modules.mem.mapper.MemBaseinfoMapper;
import com.indo.admin.pojo.req.agnet.MemAgentReq;
import com.indo.admin.pojo.req.agnet.SubordinateReq;
import com.indo.admin.pojo.vo.agent.AgentSubVO;
import com.indo.admin.pojo.vo.agent.AgentVo;
import com.indo.common.web.exception.BizException;
import com.indo.core.pojo.entity.AgentRelation;
import com.indo.core.pojo.entity.MemBaseinfo;
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
public class AgentRelationServiceImpl extends ServiceImpl<AgentRelationMapper, AgentRelation> implements IAgentRelationService {

    @Autowired
    private AgentRelationMapper memAgentMapper;
    @Autowired
    private MemBaseinfoMapper memBaseinfoMapper;

    @Override
    public Page<AgentVo> getPage(MemAgentReq req) {
        Page<AgentVo> page = new Page<>(req.getPage(), req.getLimit());
        List<AgentVo> list = memAgentMapper.queryList(page, req);
        page.setRecords(list);
        return page;
    }

    @Override
    public Page<AgentSubVO> subordinatePage(SubordinateReq req) {
        Page<AgentSubVO> page = new Page<>(req.getPage(), req.getLimit());
        List<AgentSubVO> agentList = memAgentMapper.subordinateList(page, req);
        page.setRecords(agentList);
        return page;
    }

    @Override
    public boolean upgradeAgent(String account) {
        LambdaQueryWrapper<MemBaseinfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MemBaseinfo::getAccount, account);
        MemBaseinfo memBaseinfo = memBaseinfoMapper.selectOne(wrapper);
        if (null == memBaseinfo) {
            throw new BizException("该账号用户不存在");
        }
        memBaseinfo.setAccType(2);
        return memBaseinfoMapper.updateById(memBaseinfo) > 0;
    }
}
