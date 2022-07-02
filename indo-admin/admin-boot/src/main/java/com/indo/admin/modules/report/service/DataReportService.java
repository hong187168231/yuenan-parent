package com.indo.admin.modules.report.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.pojo.dto.AgentReportDTO;
import com.indo.admin.pojo.dto.MemReportDTO;
import com.indo.admin.pojo.dto.PayRechargeReportDTO;
import com.indo.admin.pojo.vo.agent.AgentReportVo;
import com.indo.admin.pojo.vo.mem.MemReportVo;
import com.indo.admin.pojo.vo.pay.PayRechargeReportVo;

public interface DataReportService {
    /**
     * 查询代理报表
     * @param agentReportDTO
     * @return
     */
    Page<AgentReportVo> findAgentReport(AgentReportDTO agentReportDTO);

    /**
     * 查询会员报表
     * @param memReportDTO
     * @return
     */
    Page<MemReportVo> findMemberReport(MemReportDTO memReportDTO);

    /**
     *  查询充值报表
     * @param payRechargeReportDTO
     * @return
     */
    Page<PayRechargeReportVo> findPayRechargeReport(PayRechargeReportDTO payRechargeReportDTO);
}
