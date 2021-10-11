package com.indo.admin.modules.pay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.pay.pojo.dto.PayBankConfigDto;
import com.indo.pay.pojo.dto.PayChannelConfigDto;
import com.indo.pay.pojo.entity.PayBankConfig;
import com.indo.pay.pojo.entity.PayChannelConfig;
import com.indo.pay.pojo.vo.PayBankConfigVO;
import com.indo.pay.pojo.vo.PayChannelConfigVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * <p>
 * 支付渠道配置持久层
 * </p>
 *
 * @author boyd
 * @since 2021-09-09
 */
@Mapper
public interface PayChannelConfigMapper extends BaseMapper<PayChannelConfig> {
    /**
     * 支付渠道配置列表查询
     * @param page
     * @param dto
     * @return
     */
    List<PayChannelConfigVO> queryList(@Param("page") Page<PayChannelConfigVO> page, @Param("dto") PayChannelConfigDto dto);

    /**
     * 查询单个支付渠道配置
     * @param dto
     * @return
     */
    PayChannelConfigVO querySingle(@Param("dto") PayChannelConfigDto dto);

    /**
     * 支付渠道配置写入
     * @param payChannelConfig
     */
    void insertPayChannel(PayChannelConfig payChannelConfig);

    /**
     * 支付渠道配置更新
     * @param payChannelConfig
     */
    void updatePayChannel(PayChannelConfig payChannelConfig);

    /**
     * 支付渠道配置删除
     * @param dto
     */
    void deletePayChannel(PayChannelConfigDto dto);

    /**
     * 停启支付渠道配置
     * @param dto
     */
    void stopStartPayChannel(PayChannelConfigDto dto);
}
