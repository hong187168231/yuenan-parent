package com.indo.admin.modules.pay.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.admin.pojo.vo.pay.PayTakeCashApplyVO;
import com.indo.admin.pojo.vo.pay.PayTakeCashRecordVO;
import com.indo.common.enums.AudiTypeEnum;
import com.indo.common.result.Result;
import com.indo.core.pojo.entity.PayTakeCash;
import com.indo.pay.pojo.req.PayTakeCashReq;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author xxx
 * @since 2021-11-13
 */
public interface IPayTakeCashService extends IService<PayTakeCash> {


    Page<PayTakeCashApplyVO> cashApplyList(PayTakeCashReq cashOrderDTO);

    Result<List<PayTakeCashRecordVO>> cashRecordList(PayTakeCashReq payTakeCashReq);

    boolean takeCashOpera(AudiTypeEnum audiTypeEnum, Long takeCashId);
}
