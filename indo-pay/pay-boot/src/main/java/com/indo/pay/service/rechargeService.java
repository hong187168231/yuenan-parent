package com.indo.pay.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.core.base.service.SuperService;
import com.indo.core.pojo.entity.PayRecharge;
import com.indo.pay.pojo.bo.RechargeBO;
import com.indo.pay.pojo.dto.RechargeDTO;
import com.indo.pay.pojo.req.RechargeReq;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author puff
 * @since 2021-11-13
 */
public interface rechargeService extends SuperService<PayRecharge> {

    boolean saveRechargeRecord(RechargeDTO rechargeDTO);

    // 业务逻辑参数校验
    RechargeBO logicConditionCheck(RechargeReq rechargeReq, LoginInfo loginInfo);

}
