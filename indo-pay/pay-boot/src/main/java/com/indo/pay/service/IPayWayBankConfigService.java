package com.indo.pay.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.core.pojo.entity.PayWayBankConfig;

import java.util.List;

/**
 * 支付银行通道配置 服务类
 */
public interface IPayWayBankConfigService extends IService<PayWayBankConfig> {

	List<PayWayBankConfig> getPayChannelIdAndWayById(Long channelId, Long wayId);

	List<PayWayBankConfig> getPayChannelId(Long channelId);
}
