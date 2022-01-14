package com.indo.admin.modules.pay.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.admin.pojo.req.pay.PayRechargeReq;
import com.indo.admin.pojo.vo.pay.RechargeOrderVO;
import com.indo.core.pojo.entity.PayRecharge;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author puff
 * @since 2021-11-13
 */
public interface IPayRechargeService extends IService<PayRecharge> {

    Page<RechargeOrderVO> rechargeList(PayRechargeReq rechargeReq);


}
