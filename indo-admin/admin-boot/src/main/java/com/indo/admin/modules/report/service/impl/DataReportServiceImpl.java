package com.indo.admin.modules.report.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.modules.agent.mapper.AgentRelationMapper;
import com.indo.admin.modules.report.service.DataReportService;
import com.indo.admin.pojo.dto.AgentReportDTO;
import com.indo.admin.pojo.vo.agent.AgentReportVO;
import com.indo.common.utils.StringUtils;
import com.indo.common.web.exception.BizException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class DataReportServiceImpl implements DataReportService {
    @Resource
    private AgentRelationMapper memAgentMapper;
    @Override
    public Page<AgentReportVO> findAgentReport(AgentReportDTO agentReportDTO) {
        if(StringUtils.isEmpty(agentReportDTO.getBeginTime())||StringUtils.isEmpty(agentReportDTO.getEndTime())){
            throw new BizException("查询时间不可为空");
        }
        Page<AgentReportVO> page = new Page<>(agentReportDTO.getPage(), agentReportDTO.getLimit());
        return memAgentMapper.findAgentReport(page,agentReportDTO);
    }
}
