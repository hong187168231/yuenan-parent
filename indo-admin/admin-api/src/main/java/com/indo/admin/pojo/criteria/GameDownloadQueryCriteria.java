
package com.indo.admin.pojo.criteria;

import com.indo.common.annotation.Query;
import lombok.Data;


@Data
public class GameDownloadQueryCriteria {

    @Query
    private Integer isStart = 1;

}
