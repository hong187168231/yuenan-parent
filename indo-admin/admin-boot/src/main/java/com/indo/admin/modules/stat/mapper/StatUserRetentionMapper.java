package com.indo.admin.modules.stat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.modules.stat.entity.StatUserRetention;
import com.indo.admin.modules.stat.req.UserRetentionPageReq;
import com.indo.admin.modules.stat.vo.UserRetentionVo;
import org.apache.ibatis.annotations.Mapper;

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

    List<UserRetentionVo> queryList(Page<UserRetentionVo> page, UserRetentionPageReq req);
}
