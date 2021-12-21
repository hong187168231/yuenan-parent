package com.indo.admin.modules.pay.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.admin.pojo.dto.PayChannelDTO;
import com.indo.admin.pojo.dto.PayWayDTO;
import com.indo.admin.pojo.dto.PayWayQueryDTO;
import com.indo.admin.pojo.vo.PayChannelConfigVO;
import com.indo.admin.pojo.vo.PayWayConfigVO;
import com.indo.pay.pojo.dto.PayWayConfigDto;
import com.indo.pay.pojo.entity.PayWayConfig;

/**
 * <p>
 * 支付方式配置 服务类
 * </p>
 *
 * @author xxx
 * @since 2021-12-20
 */
public interface IPayWayConfigService extends IService<PayWayConfig> {


    Page<PayWayConfigVO> queryAll(PayWayQueryDTO dto);


    boolean add(PayWayDTO addDto);


    boolean edit(PayWayDTO editDto);

}
