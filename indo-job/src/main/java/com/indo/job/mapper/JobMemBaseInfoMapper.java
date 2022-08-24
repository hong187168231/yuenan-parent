package com.indo.job.mapper;

import com.indo.core.base.mapper.SuperMapper;
import com.indo.core.pojo.entity.MemBaseinfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface JobMemBaseInfoMapper extends SuperMapper<MemBaseinfo> {

    @Select("SELECT id FROM mem_baseinfo where DATE_FORMAT(create_time, \"%Y%m%d\") = #{date}")
    List<Long> findIdListByCreateTime(@Param("date") String date);
}
