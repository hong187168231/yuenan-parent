package com.indo.admin.modules.stat.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.admin.modules.stat.entity.StatAgentReport;
import com.indo.admin.modules.stat.mapper.StatAgentReportMapper;
import com.indo.admin.modules.stat.req.AgentReportReq;
import com.indo.admin.modules.stat.service.IStatAgentReportService;
import com.indo.admin.modules.stat.vo.UserReportVo;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author xxx
 * @since 2022-01-11
 */
@Service
public class StatAgentReportServiceImpl extends ServiceImpl<StatAgentReportMapper, StatAgentReport> implements IStatAgentReportService {

    @Autowired
    private StatAgentReportMapper statAgentReportMapper;

    @Override
    public Page<StatAgentReport> queryList(AgentReportReq req) {
        Page<StatAgentReport> page = new Page<>(req.getPage(), req.getLimit());
        List<StatAgentReport> list = statAgentReportMapper.queryList(page, req);
        page.setRecords(list);
        return page;
    }
}
