package com.indo.admin.modules.pay.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.core.base.service.SuperService;
import com.indo.core.pojo.entity.PayManualRecharge;
import com.indo.pay.pojo.vo.ManualRechargeMemVO;
import com.indo.pay.pojo.vo.ManualRechargeRecordVO;

/**
 * <p>
 * 人工充值表 服务类
 * </p>
 *
 * @author xxx
 * @since 2022-01-12
 */
public interface IPayManualRechargeService extends SuperService<PayManualRecharge> {


    Page<ManualRechargeMemVO> memList(Long page, Long limit, String account);


    boolean operateRecharge(Integer operateType, Long memId, Float amount);


    Page<ManualRechargeRecordVO> queryList(Integer page, Integer limit, String account, Integer operateType);


}
