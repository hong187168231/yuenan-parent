package com.indo.admin.modules.pay.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.common.result.Result;
import com.indo.pay.pojo.dto.PayChannelConfigDto;
import com.indo.pay.pojo.dto.PayWayConfigDto;
import com.indo.pay.pojo.entity.PayChannelConfig;
import com.indo.pay.pojo.entity.PayWayConfig;
import com.indo.pay.pojo.vo.PayChannelConfigVO;
import com.indo.pay.pojo.vo.PayWayConfigVO;

import java.util.List;

/**
 * <p>
 * 支付方式配置
 * </p>
 *
 * @author boyd
 * @since 2021-09-09
 */
public interface IPayWayConfigService extends IService<PayWayConfig> {
    /**
     * 分页查询
     * @param page
     * @param dto
     * @return
     */
    List<PayWayConfigVO> queryList(Page<PayWayConfigVO> page, PayWayConfigDto dto);

    /**
     * 查询单个
     * @param dto
     * @return
     */
    PayWayConfigVO querySingle(PayWayConfigDto dto);

    /**
     * 支付方式配置写入
     * @param dto
     */
    Result insertPayWay(PayWayConfigDto dto);

    /**
     * 支付方式配置更新
     * @param dto
     */
    Result updatePayWay(PayWayConfigDto dto);

    /**
     * 支付方式配置删除
     * @param dto
     */
    Result deletePayWay(PayWayConfigDto dto);

    /**
     * 停启支付方式配置
     * @param dto
     */
    Result stopStartPayWay(PayWayConfigDto dto);
}
