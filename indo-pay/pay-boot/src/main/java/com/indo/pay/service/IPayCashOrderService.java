package com.indo.pay.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.core.pojo.entity.PayCashOrder;
import com.indo.pay.pojo.req.CashApplyReq;
import com.indo.pay.pojo.vo.PayCashOrderVO;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author xxx
 * @since 2021-11-13
 */
public interface IPayCashOrderService extends IService<PayCashOrder> {


    boolean cashApply(CashApplyReq cashApplyReq, LoginInfo loginInfo);

    Result<List<PayCashOrderVO>> cashRecordList(Integer page, Integer limit, LoginInfo loginInfo);

}
