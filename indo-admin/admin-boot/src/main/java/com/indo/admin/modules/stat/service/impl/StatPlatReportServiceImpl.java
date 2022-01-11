package com.indo.admin.modules.stat.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.admin.modules.stat.entity.StatPlatReport;
import com.indo.admin.modules.stat.mapper.StatPlatReportMapper;
import com.indo.admin.modules.stat.req.PlatReportReq;
import com.indo.admin.modules.stat.service.IStatPlatReportService;
import com.indo.admin.modules.stat.vo.UserReportVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xxx
 * @since 2022-01-11
 */
@Service
public class StatPlatReportServiceImpl extends ServiceImpl<StatPlatReportMapper, StatPlatReport> implements IStatPlatReportService {

    @Autowired
    private StatPlatReportMapper statPlatReportMapper;

    @Override
    public Page<StatPlatReport> queryList(PlatReportReq req) {
        Page<StatPlatReport> page = new Page<>(req.getPage(), req.getLimit());
        List<StatPlatReport> list = statPlatReportMapper.queryList(page, req);
        page.setRecords(list);
        return page;
    }
}
