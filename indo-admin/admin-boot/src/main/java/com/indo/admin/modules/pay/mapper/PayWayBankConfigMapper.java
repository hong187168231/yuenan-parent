package com.indo.admin.modules.pay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.pojo.dto.PayWayQueryDTO;
import com.indo.admin.pojo.vo.pay.PayWayConfigVO;
import com.indo.core.pojo.entity.PayWayBankConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 支付银行通道配置 Mapper 接口
 * </p>
 *
 * @author xxx
 * @since 2022-06-01
 */
@Mapper
public interface PayWayBankConfigMapper extends BaseMapper<PayWayBankConfig> {

	@Select("select * from pay_way_bank_config where `pay_channel_id` = #{payChannelId} and pay_way_id = #{payWayId} and status=1")
	List<PayWayBankConfig> selectPayChannelIdAndWayById(@Param("payChannelId") Long payChannelId, @Param("payWayId") Long payWayId);

	@Select("select * from pay_way_bank_config where `pay_channel_id` = #{payChannelId} and status=1")
	List<PayWayBankConfig> selectPayChannelId(@Param("payChannelId") Long payChannelId);

	List<PayWayConfigVO> queryAll(@Param("page") Page<PayWayConfigVO> page, @Param("dto") PayWayQueryDTO dto);
}
