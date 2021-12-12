package com.indo.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.common.result.PageResult;
import com.indo.user.mapper.MemAgentMapper;
import com.indo.user.pojo.entity.MemAgent;
import com.indo.user.pojo.entity.MemBankRelation;
import com.indo.user.pojo.req.mem.MemAgentStatReq;
import com.indo.user.pojo.req.mem.SubordinateReq;
import com.indo.user.pojo.vo.AgentStatVo;
import com.indo.user.pojo.vo.SubordinateVo;
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
 * @since 2021-12-12
 */
@Service
public class MemMemAgentServiceImpl extends ServiceImpl<MemAgentMapper, MemAgent> implements IMemAgentService {

    @Autowired
    private MemAgentMapper memAgentMapper;

    @Override
    public AgentStatVo agentStat(MemAgentStatReq req) {
        MemAgent memAgent = baseMapper.selectOne(new QueryWrapper<MemAgent>().lambda().eq(MemAgent::getMemId, req.getMemId()));
        AgentStatVo vo = new AgentStatVo();
        vo.setTeamNumber(memAgent.getTeamNum());
        return vo;
    }

    @Override
    public PageResult<MemAgent> subordinatePage(SubordinateReq req) {
        Integer pageNum = 1;
        Integer pageSize = 10;
        if (null != req.getPage() && null != req.getLimit()) {
            pageNum = req.getPage();
            pageSize = req.getLimit();
        }
        Page<MemAgent> page = new Page<>(pageNum, pageSize);
        List<MemAgent> list = memAgentMapper.queryList(page, req);
        page.setRecords(list);
        return PageResult.getPageResult(page);
    }
}
