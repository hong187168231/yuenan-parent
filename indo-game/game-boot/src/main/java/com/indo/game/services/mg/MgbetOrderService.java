package com.indo.game.services.mg;

import com.indo.game.pojo.entity.mg.MgBetOrder;

import java.util.List;

public interface MgbetOrderService {

    /**
     * 批量插入注单数据
     */

    void insertBatch(List<MgBetOrder> mgBetOrderLsit);

    List<MgBetOrder> queryMgOrderList();
}
