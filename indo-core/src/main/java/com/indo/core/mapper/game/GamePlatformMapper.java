package com.indo.core.mapper.game;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.indo.core.pojo.entity.game.GamePlatform;
import com.indo.core.pojo.vo.game.app.GamePlatformRecord;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GamePlatformMapper extends BaseMapper<GamePlatform> {
    List<GamePlatformRecord> queryAllGamePlatform();
}
