package com.indo.admin.modules.pay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.pay.pojo.dto.PayChannelConfigDto;
import com.indo.pay.pojo.dto.PayWayConfigDto;
import com.indo.pay.pojo.entity.PayChannelConfig;
import com.indo.pay.pojo.entity.PayWayConfig;
import com.indo.pay.pojo.vo.PayChannelConfigVO;
import com.indo.pay.pojo.vo.PayWayConfigVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * <p>
 * 支付方式配置持久层
 * </p>
 *
 * @author boyd
 * @since 2021-09-09
 */
@Mapper
public interface PayWayConfigMapper extends BaseMapper<PayWayConfig> {
    /**
     * 支付方式配置列表查询
     * @param page
     * @param dto
     * @return
     */
    List<PayWayConfigVO> queryList(@Param("page") Page<PayWayConfigVO> page, @Param("dto") PayWayConfigDto dto);

    /**
     * 查询单个支付方式配置
     * @param dto
     * @return
     */
    PayWayConfigVO querySingle(@Param("dto") PayWayConfigDto dto);

    /**
     * 支付方式配置写入
     * @param payWayConfig
     */
    void insertPayWay(PayWayConfig payWayConfig);

    /**
     * 支付方式配置更新
     * @param payWayConfig
     */
    void updatePayWay(PayWayConfig payWayConfig);

    /**
     * 支付方式配置删除
     * @param dto
     */
    void deletePayWay(PayWayConfigDto dto);

    /**
     * 停启支付方式配置
     * @param dto
     */
    void stopStartPayWay(PayWayConfigDto dto);
}
