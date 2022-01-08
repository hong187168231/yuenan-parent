package com.indo.admin.modules.stat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.modules.stat.req.UserRetentionPageReq;
import com.indo.admin.modules.stat.vo.UserRetentionVo;
import com.indo.admin.pojo.entity.StatUserRetention;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author kevin
 * @since 2021-11-09
 */
@Mapper
public interface StatUserRetentionMapper extends BaseMapper<StatUserRetention> {

    List<UserRetentionVo> queryList( @Param("page")Page<UserRetentionVo> page, @Param("req") UserRetentionPageReq req);
}
