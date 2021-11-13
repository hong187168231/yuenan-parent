package com.indo.game.services.db;

import com.indo.game.pojo.entity.db.DbBetOrder;

import java.util.List;

public interface JdbBetOrderService {

    /**
     * 批量插入注单数据
     */

    void insertBatch(List<DbBetOrder> jdbBetOrderLsit);

    List<DbBetOrder> queryJdbOrderList();
}
