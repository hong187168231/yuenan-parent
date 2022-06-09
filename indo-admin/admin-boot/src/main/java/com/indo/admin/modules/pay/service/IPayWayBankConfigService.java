package com.indo.admin.modules.pay.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.admin.pojo.dto.PayWayBankQueryDTO;
import com.indo.admin.pojo.dto.PayWayDTO;
import com.indo.admin.pojo.dto.PayWayQueryDTO;
import com.indo.admin.pojo.vo.pay.PayWayBankConfigVO;
import com.indo.admin.pojo.vo.pay.PayWayConfigVO;
import com.indo.common.result.Result;
import com.indo.core.pojo.entity.PayWayBankConfig;

import java.util.List;

/**
 * 支付银行通道配置 服务类
 */
public interface IPayWayBankConfigService extends IService<PayWayBankConfig> {

	List<PayWayBankConfigVO> getPayChannelIdAndWayById(Long channelId, Long wayId);

	List<PayWayBankConfigVO> getPayChannelId(Long channelId);

	Result<List<PayWayBankConfigVO>> queryAll(PayWayBankQueryDTO dto);


	boolean add(PayWayDTO addDto);


	boolean edit(PayWayDTO editDto);
}
