package com.indo.game.mapper.ky;

import com.indo.game.pojo.dto.GameDailyDataDO;
import com.indo.game.pojo.dto.GameUserDataDO;
import com.indo.game.pojo.entity.ky.KyBetOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface KyBetOrderMapperExt {
    Integer insertBatch(List<KyBetOrder> list);

    GameDailyDataDO statisticalDayData(@Param("startTime") String startTime, @Param("endTime") String endTime);

    List<GameUserDataDO> statisticalDataByUser(@Param("startTime") String startTime, @Param("endTime") String endTime);

    List<KyBetOrder> queryKyOrderList();
}