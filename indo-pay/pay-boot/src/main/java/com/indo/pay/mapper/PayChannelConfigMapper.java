package com.indo.pay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.indo.core.pojo.entity.PayChannelConfig;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 支付渠道配置 Mapper 接口
 * </p>
 *
 * @author puff
 * @since 2021-11-13
 */
@Mapper
public interface PayChannelConfigMapper extends BaseMapper<PayChannelConfig> {

     List<PayChannelConfig> channelList();

}
