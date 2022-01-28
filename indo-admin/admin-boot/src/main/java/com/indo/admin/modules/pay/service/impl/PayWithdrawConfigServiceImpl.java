package com.indo.admin.modules.pay.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.admin.common.util.AdminBusinessRedisUtils;
import com.indo.admin.modules.pay.mapper.PayWayConfigMapper;
import com.indo.admin.modules.pay.mapper.PayWithdrawConfigMapper;
import com.indo.admin.modules.pay.service.IPayWayConfigService;
import com.indo.admin.modules.pay.service.IPayWithdrawConfigService;
import com.indo.admin.pojo.dto.PayWayDTO;
import com.indo.admin.pojo.dto.PayWayQueryDTO;
import com.indo.admin.pojo.dto.PayWithdrawDTO;
import com.indo.admin.pojo.dto.PayWithdrawQueryDTO;
import com.indo.admin.pojo.vo.pay.PayWayConfigVO;
import com.indo.admin.pojo.vo.pay.PayWithdrawConfigVO;
import com.indo.common.constant.RedisConstants;
import com.indo.core.pojo.entity.PayWayConfig;
import com.indo.core.pojo.entity.PayWithdrawConfig;
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
public class PayWithdrawConfigServiceImpl extends ServiceImpl<PayWithdrawConfigMapper, PayWithdrawConfig> implements IPayWithdrawConfigService {

    @Autowired
    private PayWithdrawConfigMapper payWithdrawConfigMapper;

    @Override
    public Page<PayWithdrawConfigVO> queryAll(PayWithdrawQueryDTO dto) {
        Page<PayWithdrawConfigVO> page = new Page<>(dto.getPage(), dto.getLimit());
        List<PayWithdrawConfigVO> list = payWithdrawConfigMapper.queryAll(page);
        page.setRecords(list);
        return page;
    }


    @Override
    public boolean edit(PayWithdrawDTO editDto) {
        PayWithdrawConfig payWithdrawConfig = new PayWithdrawConfig();
        BeanUtils.copyProperties(editDto, payWithdrawConfig);
        if (baseMapper.updateById(payWithdrawConfig) > 0) {
            AdminBusinessRedisUtils.hset(RedisConstants.PAY_WITHDRAW_KEY, payWithdrawConfig.getPayWithdrawId() + "", payWithdrawConfig);
            return true;
        }
        return false;
    }
}
