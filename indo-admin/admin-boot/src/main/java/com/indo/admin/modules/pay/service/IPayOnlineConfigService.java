package com.indo.admin.modules.pay.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.common.result.Result;
import com.indo.pay.pojo.dto.PayOnlineConfigDto;
import com.indo.pay.pojo.dto.PayWayConfigDto;
import com.indo.pay.pojo.entity.PayOnlineConfig;
import com.indo.pay.pojo.entity.PayWayConfig;
import com.indo.pay.pojo.vo.PayOnlineConfigVO;
import com.indo.pay.pojo.vo.PayWayConfigVO;

import java.util.List;

/**
 * <p>
 * 在线支付配置
 * </p>
 *
 * @author boyd
 * @since 2021-09-09
 */
public interface IPayOnlineConfigService extends IService<PayOnlineConfig> {
    /**
     * 分页查询
     * @param page
     * @param dto
     * @return
     */
    List<PayOnlineConfigVO> queryList(Page<PayOnlineConfigVO> page, PayOnlineConfigDto dto);

    /**
     * 查询单个
     * @param dto
     * @return
     */
    PayOnlineConfigVO querySingle(PayOnlineConfigDto dto);

    /**
     * 在线支付配置写入
     * @param dto
     */
    Result insertPayOnline(PayOnlineConfigDto dto);

    /**
     * 在线支付配置更新
     * @param dto
     */
    Result updatePayOnline(PayOnlineConfigDto dto);

    /**
     * 在线支付配置复制
     * @param dto
     */
    Result copyPayOnline(PayOnlineConfigDto dto);

    /**
     * 在线支付配置删除
     * @param dto
     */
    Result deletePayOnline(PayOnlineConfigDto dto);

    /**
     * 停启在线支付配置
     * @param dto
     */
    Result stopStartPayOnline(PayOnlineConfigDto dto);
}
