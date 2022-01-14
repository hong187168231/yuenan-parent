package com.indo.admin.modules.mem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.admin.modules.mem.mapper.MemAgentApplyMapper;
import com.indo.admin.modules.mem.mapper.MemAgentMapper;
import com.indo.admin.modules.mem.mapper.MemInviteCodeMapper;
import com.indo.admin.pojo.req.MemAgentApplyPageReq;
import com.indo.admin.pojo.req.MemApplyAuditReq;
import com.indo.admin.modules.mem.service.IMemAgentApplyService;
import com.indo.admin.pojo.vo.agent.AgentApplyVO;
import com.indo.common.enums.AudiTypeEnum;
import com.indo.common.utils.ShareCodeUtil;
import com.indo.common.utils.StringUtils;
import com.indo.common.web.exception.BizException;
import com.indo.core.pojo.entity.MemAgent;
import com.indo.core.pojo.entity.MemAgentApply;
import com.indo.core.pojo.entity.MemInviteCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private MemAgentMapper memAgent1Mapper;
    @Autowired
    private MemInviteCodeMapper memInviteCodeMapper;


    @Override
    public Page<AgentApplyVO> getPage(MemAgentApplyPageReq req) {
        Page<AgentApplyVO> page = new Page<>(req.getPage(), req.getLimit());
        List<AgentApplyVO> list = memAgentApplyMapper.queryList(page, req);
        page.setRecords(list);
        return page;
    }

    @Override
    @Transactional
    public boolean applyAudit(MemApplyAuditReq req) {
        MemAgentApply memAgentApply = baseMapper.selectById(req.getAgentApplyId());
        if (memAgentApply == null) {
            throw new BizException("代理审核申请不存在");
        }
        memAgentApply.setStatus(req.getAudiType().getStatus());
        if (req.getAudiType().name().equals(AudiTypeEnum.reject)) {
            if (StringUtils.isNotBlank(req.getRejectReason())) {
                memAgentApply.setRejectReason(req.getRejectReason());
            }
        }
        if (baseMapper.updateById(memAgentApply) > 0) {
            if (req.getAudiType().name().equals(AudiTypeEnum.agree)) {
                LambdaQueryWrapper<MemAgent> wa = new LambdaQueryWrapper<>();
                wa.eq(MemAgent::getMemId, req.getMemId())
                        .eq(MemAgent::getIsDel, true);
                MemAgent memAgent = memAgent1Mapper.selectOne(wa);
                boolean agentflag;
                if (memAgent == null) {
                    memAgent = new MemAgent();
                    memAgent.setMemId(memAgentApply.getMemId());
                    memAgent.setIsDel(false);
                    agentflag = memAgent1Mapper.insert(memAgent) > 0;
                } else {
                    memAgent.setIsDel(false);
                    agentflag = memAgent1Mapper.updateById(memAgent) > 0;
                }
                MemInviteCode memInviteCode = new MemInviteCode();
                String code = ShareCodeUtil.inviteCode(memAgent.getMemId());
                memInviteCode.setMemId(memAgent.getMemId());
                memInviteCode.setInviteCode(code.toLowerCase());
                boolean inviteFlag = memInviteCodeMapper.insert(memInviteCode) > 0;
                if (!agentflag || !inviteFlag) {
                    throw new BizException("代理审核出错!");
                }
                return true;
            }
            return true;
        }
        return false;
    }
}
