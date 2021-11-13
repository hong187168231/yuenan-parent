package com.indo.game.mapper;

import com.indo.admin.pojo.entity.SysParameter;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysParameterMapperExt {

    SysParameter selectByCode(String sysparamcode);

//    Page<SysParameter> getList(SysParameter req, RowBounds rowBounds);
//
//    SysParameter getRepeat(SysParameter req);
//
//    List<SysParameter> queryByCodeNames(@Param("codeNames") List<String> codeNames);
//
//    List<String> getSameCodeParamList(@Param("sysparamcode") String code, @Param("sort") String sort);
//
//    @Select("select sysparamcode from sys_parameter")
//    List<String> queryAllKey();

}