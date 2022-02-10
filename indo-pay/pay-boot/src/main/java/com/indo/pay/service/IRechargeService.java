package com.indo.pay.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.core.base.service.SuperService;
import com.indo.core.pojo.entity.PayRecharge;
import com.indo.pay.pojo.bo.RechargeBO;
import com.indo.pay.pojo.dto.RechargeDTO;
import com.indo.pay.pojo.req.RechargeReq;
import com.indo.pay.pojo.vo.RechargeRecordVO;
import com.indo.pay.pojo.vo.TakeCashRecordVO;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author puff
 * @since 2021-11-13
 */
public interface IRechargeService extends SuperService<PayRecharge> {

    boolean saveRechargeRecord(RechargeDTO rechargeDTO);

    // 业务逻辑参数校验
    RechargeBO logicConditionCheck(RechargeReq rechargeReq, LoginInfo loginInfo);

    Result<List<RechargeRecordVO>> rechargeRecordList(Integer page, Integer limit, LoginInfo loginInfo);

}
