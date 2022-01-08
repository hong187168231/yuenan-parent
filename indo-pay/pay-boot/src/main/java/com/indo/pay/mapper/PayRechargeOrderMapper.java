package com.indo.pay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.indo.pay.pojo.entity.PayRechargeOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author puff
 * @since 2021-11-13
 */
@Mapper
public interface PayRechargeOrderMapper extends BaseMapper<PayRechargeOrder> {


    @Select("SELECT COUNT(1) from pay_recharge_order pro  WHERE pro.order_status = 1  and mem_id = #{memId}")
    int countMemIsFirstRecharge(@Param("memId") Long memId);

}
