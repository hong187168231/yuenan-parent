package com.indo.admin.modules.agent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.admin.modules.agent.mapper.AgentApplyMapper;
import com.indo.admin.modules.agent.mapper.AgentRelationMapper;
import com.indo.admin.modules.agent.service.IAgentApplyService;
import com.indo.admin.modules.mem.mapper.MemInviteCodeMapper;
import com.indo.admin.modules.mem.service.IMemBaseinfoService;
import com.indo.admin.pojo.req.agnet.MemAgentApplyReq;
import com.indo.admin.pojo.req.agnet.MemApplyAuditReq;
import com.indo.admin.pojo.vo.agent.AgentApplyVO;
import com.indo.common.enums.AudiTypeEnum;
import com.indo.common.utils.ShareCodeUtil;
import com.indo.common.utils.StringUtils;
import com.indo.common.web.exception.BizException;
import com.indo.core.pojo.bo.MemBaseInfoBO;
import com.indo.core.pojo.dto.MemBaseInfoDTO;
import com.indo.core.pojo.entity.AgentApply;
import com.indo.core.pojo.entity.AgentRelation;
import com.indo.core.pojo.entity.MemBaseinfo;
import com.indo.core.pojo.entity.MemInviteCode;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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
public class AgentApplyServiceImpl extends ServiceImpl<AgentApplyMapper, AgentApply> implements IAgentApplyService {

    @Autowired
    private AgentApplyMapper agentApplyMapper;
    @Autowired
    private AgentRelationMapper agentRelationMapper;
    @Autowired
    private MemInviteCodeMapper memInviteCodeMapper;
    @Autowired
    private IMemBaseinfoService iMemBaseinfoService;

    @Override
    public Page<AgentApplyVO> getPage(MemAgentApplyReq req) {
        Page<AgentApplyVO> page = new Page<>(req.getPage(), req.getLimit());
        List<AgentApplyVO> list = agentApplyMapper.queryList(page, req);
        page.setRecords(list);
        return page;
    }

    @Override
    @Transactional
    public boolean applyAudit(MemApplyAuditReq req) {
        AgentApply memAgentApply = baseMapper.selectById(req.getAgentApplyId());
        if (memAgentApply == null) {
            throw new BizException("代理审核申请不存在");
        }
        MemBaseinfo superiorMem = iMemBaseinfoService.getMemBaseInfo(req.getMemId());
        if (superiorMem.getProhibitInvite().equals(1)) {
            throw new BizException("该邀请人已被禁止发展下级");
        }
        memAgentApply.setStatus(req.getAudiType().getStatus());
        if (req.getAudiType().name().equals(AudiTypeEnum.reject)) {
            if (StringUtils.isNotBlank(req.getRejectReason())) {
                memAgentApply.setRejectReason(req.getRejectReason());
            }
        }
        if (baseMapper.updateById(memAgentApply) > 0) {
            if (req.getAudiType().name().equals(AudiTypeEnum.agree)) {
                LambdaQueryWrapper<AgentRelation> wa = new LambdaQueryWrapper<>();
                wa.eq(AgentRelation::getMemId, req.getMemId())
                        .eq(AgentRelation::getStatus, 0);
                AgentRelation memAgent = agentRelationMapper.selectOne(wa);
                boolean agentflag;
                if (memAgent == null) {
                    memAgent = new AgentRelation();
                    memAgent.setMemId(memAgentApply.getMemId());
                    memAgent.setStatus(1);
                    agentflag = agentRelationMapper.insert(memAgent) > 0;
                } else {
                    memAgent.setStatus(1);
                    memAgent.setCreateTime(new Date());
                    agentflag = agentRelationMapper.updateById(memAgent) > 0;
                }
                MemInviteCode memInviteCode = new MemInviteCode();
                String code = ShareCodeUtil.inviteCode(memAgent.getMemId());
                memInviteCode.setMemId(memAgent.getMemId());
                memInviteCode.setInviteCode(code.toLowerCase());
                boolean inviteFlag = memInviteCodeMapper.insert(memInviteCode) > 0;
                if (!agentflag || !inviteFlag) {
                    throw new BizException("代理审核出错!");
                }

                MemBaseInfoDTO memBaseInfoDTO = new MemBaseInfoDTO();
                memBaseInfoDTO.setAccType(AudiTypeEnum.agree.getStatus());
                iMemBaseinfoService.refreshMemBaseInfo(memBaseInfoDTO, memAgent.getAccount());
                return true;
            }
            return true;
        }
        return false;
    }
}
