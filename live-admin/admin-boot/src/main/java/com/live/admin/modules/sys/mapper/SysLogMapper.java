/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制
 */
package com.live.admin.modules.sys.mapper;

import com.live.admin.pojo.entity.SysLog;
import com.live.common.mybatis.base.mapper.SuperMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * @author puff
 */
@Repository
@Mapper
public interface SysLogMapper extends SuperMapper<SysLog> {

    @Delete("delete from sys_log where log_type = #{logType}")
    void deleteByLogType(@Param("logType") String logType);

    @Select("<script>select l.id,l.create_time as createTime,l.description, l.request_ip as requestIp,l.address,u.nickname from sys_log l  " +
            " left join yx_user u on u.uid=l.uid where l.type=1 " +
            " <if test = \"nickname !=null\"> and u.nickname LIKE CONCAT('%',#{nickname},'%')</if> order by l.id desc</script>")
    List<SysLog> findAllByPageable(@Param("nickname") String nickname);

    @Select("select count(*) FROM (select request_ip FROM sys_log where create_time between #{date1} and #{date2} GROUP BY request_ip) as s")
    long findIp(@Param("date1") String date1, @Param("date2") String date2);
}
