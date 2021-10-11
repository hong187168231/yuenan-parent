package com.indo.admin.modules.pay.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.common.result.Result;
import com.indo.pay.pojo.dto.PayBankConfigDto;
import com.indo.pay.pojo.dto.PayGroupConfigDto;
import com.indo.pay.pojo.entity.PayBankConfig;
import com.indo.pay.pojo.entity.PayGroupConfig;
import com.indo.pay.pojo.vo.PayBankConfigVO;
import com.indo.pay.pojo.vo.PayGroupConfigVO;

import java.util.List;

/**
 * <p>
 * 支付层级配置
 * </p>
 *
 * @author boyd
 * @since 2021-09-09
 */
public interface IPayGroupConfigService extends IService<PayGroupConfig> {
    /**
     * 分页查询
     * @param page
     * @param dto
     * @return
     */
    List<PayGroupConfigVO> queryList(Page<PayGroupConfigVO> page, PayGroupConfigDto dto);

    /**
     * 查询单个
     * @param dto
     * @return
     */
    PayGroupConfigVO querySingle(PayGroupConfigDto dto);

    /**
     * 银行支付配置写入
     * @param dto
     */
    Result insertPayGroup(PayGroupConfigDto dto);

    /**
     * 银行支付配置更新
     * @param dto
     */
    Result updatePayGroup(PayGroupConfigDto dto);

    /**
     * 银行支付配置删除
     * @param dto
     */
    Result deletePayGroup(PayGroupConfigDto dto);

    /**
     * 停启银行支付配置
     * @param dto
     */
    Result stopStartPayGroup(PayGroupConfigDto dto);
}
