
package com.indo.admin.pojo.criteria;

import com.indo.common.annotation.Query;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

/**
 * 日志查询类
 * @author hupeng
 * @date 2019-6-4 09:23:07
 */
@Data
public class SysLogQueryCriteria {

    @Query(blurry = "username,description,address,requestIp,method,params")
    private String blurry;

    @Query
    private String logType;

    @Query(type = Query.Type.BETWEEN)
    private List<Timestamp> createTime;


    @Query
    private Integer type;
}
