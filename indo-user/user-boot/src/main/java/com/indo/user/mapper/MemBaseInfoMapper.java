package com.indo.user.mapper;

import com.indo.core.base.mapper.SuperMapper;
import com.indo.core.pojo.entity.MemBaseinfo;
import com.indo.core.pojo.vo.MemTradingVO;
import com.indo.user.pojo.bo.MemTradingBO;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface MemBaseInfoMapper extends SuperMapper<MemBaseinfo> {


    MemTradingBO tradingInfo(String account);

}
