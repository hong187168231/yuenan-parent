package com.indo.pay.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.common.constant.RedisConstants;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.redis.utils.RedisUtils;
import com.indo.common.web.exception.BizException;
import com.indo.core.pojo.entity.PayChannelConfig;
import com.indo.pay.pojo.vo.PayChannelVO;

import java.util.List;

/**
 * <p>
 * 支付渠道配置 服务类
 * </p>
 *
 * @author puff
 * @since 2021-11-13
 */
public interface IPayChannelService extends IService<PayChannelConfig> {


    List<PayChannelVO> channelList(LoginInfo loginInfo);

    PayChannelConfig getPayChannelById(Long channelId);


}
