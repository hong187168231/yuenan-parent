package com.indo.game.services.ae.impl;


import com.indo.game.mapper.ae.AeBetOrderMapperExt;
import com.indo.game.pojo.entity.ae.AeBetOrder;
import com.indo.game.services.ae.AebetOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AebetOrderServiceImpl implements AebetOrderService {
    @Autowired
    private AeBetOrderMapperExt aeBetOrderMapperExt;

    @Override
    public void insertBatch(List<AeBetOrder> aeBetOrderList) {
         aeBetOrderMapperExt.insertBatch(aeBetOrderList);
    }

    @Override
    public List<AeBetOrder> queryAeList() {
        return  aeBetOrderMapperExt.queryAeList();
    }
}
