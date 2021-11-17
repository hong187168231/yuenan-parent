package com.indo.game.mapper.mg;

import com.indo.game.pojo.dto.GameDailyDataDO;
import com.indo.game.pojo.dto.GameUserDataDO;
import com.indo.game.pojo.entity.mg.MgBetOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MgBetOrderMapperExt {

    Integer insertBatch(List<MgBetOrder> list);

    GameDailyDataDO statisticalDayData(@Param("startTime") String startTime, @Param("endTime") String endTime);

    List<GameUserDataDO> statisticalDataByUser(@Param("startTime") String startTime, @Param("endTime") String endTime);

    List<MgBetOrder> queryMgOrderList();
}