package com.indo.admin.modules.game.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.indo.admin.pojo.dto.game.manage.GamePlatformPageReq;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GamePlatformMapper extends BaseMapper<GamePlatform> {

    List<GamePlatform> queryAllGamePlatform(@Param("page") IPage<GamePlatform> page, @Param("req") GamePlatformPageReq req);
}
