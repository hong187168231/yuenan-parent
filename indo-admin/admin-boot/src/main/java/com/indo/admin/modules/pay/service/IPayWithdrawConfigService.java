package com.indo.admin.modules.pay.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.admin.pojo.dto.PayWayDTO;
import com.indo.admin.pojo.dto.PayWayQueryDTO;
import com.indo.admin.pojo.dto.PayWithdrawDTO;
import com.indo.admin.pojo.dto.PayWithdrawQueryDTO;
import com.indo.admin.pojo.vo.pay.PayWayConfigVO;
import com.indo.admin.pojo.vo.pay.PayWithdrawConfigVO;
import com.indo.core.pojo.entity.PayWayConfig;
import com.indo.core.pojo.entity.PayWithdrawConfig;

/**
 * <p>
 * 支付方式配置 服务类
 * </p>
 *
 * @author xxx
 * @since 2021-12-20
 */
public interface IPayWithdrawConfigService extends IService<PayWithdrawConfig> {


    Page<PayWithdrawConfigVO> queryAll(PayWithdrawQueryDTO dto);


    boolean edit(PayWithdrawDTO editDto);

}
