package com.indo.admin.modules.pay.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.admin.pojo.dto.PayChannelDTO;
import com.indo.admin.pojo.dto.PayChannelQueryDTO;
import com.indo.admin.pojo.vo.pay.PayChannelConfigVO;
import com.indo.core.pojo.entity.PayChannelConfig;

import java.util.List;

/**
 * <p>
 * 支付渠道配置 服务类
 * </p>
 *
 * @author xxx
 * @since 2021-12-20
 */
public interface IPayChannelConfigService extends IService<PayChannelConfig> {

    Page<PayChannelConfigVO> queryAll(PayChannelQueryDTO queryDTO);

    boolean add(PayChannelDTO addDto);


    boolean edit(PayChannelDTO editDto);

	  List<PayChannelConfig> queryByIds(List<Long> ids);
}
