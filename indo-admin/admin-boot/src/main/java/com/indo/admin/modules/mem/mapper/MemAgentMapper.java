package com.indo.admin.modules.mem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.modules.mem.entity.MemAgent;
import com.indo.admin.modules.mem.req.MemAgentPageReq;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 会员下级表 Mapper 接口
 * </p>
 *
 * @author xxx
 * @since 2021-12-11
 */
@Mapper
public interface MemAgentMapper extends BaseMapper<MemAgent> {

    List<MemAgent> queryList(Page<MemAgent> page, MemAgentPageReq req);
}
