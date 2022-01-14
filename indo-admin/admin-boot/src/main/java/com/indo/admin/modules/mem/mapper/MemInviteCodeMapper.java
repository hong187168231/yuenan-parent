package com.indo.admin.modules.mem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.pojo.req.MeminviteCodePageReq;
import com.indo.admin.pojo.vo.MemInviteCodeVo;
import com.indo.core.pojo.entity.MemInviteCode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
public interface MemInviteCodeMapper extends BaseMapper<MemInviteCode> {

    List<MemInviteCodeVo> queryList(@Param("page") Page<MemInviteCodeVo> page, @Param("req") MeminviteCodePageReq req);
}
