package com.indo.core.service;

import com.indo.core.base.service.SuperService;
import com.indo.core.pojo.dto.MemGoldChangeDto;
import com.indo.core.pojo.entity.MemGoldChange;
import com.indo.core.pojo.vo.MemTradingVO;

import java.math.BigDecimal;

/**
 * <p>
 * 会员账变记录表 服务类
 * </p>
 *
 * @author puff
 * @since 2021-12-07
 */
public interface IMemTradingService {

    MemTradingVO tradingInfo(String account);


}
