package com.indo.admin.modules.stat.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.admin.pojo.entity.StatDailyBase;
import com.indo.admin.modules.stat.mapper.StatDailyBaseMapper;
import com.indo.admin.modules.stat.req.TotalStatReq;
import com.indo.admin.modules.stat.req.UserReportReq;
import com.indo.admin.modules.stat.service.IStatDailyBaseService;
import com.indo.admin.modules.stat.vo.UserReportVo;
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
public class StatDailyBaseServiceImpl extends ServiceImpl<StatDailyBaseMapper, StatDailyBase> implements IStatDailyBaseService {

    @Autowired
    private StatDailyBaseMapper statDailyBaseMapper;

    @Override
    public Page<UserReportVo> queryList(UserReportReq req) {
        Page<UserReportVo> page = new Page<>(req.getPage(), req.getLimit());
        List<UserReportVo> list = statDailyBaseMapper.queryList(page, req);
        page.setRecords(list);
        return page;
    }

    @Override
    public StatDailyBase totalStat(TotalStatReq req) {

        return statDailyBaseMapper.queryTotal(req);
    }
}
