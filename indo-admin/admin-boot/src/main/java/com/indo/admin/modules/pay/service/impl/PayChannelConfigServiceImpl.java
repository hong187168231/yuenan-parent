package com.indo.admin.modules.pay.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.admin.common.util.AdminBusinessRedisUtils;
import com.indo.admin.modules.pay.mapper.PayChannelConfigMapper;
import com.indo.admin.modules.pay.mapper.PayWithdrawConfigMapper;
import com.indo.admin.modules.pay.service.IPayChannelConfigService;
import com.indo.admin.pojo.dto.PayChannelDTO;
import com.indo.admin.pojo.dto.PayChannelQueryDTO;
import com.indo.admin.pojo.vo.pay.PayChannelConfigVO;
import com.indo.common.constant.RedisConstants;
import com.indo.core.pojo.entity.PayChannelConfig;
import com.indo.core.pojo.entity.PayWithdrawConfig;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 支付渠道配置 服务实现类
 * </p>
 *
 * @author puff
 * @since 2021-12-20
 */
@Service
public class PayChannelConfigServiceImpl extends ServiceImpl<PayChannelConfigMapper, PayChannelConfig> implements IPayChannelConfigService {

    @Autowired
    private PayChannelConfigMapper payChannelConfigMapper;

    @Autowired
    private PayWithdrawConfigMapper payWithdrawConfigMapper;

    @Override
    public Page<PayChannelConfigVO> queryAll(PayChannelQueryDTO dto) {
        Page<PayChannelConfigVO> page = new Page<>(dto.getPage(), dto.getLimit());
        List<PayChannelConfigVO> list = payChannelConfigMapper.queryAll(page, dto);
        page.setRecords(list);
        return page;
    }

    @Override
    public boolean add(PayChannelDTO addDto) {
        PayChannelConfig channelConfig = new PayChannelConfig();
        BeanUtils.copyProperties(addDto, channelConfig);
        if (baseMapper.insert(channelConfig) > 0) {
            AdminBusinessRedisUtils.hset(RedisConstants.PAY_CHANNEL_KEY, channelConfig.getPayChannelId() + "", channelConfig);
            return true;
        }
        PayWithdrawConfig payWithdrawConfig = new PayWithdrawConfig();
        payWithdrawConfig.setPayChannelId(channelConfig.getPayChannelId());
        return payWithdrawConfigMapper.insert(payWithdrawConfig) > 0;
    }

    @Override
    public boolean edit(PayChannelDTO editDto) {
        PayChannelConfig channelConfig = new PayChannelConfig();
        BeanUtils.copyProperties(editDto, channelConfig);
        if (baseMapper.updateById(channelConfig) > 0) {
            AdminBusinessRedisUtils.hset(RedisConstants.PAY_CHANNEL_KEY, channelConfig.getPayChannelId() + "", channelConfig);
            return true;
        }
        return false;
    }
}
