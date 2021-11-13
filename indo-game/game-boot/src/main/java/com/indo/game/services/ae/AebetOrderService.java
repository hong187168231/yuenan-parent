package com.indo.game.services.ae;

import com.indo.game.pojo.entity.ae.AeBetOrder;

import java.util.List;

public interface AebetOrderService {

    /**
     * 批量插入注单数据
     */
    void insertBatch(List<AeBetOrder> aeBetOrderList);

    List<AeBetOrder> queryAeList();
}
