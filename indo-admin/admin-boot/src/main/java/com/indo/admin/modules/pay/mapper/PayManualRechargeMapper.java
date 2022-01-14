package com.indo.admin.modules.pay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.core.pojo.entity.PayManualRecharge;
import com.indo.pay.pojo.vo.ManualRechargeMemVO;
import com.indo.pay.pojo.vo.ManualRechargeRecordVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 人工充值表 Mapper 接口
 * </p>
 *
 * @author xxx
 * @since 2022-01-12
 */
@Mapper
public interface PayManualRechargeMapper extends BaseMapper<PayManualRecharge> {


    List<ManualRechargeMemVO> memList(@Param("page") Page<ManualRechargeMemVO> page, @Param("account") String account);

    List<ManualRechargeRecordVO> queryList(@Param("page") Page<ManualRechargeRecordVO> page,
                                           @Param("account") String account,
                                           @Param("operateType") Integer operateType);

}
