package com.indo.admin.modules.pay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.pojo.dto.PayWayQueryDTO;
import com.indo.admin.pojo.vo.PayChannelConfigVO;
import com.indo.admin.pojo.vo.PayWayConfigVO;
import com.indo.pay.pojo.dto.PayChannelConfigDto;
import com.indo.pay.pojo.dto.PayWayConfigDto;
import com.indo.pay.pojo.entity.PayWayConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 支付方式配置 Mapper 接口
 * </p>
 *
 * @author xxx
 * @since 2021-12-20
 */
@Mapper
public interface PayWayConfigMapper extends BaseMapper<PayWayConfig> {

    List<PayWayConfigVO> queryAll(@Param("page") Page<PayWayConfigVO> page, @Param("dto") PayWayQueryDTO dto);

}
