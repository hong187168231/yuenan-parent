package com.indo.admin.modules.pay.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.admin.modules.pay.mapper.PayWayConfigMapper;
import com.indo.admin.modules.pay.service.IPayWayConfigService;
import com.indo.admin.pojo.dto.PayWayDTO;
import com.indo.admin.pojo.dto.PayWayQueryDTO;
import com.indo.admin.pojo.vo.PayWayConfigVO;
import com.indo.core.pojo.entity.PayWayConfig;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 支付方式配置 服务实现类
 * </p>
 *
 * @author puff
 * @since 2021-12-20
 */
@Service
public class PayWayConfigServiceImpl extends ServiceImpl<PayWayConfigMapper, PayWayConfig> implements IPayWayConfigService {

    @Autowired
    private PayWayConfigMapper payWayConfigMapper;

    @Override
    public Page<PayWayConfigVO> queryAll(PayWayQueryDTO dto) {
        Page<PayWayConfigVO> page = new Page<>(dto.getPage(), dto.getLimit());
        List<PayWayConfigVO> list = payWayConfigMapper.queryAll(page, dto);
        page.setRecords(list);
        return page;
    }

    @Override
    public boolean add(PayWayDTO addDto) {
        PayWayConfig payWayConfig = new PayWayConfig();
        BeanUtils.copyProperties(addDto, payWayConfig);
        return this.baseMapper.insert(payWayConfig) > 0;
    }

    @Override
    public boolean edit(PayWayDTO editDto) {
        PayWayConfig payWayConfig = new PayWayConfig();
        BeanUtils.copyProperties(editDto, payWayConfig);
        return this.baseMapper.updateById(payWayConfig) > 0;
    }
}
