package com.indo.admin.modules.pay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.indo.core.pojo.entity.PayCashOrder;
import com.indo.pay.pojo.dto.PayCashOrderDTO;
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



    List<PayCashOrderVO> cashApplyList(Page<PayCashOrder> page, PayCashOrderDTO dto);

}
