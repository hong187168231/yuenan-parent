
package com.indo.admin.pojo.criteria;

import com.indo.common.annotation.Query;
import lombok.Data;


@Data
public class PayWayQueryCriteria {

    @Query
    private Integer status = 1;

}
