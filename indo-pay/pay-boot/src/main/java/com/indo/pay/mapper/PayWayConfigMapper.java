package com.indo.pay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.indo.pay.pojo.entity.PayWayConfig;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 支付方式配置 Mapper 接口
 * </p>
 *
 * @author puff
 * @since 2021-11-13
 */
@Mapper
public interface PayWayConfigMapper extends BaseMapper<PayWayConfig> {

    List<PayWayConfig> wayList();

}
