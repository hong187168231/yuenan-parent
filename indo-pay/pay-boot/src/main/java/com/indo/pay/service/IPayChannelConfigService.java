package com.indo.pay.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.pay.pojo.entity.PayChannelConfig;
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
public interface IPayChannelConfigService extends IService<PayChannelConfig> {



    List<PayChannelVO> channelList(LoginInfo loginInfo);


}
