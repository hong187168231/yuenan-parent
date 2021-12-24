package com.indo.admin.modules.mem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.indo.admin.modules.mem.entity.MemNotice;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 会员站内信 Mapper 接口
 * </p>
 *
 * @author kevin
 * @since 2021-11-02
 */
@Mapper
@Repository
public interface MemNoticeMapper extends BaseMapper<MemNotice> {

}
