package com.indo.game.mapper.frontend;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.indo.game.pojo.entity.manage.GameType;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 游戏种类字典表 Mapper 接口
 * </p>
 *
 * @author xxx
 * @since 2021-11-19
 */
@Mapper
public interface GameTypeMapper extends BaseMapper<GameType> {

}
