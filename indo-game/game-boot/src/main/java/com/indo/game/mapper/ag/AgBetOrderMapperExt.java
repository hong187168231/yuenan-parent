package com.indo.game.mapper.ag;

import com.indo.game.pojo.dto.GameDailyDataDO;
import com.indo.game.pojo.dto.GameUserDataDO;
import com.indo.game.pojo.entity.ag.AgBetOrder;
import com.indo.game.pojo.entity.ag.AgFishBetOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AgBetOrderMapperExt {

    int insertBatch(List<AgBetOrder> list);

    int insertFishBatch(List<AgFishBetOrder> betOrderList);

    GameDailyDataDO statisticalDayData(@Param("startTime") String startTime, @Param("endTime") String endTime);

    List<GameUserDataDO> statisticalDataByUser(@Param("startTime") String startTime, @Param("endTime") String endTime);

    List<AgFishBetOrder> batchFishOrderList();

    List<AgBetOrder> batchOrderBetList();
}