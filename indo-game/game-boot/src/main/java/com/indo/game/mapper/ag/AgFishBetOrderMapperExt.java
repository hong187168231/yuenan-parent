package com.indo.game.mapper.ag;

import com.indo.game.pojo.dto.GameDailyDataDO;
import com.indo.game.pojo.dto.GameUserDataDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AgFishBetOrderMapperExt {
    GameDailyDataDO statisticalDayData(@Param("startTime") String startTime, @Param("endTime") String endTime);

    List<GameUserDataDO> statisticalDataByUser(@Param("startTime") String startTime, @Param("endTime") String endTime);
}