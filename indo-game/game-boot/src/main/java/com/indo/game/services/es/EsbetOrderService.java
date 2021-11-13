package com.indo.game.services.es;

import com.indo.game.pojo.entity.es.EsBetOrder;

import java.util.List;

public interface EsbetOrderService {

    /**
     * 批量插入注单数据
     */

    void insertBatch(List<EsBetOrder> esBetOrderList);
}
