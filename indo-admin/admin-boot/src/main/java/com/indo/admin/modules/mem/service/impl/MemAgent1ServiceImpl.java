package com.indo.admin.modules.mem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.admin.modules.mem.mapper.MemAgentMapper;
import com.indo.admin.modules.mem.mapper.MemBaseinfoMapper;
import com.indo.admin.modules.mem.req.MemAgentPageReq;
import com.indo.admin.modules.mem.service.IMemAgentService;
import com.indo.admin.modules.mem.vo.MemBaseInfoVo;
import com.indo.admin.modules.mem.req.SubordinateReq;
import com.indo.admin.pojo.entity.MemAgent;
import com.indo.admin.pojo.vo.agent.AgentSubVO;
import com.indo.admin.pojo.vo.agent.AgentVo;
import com.indo.common.web.exception.BizException;
import com.indo.common.web.util.DozerUtil;
import com.indo.user.pojo.entity.MemBaseinfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 会员下级表 服务实现类
 * </p>
 *
 * @author xxx
 * @since 2021-12-11
 */
@Service
public class MemAgent1ServiceImpl extends ServiceImpl<MemAgentMapper, MemAgent> implements IMemAgentService {

    @Autowired
    private MemAgentMapper memAgentMapper;
    @Autowired
    private MemBaseinfoMapper memBaseinfoMapper;
    @Autowired
    private DozerUtil dozerUtil;

    @Override
    public Page<AgentVo> getPage(MemAgentPageReq req) {
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
