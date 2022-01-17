package com.indo.pay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.indo.core.pojo.entity.PayRecharge;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;

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

    @Select("select sum(real_amount) from pay_recharge where  where to_days(create_time) = to_days(now()) and  order_status = 2 and memId = #{memId} ")
    BigDecimal countTodayAmount(@Param("memId") Long memId);

}
