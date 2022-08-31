package com.indo.admin.modules.agent.service.impl;

import cn.hutool.core.util.ObjectUtil;
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
import com.indo.common.constant.GlobalConstants;
import com.indo.common.enums.AudiTypeEnum;
import com.indo.common.enums.ProhibitStatusEnum;
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
 * 代理申请 服务实现类
 * </p>
 *
 * @author puff
 * @since 2021-11-19
 */
@Service
public class AgentApplyServiceImpl extends ServiceImpl<AgentApplyMapper, AgentApply> implements IAgentApplyService {

    @Autowired
    private AgentApplyMapper agentApplyMapper;
    @Autowired
    private AgentRelationMapper agentRelationMapper;
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
    public synchronized boolean applyAudit(MemApplyAuditReq req) {
        AgentApply memAgentApply = baseMapper.selectById(req.getAgentApplyId());
        if (ObjectUtil.isEmpty(memAgentApply)) {
            throw new BizException("代理审核申请不存在");
        }
        if (memAgentApply.getStatus().equals(GlobalConstants.PAY_CASH_STATUS_REJECT)) {
            throw new BizException("提现订单状态错误");
        }
        if (memAgentApply.getStatus().equals(GlobalConstants.PAY_CASH_STATUS_CANCEL)) {
            throw new BizException("提现订单状态错误");
        }
        MemBaseinfo memBaseinfo = iMemBaseinfoService.getMemBaseInfo(memAgentApply.getMemId());
        if (ProhibitStatusEnum.invite.getStatus().equals(memBaseinfo.getProhibitInvite())) {
            throw new BizException("该邀请人已被禁止发展下级");
        }
        if (req.getAudiType().name().equals(AudiTypeEnum.reject.name())) {
            //更新代理关系
            modifyAgentRelation(memBaseinfo);
            //插入会员邀请码
            iMemBaseinfoService.saveMemInviteCode(memBaseinfo);
            //更新会员代理状态
            modifyMemAccType(memBaseinfo);
        } else {
            // 记录拒绝原因
            if (StringUtils.isNotBlank(req.getRejectReason())) {
                memAgentApply.setRejectReason(req.getRejectReason());
            }
        }
        memAgentApply.setStatus(req.getAudiType().getStatus());
        return baseMapper.updateById(memAgentApply) > 0;
    }


    /**
     * 更新会员代理状态
     *
     * @param memBaseinfo
     */
    private void modifyMemAccType(MemBaseinfo memBaseinfo) {
        memBaseinfo.setAccType(GlobalConstants.ACC_TYPE_AGENT);
        boolean memFlag = iMemBaseinfoService.updateById(memBaseinfo);
        if (!memFlag) {
            throw new BizException("代理审核出错!");
        }
        // 刷新用户缓存
        MemBaseInfoDTO memBaseInfoDTO = new MemBaseInfoDTO();
        memBaseInfoDTO.setAccType(GlobalConstants.ACC_TYPE_AGENT);
        iMemBaseinfoService.refreshMemBaseInfo(memBaseInfoDTO, memBaseinfo.getAccount());
    }



    /**
     * 更新会员代理关系
     *
     * @param memBaseinfo
     */
    private void modifyAgentRelation(MemBaseinfo memBaseinfo) {
        LambdaQueryWrapper<AgentRelation> wa = new LambdaQueryWrapper<>();
        wa.eq(AgentRelation::getMemId, memBaseinfo.getId());
        AgentRelation memAgent = agentRelationMapper.selectOne(wa);
        boolean agentflag;
        if (ObjectUtil.isEmpty(memAgent)) {
            memAgent = new AgentRelation();
            memAgent.setMemId(memBaseinfo.getId());
            memAgent.setAccount(memBaseinfo.getAccount());
            memAgent.setStatus(GlobalConstants.STATUS_NORMAL);
            agentflag = agentRelationMapper.insert(memAgent) > 0;
        } else {
            memAgent.setStatus(GlobalConstants.STATUS_NORMAL);
            memAgent.setCreateTime(new Date());
            agentflag = agentRelationMapper.updateById(memAgent) > 0;
        }
        if (!agentflag) {
            throw new BizException("代理审核出错!");
        }
    }


}
