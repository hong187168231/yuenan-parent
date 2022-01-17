package com.indo.pay.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.core.base.service.SuperService;
import com.indo.core.pojo.entity.PayTakeCash;
import com.indo.pay.pojo.req.TakeCashApplyReq;
import com.indo.pay.pojo.vo.TakeCashRecordVO;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author xxx
 * @since 2021-11-13
 */
public interface ITakeCashService extends SuperService<PayTakeCash> {


    boolean takeCashApply(TakeCashApplyReq cashApplyReq, LoginInfo loginInfo);

    Result<List<TakeCashRecordVO>> cashRecordList(Integer page, Integer limit, LoginInfo loginInfo);

}
