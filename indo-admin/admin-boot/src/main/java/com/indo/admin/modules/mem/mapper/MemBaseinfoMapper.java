package com.indo.admin.modules.mem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.pojo.req.MemBaseInfoPageReq;
import com.indo.admin.pojo.vo.MemBaseInfoVo;
import com.indo.core.pojo.entity.MemBaseinfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 会员基础信息表 Mapper 接口
 * </p>
 *
 * @author kevin
 * @since 2021-10-23
 */
@Mapper
public interface MemBaseinfoMapper extends BaseMapper<MemBaseinfo> {

    List<MemBaseInfoVo> queryList(@Param("page") Page<MemBaseInfoVo> page,@Param("dto") MemBaseInfoPageReq dto);

    List<Long> findIdListByCreateTime(@Param("date") String date);
}
