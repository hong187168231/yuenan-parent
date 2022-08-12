package com.indo.admin.modules.game.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.pojo.dto.PlatformReportDTO;
import com.indo.admin.pojo.dto.game.manage.GameParentPlatformPageReq;
import com.indo.admin.pojo.vo.game.PlatformReportVo;
import com.indo.core.pojo.entity.game.GameParentPlatform;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GameParentPlatformMapper extends BaseMapper<GameParentPlatform> {
    List<GameParentPlatform> queryAllGameParentPlatform(@Param("page") IPage<GameParentPlatform> page, @Param("req") GameParentPlatformPageReq req);

    /**
     * 查询平台报表
     * @param page
     * @param platformReportDTO
     * @return
     */
    Page<PlatformReportVo> findPlatformReport(@Param("page")Page<PlatformReportVo> page,@Param("dto") PlatformReportDTO platformReportDTO);
}
