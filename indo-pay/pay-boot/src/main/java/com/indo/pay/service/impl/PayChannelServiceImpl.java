package com.indo.pay.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.indo.common.constant.RedisConstants;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.redis.utils.RedisUtils;
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

    @Resource
    private DozerUtil dozerUtil;

    @Override
    public List<PayChannelVO> channelList(LoginInfo loginInfo) {
        Map<Object, Object> map = RedisUtils.hmget(RedisConstants.PAY_CHANNEL_KEY);
        List<PayChannelConfig> configList = new LinkedList(map.values());
        return dozerUtil.convert(configList, PayChannelVO.class);
    }
}
