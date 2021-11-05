package com.indo.admin.modules.mem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.modules.mem.entity.MemBaseinfo;
import com.indo.admin.modules.mem.req.MemBaseInfoPageReq;
import com.indo.admin.modules.mem.vo.MemBaseInfoVo;
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

    List<Long> selectIdsByAccounts(List<String> accounts);

    List<String> selectNickNameByAccounts(List<String> receiver);

    List<MemBaseInfoVo> queryList(@Param("page") Page<MemBaseInfoVo> page,@Param("dto") MemBaseInfoPageReq dto);
}
