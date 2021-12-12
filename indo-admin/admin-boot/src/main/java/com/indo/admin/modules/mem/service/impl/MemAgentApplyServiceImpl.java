package com.indo.admin.modules.mem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.admin.modules.mem.entity.MemAgent;
import com.indo.admin.modules.mem.entity.MemAgentApply;
import com.indo.admin.modules.mem.entity.MemBaseinfo;
import com.indo.admin.modules.mem.entity.MemInviteCode;
import com.indo.admin.modules.mem.mapper.MemAgentApplyMapper;
import com.indo.admin.modules.mem.mapper.MemAgentMapper;
import com.indo.admin.modules.mem.mapper.MemBaseinfoMapper;
import com.indo.admin.modules.mem.mapper.MemInviteCodeMapper;
import com.indo.admin.modules.mem.req.MemAgentApplyPageReq;
import com.indo.admin.modules.mem.req.MemApplyAuditReq;
import com.indo.admin.modules.mem.service.IMemAgentApplyService;
import com.indo.admin.modules.mem.vo.MemBankRelationVO;
import com.indo.common.result.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 会员下级表 服务实现类
 * </p>
 *
 * @author xxx
 * @since 2021-11-19
 */
@Service
public class MemAgentApplyServiceImpl extends ServiceImpl<MemAgentApplyMapper, MemAgentApply> implements IMemAgentApplyService {

    @Autowired
    private MemAgentApplyMapper memAgentApplyMapper;
    @Autowired
    private MemBaseinfoMapper memBaseinfoMapper;
    @Autowired
    private MemAgentMapper memAgentMapper;
    @Autowired
    private MemInviteCodeMapper memInviteCodeMapper;

    @Override
    public PageResult<MemAgentApply> getPage(MemAgentApplyPageReq req) {
        Integer pageNum = 1;
        Integer pageSize = 10;
        if (null != req.getPage() && null != req.getLimit()) {
            pageNum = req.getPage();
            pageSize = req.getLimit();
        }
        Page<MemAgentApply> page = new Page<>(pageNum, pageSize);
        List<MemAgentApply> list = memAgentApplyMapper.queryList(page, req);
        page.setRecords(list);
        return PageResult.getPageResult(page);
    }

    @Override
    public void applyAudit(MemApplyAuditReq req) {
        MemAgentApply memAgentApply = new MemAgentApply();
        memAgentApply.setMemId(req.getMemId());
        memAgentApply.setStatus(req.getStatus());
        memAgentApply.setRejectReason(req.getRejectReason());
        baseMapper.updateById(memAgentApply);

        MemBaseinfo memBaseinfo = memBaseinfoMapper.selectById(req.getMemId());
        memBaseinfo.getRInviteCode();
        MemInviteCode memInviteCode = memInviteCodeMapper.selectOne(new QueryWrapper<MemInviteCode>().lambda().eq(MemInviteCode::getInviteCode, memBaseinfo.getRInviteCode()));
        MemAgent memAgent = new MemAgent();
        memAgent.setMemId(req.getMemId());
        memAgent.setIsDel(false);
        memAgent.setTeamNum(0);
        memAgent.setParentId(memInviteCode.getMemId());
        memAgentMapper.insert(memAgent);
    }
}
