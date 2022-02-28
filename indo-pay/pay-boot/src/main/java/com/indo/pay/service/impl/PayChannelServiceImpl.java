package com.indo.pay.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.common.constant.GlobalConstants;
import com.indo.common.constant.RedisConstants;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.redis.utils.RedisUtils;
import com.indo.common.utils.CollectionUtil;
import com.indo.common.web.exception.BizException;
import com.indo.common.web.util.DozerUtil;
import com.indo.core.pojo.entity.Activity;
import com.indo.core.pojo.entity.PayChannelConfig;
import com.indo.pay.mapper.PayChannelMapper;
import com.indo.pay.pojo.vo.PayChannelVO;
import com.indo.pay.service.IPayChannelService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 支付渠道配置 服务实现类
 * </p>
 *
 * @author puff
 * @since 2021-11-13
 */
@Service
public class PayChannelServiceImpl extends ServiceImpl<PayChannelMapper, PayChannelConfig> implements IPayChannelService {


    @Override
    public List<PayChannelVO> channelList(LoginInfo loginInfo) {
        Map<Object, Object> map = RedisUtils.hmget(RedisConstants.PAY_CHANNEL_KEY);
        List<PayChannelConfig> configList = new LinkedList(map.values());
        if (CollectionUtil.isEmpty(configList)) {
            LambdaQueryWrapper<PayChannelConfig> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(PayChannelConfig::getStatus, 1);
            configList = this.baseMapper.selectList(wrapper);
        }
        if (CollectionUtil.isNotEmpty(configList)) {
            configList = configList.stream()
                    .filter(channelConfig -> channelConfig.getStatus().equals(1))
                    .collect(Collectors.toList());
        }
        return DozerUtil.convert(configList, PayChannelVO.class);
    }

    @Override
    public PayChannelConfig getPayChannelById(Long channelId) {
        PayChannelConfig payChannelConfig = (PayChannelConfig) RedisUtils.hget(RedisConstants.PAY_CHANNEL_KEY, channelId + "");
        if (ObjectUtil.isEmpty(payChannelConfig)) {
            return this.baseMapper.selectEnableChannelById(channelId);
        } else {
            return GlobalConstants.STATUS_OPEN.equals(payChannelConfig.getStatus()) ? payChannelConfig : null;
        }
    }
}
