package com.indo.admin.modules.stat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.modules.stat.entity.StatDailyBase;
import com.indo.admin.modules.stat.req.TotalStatReq;
import com.indo.admin.modules.stat.req.UserReportReq;
import com.indo.admin.modules.stat.vo.UserReportVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author xxx
 * @since 2022-01-11
 */
@Mapper
public interface StatDailyBaseMapper extends BaseMapper<StatDailyBase> {

    List<UserReportVo> queryList(@Param("page") Page<UserReportVo> page, @Param("req") UserReportReq req);

    StatDailyBase queryTotal(@Param("req") TotalStatReq req);
}
