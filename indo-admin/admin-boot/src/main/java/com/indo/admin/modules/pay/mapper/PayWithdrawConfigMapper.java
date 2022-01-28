package com.indo.admin.modules.pay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.pojo.dto.PayWayQueryDTO;
import com.indo.admin.pojo.vo.pay.PayWayConfigVO;
import com.indo.admin.pojo.vo.pay.PayWithdrawConfigVO;
import com.indo.core.pojo.entity.PayWithdrawConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 支付方式配置 Mapper 接口
 * </p>
 *
 * @author xxx
 * @since 2022-01-26
 */
@Mapper
public interface PayWithdrawConfigMapper extends BaseMapper<PayWithdrawConfig> {

    List<PayWithdrawConfigVO> queryAll(@Param("page") Page<PayWithdrawConfigVO> page);

}
