package com.indo.user.mapper;

import com.indo.common.mybatis.base.mapper.SuperMapper;
import com.indo.user.pojo.entity.MemBaseinfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;


@Mapper
public interface MemBaseInfoMapper extends SuperMapper<MemBaseinfo> {

    //更新会员金额变动
    int updateMemberAmount(@Param("balance") BigDecimal amount, @Param("canAmount") BigDecimal canAmount, @Param("userId") Long userId);



}
