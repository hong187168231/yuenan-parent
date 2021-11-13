package com.indo.game.services.mg.impl;



import com.indo.game.mapper.mg.MgBetOrderMapperExt;
import com.indo.game.pojo.entity.mg.MgBetOrder;
import com.indo.game.services.mg.MgbetOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MgbetOrderServiceImpl implements MgbetOrderService {

    @Autowired
    private MgBetOrderMapperExt mgBetOrderMapperExt;


    @Override
    public void insertBatch(List<MgBetOrder> mgBetOrderList) {
        mgBetOrderMapperExt.insertBatch(mgBetOrderList);
    }

    @Override
    public List<MgBetOrder> queryMgOrderList() {
        return  mgBetOrderMapperExt.queryMgOrderList();
    }
}
