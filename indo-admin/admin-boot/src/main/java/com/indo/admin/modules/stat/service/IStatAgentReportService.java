package com.indo.admin.modules.stat.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.admin.modules.stat.entity.StatAgentReport;
import com.indo.admin.modules.stat.req.AgentReportReq;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xxx
 * @since 2022-01-11
 */
public interface IStatAgentReportService extends IService<StatAgentReport> {

    Page<StatAgentReport> queryList(AgentReportReq req);
}
