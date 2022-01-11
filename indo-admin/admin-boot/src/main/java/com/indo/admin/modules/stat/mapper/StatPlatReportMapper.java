package com.indo.admin.modules.stat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.modules.stat.entity.StatPlatReport;
import com.indo.admin.modules.stat.req.PlatReportReq;
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
public interface StatPlatReportMapper extends BaseMapper<StatPlatReport> {

    List<StatPlatReport> queryList(@Param("page") Page<StatPlatReport> page, @Param("req") PlatReportReq req);
}
