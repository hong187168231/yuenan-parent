package com.indo.admin.modules.pay.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.admin.modules.pay.mapper.PayChannelConfigMapper;
import com.indo.admin.modules.pay.service.IPayChannelConfigService;
import com.indo.admin.pojo.dto.PayChannelDTO;
import com.indo.admin.pojo.dto.PayChannelQueryDTO;
import com.indo.admin.pojo.vo.PayChannelConfigVO;
import com.indo.core.pojo.entity.PayChannelConfig;
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

    @Override
    public Page<PayChannelConfigVO> queryAll(PayChannelQueryDTO dto) {
        Page<PayChannelConfigVO> page = new Page<>(dto.getPage(), dto.getLimit());
        List<PayChannelConfigVO> list = payChannelConfigMapper.queryAll(page, dto);
        page.setRecords(list);
        return page;
    }

    @Override
    public boolean add(PayChannelDTO addDto) {
        PayChannelConfig payChannelConfig = new PayChannelConfig();
        BeanUtils.copyProperties(addDto, payChannelConfig);
        return this.baseMapper.insert(payChannelConfig) > 0;
    }

    @Override
    public boolean edit(PayChannelDTO editDto) {
        PayChannelConfig payChannelConfig = new PayChannelConfig();
        BeanUtils.copyProperties(editDto, payChannelConfig);
        return this.baseMapper.updateById(payChannelConfig) > 0;
    }
}
