package com.indo.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.indo.core.pojo.bo.MemBaseinfoBo;
import com.indo.core.pojo.entity.MemGoldChange;
import com.indo.core.pojo.vo.MemTradingVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * <p>
 * 会员账变记录表 Mapper 接口
 * </p>
 *
 * @author puff
 * @since 2021-12-07
 */
@Mapper
public interface MemTradingMapper {

    MemBaseinfoBo findMemBaseInfoById(@Param("memId") Long memId);

    MemBaseinfoBo findMemBaseInfoByAccount(@Param("account") String account);


    MemTradingVO tradingInfo(String account);


}
