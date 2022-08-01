package com.indo.user.mapper;

import com.indo.core.base.mapper.SuperMapper;
import com.indo.core.pojo.bo.MemBaseInfoBO;
import com.indo.core.pojo.entity.MemBaseinfo;
import com.indo.user.pojo.bo.MemTradingBO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;


@Mapper
public interface MemBaseInfoMapper extends SuperMapper<MemBaseinfo> {

    MemTradingBO tradingInfo(String account);

    MemBaseInfoBO findMemBaseInfoByAccount(@Param("account") String account);

    /**
     * 查询用户有效投注
     * @param account
     * @return
     */
    BigDecimal findUserBetMoney(@Param("account") String account);
}
