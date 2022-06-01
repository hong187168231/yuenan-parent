package com.indo.pay.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.core.pojo.entity.PayWayBankConfig;
import com.indo.pay.mapper.PayWayBankConfigMapper;
import com.indo.pay.service.IPayWayBankConfigService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 支付银行通道配置 服务实现类
 */
@Service
public class PayWayBankConfigServiceImpl extends ServiceImpl<PayWayBankConfigMapper, PayWayBankConfig>
		implements IPayWayBankConfigService {

	@Override
	public List<PayWayBankConfig> getPayChannelIdAndWayById(Long channelId, Long wayId) {
			return this.baseMapper.selectPayChannelIdAndWayById(channelId, wayId);
	}


	@Override
	public List<PayWayBankConfig> getPayChannelId(Long channelId) {
		return this.baseMapper.selectPayChannelId(channelId);
	}
}
