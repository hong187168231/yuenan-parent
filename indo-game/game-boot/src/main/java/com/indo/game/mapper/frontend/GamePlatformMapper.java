package com.indo.game.mapper.frontend;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.indo.game.pojo.entity.manage.GamePlatform;
import com.indo.game.pojo.vo.app.GamePlatformRecord;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GamePlatformMapper extends BaseMapper<GamePlatform> {
    List<GamePlatformRecord> queryAllGamePlatform();
}
