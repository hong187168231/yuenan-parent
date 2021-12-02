package com.indo.pay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.pay.pojo.dto.PayCashOrderDTO;
import com.indo.pay.pojo.entity.PayCashOrder;
import com.indo.pay.pojo.vo.PayCashOrderVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xxx
 * @since 2021-11-13
 */
@Mapper
public interface PayCashOrderMapper extends BaseMapper<PayCashOrder> {

}
