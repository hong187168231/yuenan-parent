package com.indo.game.mapper.frontend;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.indo.admin.pojo.dto.game.manage.GameParentPlatformPageReq;
import com.indo.game.pojo.entity.manage.GameParentPlatform;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GameParentPlatformMapper extends BaseMapper<GameParentPlatform> {
}
