package com.indo.core.mapper;

import com.indo.core.pojo.bo.MemGoldInfoBO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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

    MemGoldInfoBO findMemGoldInfo(@Param("memId") Long memId);

}
