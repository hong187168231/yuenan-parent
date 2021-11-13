package com.indo.game.services.ky;

import com.indo.game.pojo.entity.ky.KyBetOrder;

import java.util.List;

public interface KybetOrderService {

    /**
     * 批量插入注单数据
     */

    void insertBatch(List<KyBetOrder> kyBetOrderLsit);

    List<KyBetOrder> queryKyOrderList();
}
