package com.indo.game.services.ky.impl;


import com.indo.game.mapper.ky.KyBetOrderMapperExt;
import com.indo.game.pojo.entity.ky.KyBetOrder;
import com.indo.game.services.ky.KybetOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KybetOrderServiceImpl implements KybetOrderService {

    @Autowired
    private KyBetOrderMapperExt kyBetOrderMapperExt;


    @Override
    public void insertBatch(List<KyBetOrder> kyBetOrderList) {
        kyBetOrderMapperExt.insertBatch(kyBetOrderList);
    }

    @Override
    public List<KyBetOrder> queryKyOrderList() {
        return kyBetOrderMapperExt.queryKyOrderList();
    }
}
