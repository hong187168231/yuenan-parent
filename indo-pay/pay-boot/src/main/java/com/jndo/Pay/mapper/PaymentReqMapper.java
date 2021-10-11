package com.jndo.Pay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.indo.pay.pojo.entity.PayBankConfig;
import com.indo.pay.pojo.entity.PayGroupConfig;
import com.indo.pay.pojo.vo.PayBankConfigVO;
import com.indo.pay.pojo.vo.PayGroupConfigVO;
import com.indo.pay.pojo.vo.PayWayConfigVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * <p>
 * 支付方式持久层
 * </p>
 *
 * @author boyd
 * @since 2021-09-11
 */
@Mapper
public interface PaymentReqMapper extends BaseMapper<PayGroupConfig> {

    /**
     * 会员层级信息
     * @param memId
     * @return
     */
    PayGroupConfigVO queryGroup(@Param("memId") String memId);

    /**
     * 银行支付列表查询
     * @param bankId
     * @return
     */
    List<PayBankConfigVO> queryBankList(@Param("bankId") Long bankId, @Param("memId") Long memId);

    /**
     * 扫码支付列表查询
     * @param wayType
     * @param memId
     * @return
     */
    List<PayWayConfigVO> queryWayList(@Param("wayType") String wayType, @Param("memId") Long memId);
}
