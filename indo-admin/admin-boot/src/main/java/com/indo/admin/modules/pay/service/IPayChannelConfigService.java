package com.indo.admin.modules.pay.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.common.result.Result;
import com.indo.pay.pojo.dto.PayBankConfigDto;
import com.indo.pay.pojo.dto.PayChannelConfigDto;
import com.indo.pay.pojo.entity.PayBankConfig;
import com.indo.pay.pojo.entity.PayChannelConfig;
import com.indo.pay.pojo.vo.PayBankConfigVO;
import com.indo.pay.pojo.vo.PayChannelConfigVO;

import java.util.List;

/**
 * <p>
 * 支付渠道配置
 * </p>
 *
 * @author boyd
 * @since 2021-09-09
 */
public interface IPayChannelConfigService extends IService<PayChannelConfig> {
    /**
     * 分页查询
     * @param page
     * @param dto
     * @return
     */
    List<PayChannelConfigVO> queryList(Page<PayChannelConfigVO> page, PayChannelConfigDto dto);

    /**
     * 查询单个
     * @param dto
     * @return
     */
    PayChannelConfigVO querySingle(PayChannelConfigDto dto);

    /**
     * 支付渠道配置写入
     * @param dto
     */
    Result insertPayChannel(PayChannelConfigDto dto);

    /**
     * 支付渠道配置更新
     * @param dto
     */
    Result updatePayChannel(PayChannelConfigDto dto);

    /**
     * 支付渠道配置删除
     * @param dto
     */
    Result deletePayChannel(PayChannelConfigDto dto);

    /**
     * 停启支付渠道配置
     * @param dto
     */
    Result stopStartPayChannel(PayChannelConfigDto dto);
}
