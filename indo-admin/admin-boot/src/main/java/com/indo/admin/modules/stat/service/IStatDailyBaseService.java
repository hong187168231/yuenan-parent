package com.indo.admin.modules.stat.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.admin.modules.stat.entity.StatDailyBase;
import com.indo.admin.modules.stat.req.TotalStatReq;
import com.indo.admin.modules.stat.req.UserReportReq;
import com.indo.admin.modules.stat.vo.UserReportVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xxx
 * @since 2022-01-11
 */
public interface IStatDailyBaseService extends IService<StatDailyBase> {

    Page<UserReportVo> queryList(UserReportReq req);

    StatDailyBase totalStat(TotalStatReq req);

}
