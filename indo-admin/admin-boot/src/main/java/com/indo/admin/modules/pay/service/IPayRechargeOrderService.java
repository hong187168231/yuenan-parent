package com.indo.admin.modules.pay.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.admin.pojo.req.PayRechargeReq;
import com.indo.admin.pojo.vo.pay.RechargeOrderVO;
import com.indo.pay.pojo.entity.PayRechargeOrder;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author puff
 * @since 2021-11-13
 */
public interface IPayRechargeOrderService extends IService<PayRechargeOrder> {

    Page<RechargeOrderVO> rechargeList(PayRechargeReq rechargeReq);


}
