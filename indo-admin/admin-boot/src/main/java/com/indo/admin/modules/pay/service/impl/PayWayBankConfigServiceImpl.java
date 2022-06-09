package com.indo.admin.modules.pay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.admin.common.util.AdminBusinessRedisUtils;
import com.indo.admin.modules.pay.mapper.PayWayBankConfigMapper;
import com.indo.admin.modules.pay.mapper.PayWayConfigMapper;
import com.indo.admin.modules.pay.service.IPayWayBankConfigService;
import com.indo.admin.pojo.dto.PayWayBankQueryDTO;
import com.indo.admin.pojo.dto.PayWayDTO;
import com.indo.admin.pojo.dto.PayWayQueryDTO;
import com.indo.admin.pojo.vo.pay.PayWayBankConfigVO;
import com.indo.admin.pojo.vo.pay.PayWayConfigVO;
import com.indo.common.constant.RedisConstants;
import com.indo.common.result.Result;
import com.indo.common.utils.StringUtils;
import com.indo.common.web.util.DozerUtil;
import com.indo.core.pojo.entity.PayBank;
import com.indo.core.pojo.entity.PayWayBankConfig;
import com.indo.core.pojo.entity.PayWayConfig;
import com.indo.pay.pojo.vo.PayBankVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 支付银行通道配置 服务实现类
 */
@Service
public class PayWayBankConfigServiceImpl extends ServiceImpl<PayWayBankConfigMapper, PayWayBankConfig>
		implements IPayWayBankConfigService {

	@Autowired
	private PayWayBankConfigMapper payWayBankConfigMapper;

	@Override
	public List<PayWayBankConfigVO> getPayChannelIdAndWayById(Long channelId, Long wayId) {
		return DozerUtil.convert(this.baseMapper.selectPayChannelIdAndWayById(channelId, wayId), PayWayBankConfigVO.class);
	}


	@Override
	public List<PayWayBankConfigVO> getPayChannelId(Long channelId) {
		return DozerUtil.convert(this.baseMapper.selectPayChannelId(channelId), PayWayBankConfigVO.class);
	}

	@Override
	public Result<List<PayWayBankConfigVO>> queryAll(PayWayBankQueryDTO dto) {
		Page<PayWayBankConfig> page = new Page(dto.getPage(), dto.getLimit());
		LambdaQueryWrapper<PayWayBankConfig> wrapper = new LambdaQueryWrapper<>();
		if (dto.getPayWayId() != null) {
			wrapper.like(PayWayBankConfig::getPayWayId, dto.getPayWayId());
		}
		if (dto.getPayChannelId() != null) {
			wrapper.like(PayWayBankConfig::getPayWayId, dto.getPayWayId());
		}
		Page<PayWayBankConfig> pageList = baseMapper.selectPage(page, wrapper);
		List<PayWayBankConfigVO> result = DozerUtil.convert(pageList.getRecords(), PayWayBankConfigVO.class);
		return Result.success(result, page.getTotal());
	}

	@Override
	public boolean add(PayWayDTO addDto) {
		PayWayBankConfig payWayBankConfig = new PayWayBankConfig();
		BeanUtils.copyProperties(addDto, payWayBankConfig);
		return baseMapper.insert(payWayBankConfig) > 0;
	}

	@Override
	public boolean edit(PayWayDTO editDto) {
		PayWayBankConfig payWayBankConfig = new PayWayBankConfig();
		BeanUtils.copyProperties(editDto, payWayBankConfig);
		return baseMapper.updateById(payWayBankConfig) > 0;
	}
}
