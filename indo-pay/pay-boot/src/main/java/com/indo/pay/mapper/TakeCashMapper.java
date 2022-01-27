package com.indo.pay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.core.pojo.entity.PayTakeCash;
import com.indo.pay.pojo.vo.TakeCashRecordVO;
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
public interface TakeCashMapper extends BaseMapper<PayTakeCash> {

    List<TakeCashRecordVO> takeCashList(@Param("page") Page<TakeCashRecordVO> page, @Param("memId") Long memId);

    @Select("select sum(real_amount) from pay_recharge where  where to_days(create_time) = to_days(now()) and  order_status = 2  ")
    BigDecimal countTodayAmount();

}
