package com.indo.admin.modules.pay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.admin.pojo.dto.PayChannelQueryDTO;
import com.indo.admin.pojo.vo.PayChannelConfigVO;
import com.indo.core.pojo.entity.PayChannelConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 支付渠道配置 Mapper 接口
 * </p>
 *
 * @author xxx
 * @since 2021-12-20
 */
@Mapper
public interface PayChannelConfigMapper extends BaseMapper<PayChannelConfig> {

    List<PayChannelConfigVO> queryAll(@Param("page") Page<PayChannelConfigVO> page,@Param("dto")  PayChannelQueryDTO dto);

}
