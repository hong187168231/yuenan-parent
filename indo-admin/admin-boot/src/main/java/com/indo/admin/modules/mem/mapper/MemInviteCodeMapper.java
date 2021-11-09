package com.indo.admin.modules.mem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.modules.mem.entity.MemInviteCode;
import com.indo.admin.modules.mem.req.MeminviteCodePageReq;
import com.indo.admin.modules.mem.vo.MemInviteCodeVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 会员邀请码 Mapper 接口
 * </p>
 *
 * @author kevin
 * @since 2021-11-05
 */
@Mapper
@Repository
public interface MemInviteCodeMapper extends BaseMapper<MemInviteCode> {

    List<MemInviteCodeVo> queryList(Page<MemInviteCodeVo> page, MeminviteCodePageReq req);
}