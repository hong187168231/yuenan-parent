package com.indo.admin.modules.pay.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.admin.modules.pay.mapper.PayRechargeMapper;
import com.indo.admin.modules.pay.service.IPayRechargeService;
import com.indo.admin.pojo.req.pay.PayRechargeReq;
import com.indo.admin.pojo.vo.pay.RechargeOrderVO;
import com.indo.core.pojo.entity.PayRecharge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author puff
 * @since 2021-11-13
 */
@Service
public class PayRechargeServiceImpl extends ServiceImpl<PayRechargeMapper, PayRecharge> implements IPayRechargeService {

    @Autowired
    private PayRechargeMapper payRechargeOrderMapper;

    @Override
    public Page<RechargeOrderVO> rechargeList(PayRechargeReq req) {
        Page<RechargeOrderVO> page = new Page<>(req.getPage(), req.getLimit());
        List<RechargeOrderVO> result = payRechargeOrderMapper.rechargeList(page, req);
        page.setRecords(result);
        return page;
    }
}