package com.indo.user.mapper;

import com.indo.core.base.mapper.SuperMapper;
import com.indo.core.pojo.bo.MemBaseInfoBO;
import com.indo.core.pojo.entity.MemBaseinfo;
import com.indo.user.pojo.bo.MemTradingBO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface MemBaseInfoMapper extends SuperMapper<MemBaseinfo> {

    MemTradingBO tradingInfo(String account);

    MemBaseInfoBO findMemBaseInfoByAccount(@Param("account") String account);


}
