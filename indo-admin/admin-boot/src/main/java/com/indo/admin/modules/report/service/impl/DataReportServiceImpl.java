package com.indo.admin.modules.report.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.modules.agent.mapper.AgentRelationMapper;
import com.indo.admin.modules.mem.mapper.MemBankMapper;
import com.indo.admin.modules.pay.mapper.PayRechargeMapper;
import com.indo.admin.modules.report.service.DataReportService;
import com.indo.admin.pojo.dto.AgentReportDTO;
import com.indo.admin.pojo.dto.MemReportDTO;
import com.indo.admin.pojo.dto.PayRechargeReportDTO;
import com.indo.admin.pojo.vo.agent.AgentReportVo;
import com.indo.admin.pojo.vo.mem.MemReportVo;
import com.indo.admin.pojo.vo.pay.PayRechargeReportVo;
import com.indo.common.utils.StringUtils;
import com.indo.common.web.exception.BizException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class DataReportServiceImpl implements DataReportService {
    @Resource
    private AgentRelationMapper agentRelationMapper;
    @Resource
    private MemBankMapper memBankMapper;
    @Resource
    private PayRechargeMapper payRechargeMapper;
    @Override
    public Page<AgentReportVo> findAgentReport(AgentReportDTO agentReportDTO) {
        if(StringUtils.isEmpty(agentReportDTO.getBeginTime())||StringUtils.isEmpty(agentReportDTO.getEndTime())){
            throw new BizException("查询时间不可为空");
        }
        Page<AgentReportVo> page = new Page<>(agentReportDTO.getPage(), agentReportDTO.getLimit());
        return agentRelationMapper.findAgentReport(page,agentReportDTO);
    }

    @Override
    public Page<MemReportVo> findMemberReport(MemReportDTO memReportDTO) {
        if(StringUtils.isEmpty(memReportDTO.getBeginTime())||StringUtils.isEmpty(memReportDTO.getEndTime())){
            throw new BizException("查询时间不可为空");
        }
        Page<MemReportVo> page = new Page<>(memReportDTO.getPage(), memReportDTO.getLimit());
        return memBankMapper.findMemberReport(page,memReportDTO);
    }

    @Override
    public Page<PayRechargeReportVo> findPayRechargeReport(PayRechargeReportDTO payRechargeReportDTO) {
        if(StringUtils.isEmpty(payRechargeReportDTO.getBeginTime())||StringUtils.isEmpty(payRechargeReportDTO.getEndTime())){
            throw new BizException("查询时间不可为空");
        }
        Page<PayRechargeReportVo> page = new Page<>(payRechargeReportDTO.getPage(), payRechargeReportDTO.getLimit());
        return payRechargeMapper.findPayRechargeReport(page,payRechargeReportDTO);
    }

}
