package com.indo.admin.modules.mem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.admin.modules.mem.entity.MemAgent;
import com.indo.admin.modules.mem.mapper.MemAgentMapper;
import com.indo.admin.modules.mem.mapper.MemBaseinfoMapper;
import com.indo.admin.modules.mem.req.MemAgentPageReq;
import com.indo.admin.modules.mem.service.IMemAgentService;
import com.indo.admin.modules.mem.vo.AgentVo;
import com.indo.admin.modules.mem.vo.MemBaseInfoVo;
import com.indo.admin.modules.mem.req.SubordinateReq;
import com.indo.common.web.util.DozerUtil;
import com.indo.user.pojo.entity.MemBaseinfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public Page<MemBaseInfoVo> subordinatePage(SubordinateReq req) {
        LambdaQueryWrapper<MemAgent> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MemAgent::getAgentId, req.getAgentId());
        MemAgent memAgent = memAgentMapper.selectOne(wrapper);

        String levels = memAgent.getLevelUserIds();
        String[] array = levels.split(",");
        List<String> strList = Arrays.asList(array);
        List<Long> ids = strList.stream().map(Long::valueOf).collect(Collectors.toList());

        LambdaQueryWrapper<MemBaseinfo> memWrapper = new LambdaQueryWrapper<>();
        memWrapper.in(MemBaseinfo::getId, ids);
        Page<MemBaseinfo> page = new Page<>(req.getPage(), req.getLimit());
        Page<MemBaseinfo> pageList = memBaseinfoMapper.selectPage(page, memWrapper);
        List<MemBaseInfoVo> result = dozerUtil.convert(pageList.getRecords(), MemBaseInfoVo.class);

        Page<MemBaseInfoVo> memBaseInfoVoPage = new Page<>();
        memBaseInfoVoPage.setRecords(result);
        memBaseInfoVoPage.setTotal(page.getTotal());
        return memBaseInfoVoPage;
    }

    @Override
    public boolean upgradeAgent(Long memId) {
        MemBaseinfo memBaseinfo = new MemBaseinfo();
        memBaseinfo.setAccType(2);
        return memBaseinfoMapper.updateById(memBaseinfo) > 0;
    }
}
