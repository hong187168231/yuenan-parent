package com.indo.admin.modules.stat.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.admin.pojo.entity.StatPlatReport;
import com.indo.admin.modules.stat.req.PlatReportReq;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author xxx
 * @since 2022-01-11
 */
public interface IStatPlatReportService extends IService<StatPlatReport> {

    Page<StatPlatReport> queryList(PlatReportReq req);
}
