package com.indo.game.services.es.impl;


import com.indo.game.mapper.es.EsBetOrderMapperExt;
import com.indo.game.pojo.entity.es.EsBetOrder;
import com.indo.game.services.es.EsbetOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EsbetOrderServiceImpl implements EsbetOrderService {

    @Autowired
    private EsBetOrderMapperExt esBetOrderMapperExt;


    @Override
    public void insertBatch(List<EsBetOrder> esBetOrderList) {
        esBetOrderMapperExt.insertBatch(esBetOrderList);
    }
}
