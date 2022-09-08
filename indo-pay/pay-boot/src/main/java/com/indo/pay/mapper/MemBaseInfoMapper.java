package com.indo.pay.mapper;

import com.indo.core.base.mapper.SuperMapper;
import com.indo.core.pojo.bo.MemBaseInfoBO;
import com.indo.core.pojo.bo.MemTradingBO;
import com.indo.core.pojo.entity.MemBaseinfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.util.List;


@Mapper
public interface MemBaseInfoMapper extends SuperMapper<MemBaseinfo> {

    MemBaseInfoBO findMemBaseInfoByAccount(@Param("account") String account);

    @Update("UPDATE mem_baseinfo SET balance=balance - #{amount}, can_amount=can_amount - #{amount} WHERE account=#{account}")
    int updateBanlaceAndCanAmount(@Param("account") String account, @Param("amount") BigDecimal amount);
}
