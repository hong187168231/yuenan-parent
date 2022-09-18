package com.indo.admin.modules.report.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.pojo.dto.*;
import com.indo.admin.pojo.vo.TotalReportVo;
import com.indo.admin.pojo.vo.agent.AgentReportVo;
import com.indo.admin.pojo.vo.game.PlatformReportVo;
import com.indo.admin.pojo.vo.mem.MemReportVo;
import com.indo.admin.pojo.vo.pay.PayRechargeReportVo;

import javax.servlet.http.HttpServletRequest;

public interface DataReportService {
    /**
     * 查询代理报表
     * @param agentReportDTO
     * @return
     */
    Page<AgentReportVo> findAgentReportPage(AgentReportDTO agentReportDTO, HttpServletRequest request);

    /**
     * 查询会员报表
     * @param memReportDTO
     * @return
     */
    Page<MemReportVo> findMemberReportPage(MemReportDTO memReportDTO, HttpServletRequest request);

    /**
     *  查询充值报表
     * @param payRechargeReportDTO
     * @return
     */
    Page<PayRechargeReportVo> findPayRechargeReportPage(PayRechargeReportDTO payRechargeReportDTO, HttpServletRequest request);

    /**
     * 查询平台报表
     * @param platformReportDTO
     * @return
     */
    Page<PlatformReportVo> findPlatformReportPage(PlatformReportDTO platformReportDTO, HttpServletRequest request);

    /**
     * 查询总报表
     * @param totalReportDTO
     * @return
     */
    TotalReportVo findTotalReport(TotalReportDTO totalReportDTO, HttpServletRequest request);
}
