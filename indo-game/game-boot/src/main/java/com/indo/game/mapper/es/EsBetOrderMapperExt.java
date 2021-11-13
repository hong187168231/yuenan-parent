package com.indo.game.mapper.es;

import com.indo.game.pojo.dto.GameDailyDataDO;
import com.indo.game.pojo.dto.GameUserDataDO;
import com.indo.game.pojo.entity.es.EsBetOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EsBetOrderMapperExt {

    Integer insertBatch(List<EsBetOrder> list);

    GameDailyDataDO statisticalDayData(@Param("startTime") String startTime, @Param("endTime") String endTime);


    List<GameUserDataDO> statisticalDataByUser(@Param("startTime") String startTime, @Param("endTime") String endTime);
}
