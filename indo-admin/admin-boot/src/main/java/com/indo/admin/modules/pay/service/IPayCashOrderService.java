package com.indo.admin.modules.pay.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.common.result.Result;
import com.indo.core.pojo.entity.PayCashOrder;
import com.indo.pay.pojo.dto.PayCashOrderDTO;
import com.indo.pay.pojo.vo.PayCashOrderApplyVO;
import com.indo.pay.pojo.vo.PayCashOrderVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xxx
 * @since 2021-11-13
 */
public interface IPayCashOrderService extends IService<PayCashOrder> {


    Result<List<PayCashOrderApplyVO>> cashApplyList(PayCashOrderDTO cashOrderDTO);

    Result<List<PayCashOrderVO>> cashRecordList(PayCashOrderDTO cashOrderDTO);
}
