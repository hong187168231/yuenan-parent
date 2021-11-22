package com.indo.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.user.pojo.entity.MemNotice;
import com.indo.user.pojo.req.mem.MemNoticePageReq;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 会员站内信 Mapper 接口
 * </p>
 *
 * @author xxx
 * @since 2021-11-21
 */
@Mapper
public interface MemNoticeMapper extends BaseMapper<MemNotice> {

    List<MemNotice> queryList(Page<MemNotice> page, MemNoticePageReq req);
}
