package com.indo.admin.modules.pay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.pojo.dto.PayChannelQueryDTO;
import com.indo.admin.pojo.vo.PayChannelConfigVO;
import com.indo.pay.pojo.dto.PayCashOrderDTO;
import com.indo.pay.pojo.dto.PayChannelConfigDto;
import com.indo.pay.pojo.entity.PayCashOrder;
import com.indo.pay.pojo.entity.PayChannelConfig;
import com.indo.pay.pojo.vo.PayCashOrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 支付渠道配置 Mapper 接口
 * </p>
 *
 * @author xxx
 * @since 2021-12-20
 */
@Mapper
public interface PayChannelConfigMapper extends BaseMapper<PayChannelConfig> {

    List<PayChannelConfigVO> queryAll(@Param("page") Page<PayChannelConfigVO> page,@Param("dto")  PayChannelQueryDTO dto);

}
