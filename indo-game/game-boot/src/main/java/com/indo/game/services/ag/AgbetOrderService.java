package com.indo.game.services.ag;

import com.indo.game.pojo.entity.ag.AgBetOrder;
import com.indo.game.pojo.entity.ag.AgFishBetOrder;

import java.util.List;

public interface AgbetOrderService {

    /**
     * 批量插入注单数据
     */
    void insertFishBatch(List<AgFishBetOrder> betOrderList);

    void insertBatch(List<AgBetOrder> agBetOrderList);

    List<AgFishBetOrder> batchFishOrderList();

    List<AgBetOrder> batchOrderBetList();
}
