package com.indo.admin.modules.report.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.modules.agent.mapper.AgentRelationMapper;
import com.indo.admin.modules.game.mapper.AdminGameParentPlatformMapper;
import com.indo.admin.modules.mem.mapper.MemBankMapper;
import com.indo.admin.modules.pay.mapper.PayRechargeMapper;
import com.indo.admin.modules.report.service.DataReportService;
import com.indo.admin.pojo.dto.*;
import com.indo.admin.pojo.vo.TotalReportVo;
import com.indo.admin.pojo.vo.agent.AgentReportVo;
import com.indo.admin.pojo.vo.game.PlatformReportVo;
import com.indo.admin.pojo.vo.mem.MemReportVo;
import com.indo.admin.pojo.vo.pay.PayRechargeReportVo;
import com.indo.common.result.ResultCode;
import com.indo.common.utils.StringUtils;
import com.indo.common.utils.i18n.MessageUtils;
import com.indo.common.web.exception.BizException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Service
public class DataReportServiceImpl implements DataReportService {
    @Resource
    private AgentRelationMapper agentRelationMapper;
    @Resource
    private MemBankMapper memBankMapper;
    @Resource
    private PayRechargeMapper payRechargeMapper;
    @Resource
    private AdminGameParentPlatformMapper adminGameParentPlatformMapper;
    @Override
    public Page<AgentReportVo> findAgentReportPage(AgentReportDTO agentReportDTO, HttpServletRequest request) {
        if(StringUtils.isEmpty(agentReportDTO.getBeginTime())||StringUtils.isEmpty(agentReportDTO.getEndTime())){
            String countryCode = request.getHeader("countryCode");
            throw new BizException(MessageUtils.get(ResultCode.SYSPARAMETER_EMPTY.getCode(),countryCode));
        }
        Page<AgentReportVo> page = new Page<>(agentReportDTO.getPage(), agentReportDTO.getLimit());
        return agentRelationMapper.findAgentReport(page,agentReportDTO);
    }

    @Override
    public Page<MemReportVo> findMemberReportPage(MemReportDTO memReportDTO, HttpServletRequest request) {
        if(StringUtils.isEmpty(memReportDTO.getBeginTime())||StringUtils.isEmpty(memReportDTO.getEndTime())){
            String countryCode = request.getHeader("countryCode");
            throw new BizException(MessageUtils.get(ResultCode.SYSPARAMETER_EMPTY.getCode(),countryCode));
        }
        Page<MemReportVo> page = new Page<>(memReportDTO.getPage(), memReportDTO.getLimit());
        return memBankMapper.findMemberReport(page,memReportDTO);
    }

    @Override
    public Page<PayRechargeReportVo> findPayRechargeReportPage(PayRechargeReportDTO payRechargeReportDTO, HttpServletRequest request) {
        if(StringUtils.isEmpty(payRechargeReportDTO.getBeginTime())||StringUtils.isEmpty(payRechargeReportDTO.getEndTime())){
            String countryCode = request.getHeader("countryCode");
            throw new BizException(MessageUtils.get(ResultCode.SYSPARAMETER_EMPTY.getCode(),countryCode));
        }
        Page<PayRechargeReportVo> page = new Page<>(payRechargeReportDTO.getPage(), payRechargeReportDTO.getLimit());
        return payRechargeMapper.findPayRechargeReport(page,payRechargeReportDTO);
    }

    @Override
    public Page<PlatformReportVo> findPlatformReportPage(PlatformReportDTO platformReportDTO, HttpServletRequest request) {
        if(StringUtils.isEmpty(platformReportDTO.getBeginTime())||StringUtils.isEmpty(platformReportDTO.getEndTime())){
            String countryCode = request.getHeader("countryCode");
            throw new BizException(MessageUtils.get(ResultCode.SYSPARAMETER_EMPTY.getCode(),countryCode));
        }
        Page<PlatformReportVo> page = new Page<>(platformReportDTO.getPage(), platformReportDTO.getLimit());
        return adminGameParentPlatformMapper.findPlatformReport(page,platformReportDTO);
    }

    @Override
    public TotalReportVo findTotalReport(TotalReportDTO totalReportDTO, HttpServletRequest request) {
        if(StringUtils.isEmpty(totalReportDTO.getBeginTime())||StringUtils.isEmpty(totalReportDTO.getEndTime())){
            String countryCode = request.getHeader("countryCode");
            throw new BizException(MessageUtils.get(ResultCode.SYSPARAMETER_EMPTY.getCode(),countryCode));
        }
        return payRechargeMapper.findTotalReport(totalReportDTO);
    }

}
