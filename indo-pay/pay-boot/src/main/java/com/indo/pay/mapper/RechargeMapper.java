package com.indo.pay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.common.pojo.bo.LoginInfo;
import com.indo.common.result.Result;
import com.indo.core.pojo.entity.PayRecharge;
import com.indo.pay.pojo.vo.RechargeRecordVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author puff
 * @since 2021-11-13
 */
@Mapper
public interface RechargeMapper extends BaseMapper<PayRecharge> {


    @Select("SELECT COUNT(1) from pay_recharge_order pro  WHERE pro.order_status = 1  and mem_id = #{memId}")
    int countMemIsFirstRecharge(@Param("memId") Long memId);

    @Select("select sum(real_amount) from pay_recharge where   to_days(create_time) = to_days(now()) and  order_status = 2 and mem_id = #{memId} ")
    BigDecimal countMemTodayAmount(@Param("memId") Long memId);

    @Select("select IFNULL(sum(real_amount),0.00)  from pay_recharge where  to_days(create_time) = to_days(now()) " +
            " and  order_status = 2 and channel_id = #{channelId} and way_id = #{wayId} ")
    BigDecimal countWayTodayAmount(@Param("channelId") Long channelId, @Param("wayId") Long wayId);

    Page<RechargeRecordVO> rechargeRecordList(@Param("page") Page<RechargeRecordVO> page, @Param("memId") Long memId);

}
