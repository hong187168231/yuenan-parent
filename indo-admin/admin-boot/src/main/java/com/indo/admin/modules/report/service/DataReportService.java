package com.indo.admin.modules.report.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.pojo.dto.AgentReportDTO;
import com.indo.admin.pojo.vo.agent.AgentReportVO;

public interface DataReportService {
    /**
     * 查询代理报表
     * @param agentReportDTO
     * @return
     */
    Page<AgentReportVO> findAgentReport(AgentReportDTO agentReportDTO);
}
