package com.indo.admin.modules.pay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.indo.admin.pojo.req.PayRechargeReq;
import com.indo.admin.pojo.vo.pay.RechargeOrderVO;
import com.indo.pay.pojo.entity.PayRechargeOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
public interface PayRechargeOrderMapper extends BaseMapper<PayRechargeOrder> {


    List<RechargeOrderVO> rechargeList(@Param("page") RechargeOrderVO page, @Param("req") PayRechargeReq req);


}
