package com.live.admin.modules.pay.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.live.common.result.Result;
import com.live.pay.pojo.dto.PayBankConfigDto;
import com.live.pay.pojo.dto.PayCashConfigDto;
import com.live.pay.pojo.entity.PayBankConfig;
import com.live.pay.pojo.entity.PayCashConfig;
import com.live.pay.pojo.vo.PayBankConfigVO;
import com.live.pay.pojo.vo.PayCashConfigVO;

import java.util.List;

/**
 * <p>
 * 银行支付配置
 * </p>
 *
 * @author boyd
 * @since 2021-09-09
 */
public interface IPayBankConfigService extends IService<PayBankConfig> {
    /**
     * 分页查询
     * @param page
     * @param dto
     * @return
     */
    List<PayBankConfigVO> queryList(Page<PayBankConfigVO> page, PayBankConfigDto dto);

    /**
     * 查询单个
     * @param dto
     * @return
     */
    PayBankConfigVO querySingle(PayBankConfigDto dto);

    /**
     * 银行支付配置写入
     * @param dto
     */
    Result insertPayBank(PayBankConfigDto dto);

    /**
     * 银行支付配置更新
     * @param dto
     */
    Result updatePayBank(PayBankConfigDto dto);

    /**
     * 银行支付配置删除
     * @param dto
     */
    Result deletePayBank(PayBankConfigDto dto);

    /**
     * 停启银行支付配置
     * @param dto
     */
    Result stopStartPayBank(PayBankConfigDto dto);
}
