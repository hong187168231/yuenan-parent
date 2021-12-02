package com.indo.pay.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.web.util.DozerUtil;
import com.indo.pay.mapper.PayChannelConfigMapper;
import com.indo.pay.pojo.entity.PayChannelConfig;
import com.indo.pay.pojo.vo.PayChannelVO;
import com.indo.pay.service.IPayChannelConfigService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 支付渠道配置 服务实现类
 * </p>
 *
 * @author puff
 * @since 2021-11-13
 */
@Service
public class PayChannelConfigServiceImpl extends ServiceImpl<PayChannelConfigMapper, PayChannelConfig> implements IPayChannelConfigService {

    @Resource
    private DozerUtil dozerUtil;

    @Override
    public List<PayChannelVO> channelList(LoginInfo loginInfo) {
        List<PayChannelConfig> configList = baseMapper.channelList();
        return dozerUtil.convert(configList, PayChannelVO.class);
    }
}