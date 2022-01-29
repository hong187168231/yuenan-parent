package com.indo.pay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.indo.core.pojo.entity.PayChannelConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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
public interface PayChannelMapper extends BaseMapper<PayChannelConfig> {

    List<PayChannelConfig> channelList();

    @Select("select * from pay_channel_config where `status` = 1 and" +
            " pay_channel_id = #{payChannelId} ")
    PayChannelConfig selectEnableChannelById(@Param("payChannelId") Long payChannelId);

}
