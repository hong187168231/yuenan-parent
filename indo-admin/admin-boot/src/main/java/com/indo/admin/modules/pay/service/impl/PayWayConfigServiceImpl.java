package com.indo.admin.modules.pay.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.admin.common.util.AdminBusinessRedisUtils;
import com.indo.admin.modules.pay.mapper.PayWayConfigMapper;
import com.indo.admin.modules.pay.service.IPayWayBankConfigService;
import com.indo.admin.modules.pay.service.IPayWayConfigService;
import com.indo.admin.pojo.dto.PayWayDTO;
import com.indo.admin.pojo.dto.PayWayQueryDTO;
import com.indo.admin.pojo.vo.pay.PayWayBankConfigVO;
import com.indo.admin.pojo.vo.pay.PayWayConfigVO;
import com.indo.common.constant.RedisConstants;
import com.indo.common.web.util.DozerUtil;
import com.indo.core.pojo.entity.PayWayBankConfig;
import com.indo.core.pojo.entity.PayWayConfig;
import com.indo.pay.pojo.vo.PayChannelVO;
import com.indo.pay.pojo.vo.PayWayBankVO;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    IPayWayBankConfigService payWayBankConfigService;

    @Override
    public Page<PayWayConfigVO> queryAll(PayWayQueryDTO dto) {
        Page<PayWayConfigVO> page = new Page<>(dto.getPage(), dto.getLimit());
        List<PayWayConfigVO> list = payWayConfigMapper.queryAll(page, dto);
        if (CollectionUtils.isNotEmpty(list)) {
            for (PayWayConfigVO vo : list) {
                List<PayWayBankConfigVO> payWayBankConfigList = payWayBankConfigService.getPayChannelId(vo.getPayChannelId());
                vo.setBankList(payWayBankConfigList.stream().map(PayWayBankConfigVO::getBankName).collect(Collectors.toList()));
            }
        }
        page.setRecords(list);
        return page;
    }

    @Override
    public boolean add(PayWayDTO addDto) {
        PayWayConfig payWayConfig = new PayWayConfig();
        BeanUtils.copyProperties(addDto, payWayConfig);
        if (baseMapper.insert(payWayConfig) > 0) {
            AdminBusinessRedisUtils.hset(RedisConstants.PAY_WAY_KEY, payWayConfig.getPayWayId() + "", payWayConfig);
            return true;
        }
        return false;
    }

    @Override
    public boolean edit(PayWayDTO editDto) {
        PayWayConfig payWayConfig = new PayWayConfig();
        BeanUtils.copyProperties(editDto, payWayConfig);
        if (baseMapper.updateById(payWayConfig) > 0) {
            AdminBusinessRedisUtils.hset(RedisConstants.PAY_WAY_KEY, payWayConfig.getPayWayId() + "", payWayConfig);
            return true;
        }
        return false;
    }
}
