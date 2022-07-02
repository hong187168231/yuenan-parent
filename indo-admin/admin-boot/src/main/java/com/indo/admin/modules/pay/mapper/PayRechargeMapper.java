package com.indo.admin.modules.pay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.pojo.dto.PayRechargeReportDTO;
import com.indo.admin.pojo.req.pay.PayRechargeReq;
import com.indo.admin.pojo.vo.pay.PayRechargeReportVo;
import com.indo.admin.pojo.vo.pay.RechargeOrderVO;
import com.indo.core.pojo.entity.PayRecharge;
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
public interface PayRechargeMapper extends BaseMapper<PayRecharge> {


    List<RechargeOrderVO> rechargeList(@Param("page") Page<RechargeOrderVO> page, @Param("req") PayRechargeReq req);

    /**
     * 查询充值报表
     * @param page
     * @param payRechargeReportDTO
     * @return
     */
    Page<PayRechargeReportVo> findPayRechargeReport(Page<PayRechargeReportVo> page, PayRechargeReportDTO payRechargeReportDTO);
}
