package com.indo.game.services.db.impl;



import com.indo.game.mapper.db.DbBetOrderMapperExt;
import com.indo.game.pojo.entity.db.DbBetOrder;
import com.indo.game.services.db.JdbBetOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JdbBetOrderServiceImpl implements JdbBetOrderService {

    @Autowired
    private DbBetOrderMapperExt jdbBetOrderMapperExt;


    @Override
    public void insertBatch(List<DbBetOrder> jdbBetOrderList) {
        jdbBetOrderMapperExt.insertBatch(jdbBetOrderList);
    }

    @Override
    public List<DbBetOrder> queryJdbOrderList() {
        return jdbBetOrderMapperExt.queryJdbOrderList();
    }
}
