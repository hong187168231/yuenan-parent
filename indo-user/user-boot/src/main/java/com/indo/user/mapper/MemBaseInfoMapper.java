package com.indo.user.mapper;

import com.indo.core.base.mapper.SuperMapper;
import com.indo.core.pojo.bo.MemBaseInfoBO;
import com.indo.core.pojo.entity.MemBaseinfo;
import com.indo.core.pojo.bo.MemTradingBO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;


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


    @Select({"<script> "
        +" SELECT count(*) FROM mem_baseinfo where id in"
        +" <foreach item='item' index='index' collection='memIds' open='(' separator=',' close=')'>"
        +"   #{item}"
        +" </foreach> AND DATE_FORMAT(create_time, \"%Y%m%d\") = #{date}"
        + "</script>"
    })
    Integer countByIdsAndCreateTime(@Param("memIds") List<Long> memIds, @Param("date") String date);


    @Select({"<script> "
        +" SELECT count(*) FROM mem_baseinfo where id in"
        +" <foreach item='item' index='index' collection='memIds' open='(' separator=',' close=')'>"
        +"   #{item}"
        +" </foreach> " +
        "AND DATE_FORMAT(create_time, \"%Y%m\") = #{date}"
        + "</script>"
    })
    Integer countMonthAddNumByIdsAndCreateTime(@Param("memIds") List<Long> memIds, @Param("date") String date);
}
