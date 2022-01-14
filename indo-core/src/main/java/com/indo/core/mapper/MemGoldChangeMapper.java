package com.indo.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.indo.core.pojo.bo.MemBaseinfoBo;
import com.indo.core.pojo.entity.MemGoldChange;
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
public interface MemGoldChangeMapper extends BaseMapper<MemGoldChange> {


    //更新会员金额变动
    int updateMemberAmount(@Param("balance") BigDecimal amount,
                           @Param("canAmount") BigDecimal canAmount,
                           @Param("betAmount") BigDecimal betAmount,
                           @Param("rechargeAmount") BigDecimal rechargeAmount,
                           @Param("canAmount") BigDecimal cashAmount,
                           @Param("memId") Long memId);


}
