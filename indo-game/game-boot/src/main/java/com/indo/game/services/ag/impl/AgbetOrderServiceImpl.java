package com.indo.game.services.ag.impl;


import com.indo.game.mapper.ag.AgBetOrderMapperExt;
import com.indo.game.pojo.entity.ag.AgBetOrder;
import com.indo.game.pojo.entity.ag.AgFishBetOrder;
import com.indo.game.services.ag.AgbetOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgbetOrderServiceImpl implements AgbetOrderService {

    @Autowired
    private AgBetOrderMapperExt agBetOrderMapperExt;

    @Override
    public void insertFishBatch(List<AgFishBetOrder> betOrderList) {
        agBetOrderMapperExt.insertFishBatch(betOrderList);
    }

    @Override
    public void insertBatch(List<AgBetOrder> agBetOrderList) {
        agBetOrderMapperExt.insertBatch(agBetOrderList);
    }

    @Override
    public List<AgFishBetOrder> batchFishOrderList() {
        return agBetOrderMapperExt.batchFishOrderList();
    }

    @Override
    public List<AgBetOrder> batchOrderBetList() {
        return agBetOrderMapperExt.batchOrderBetList();
    }
}
