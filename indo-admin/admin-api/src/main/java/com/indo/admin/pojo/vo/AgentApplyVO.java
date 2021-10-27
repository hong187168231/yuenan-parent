package com.indo.admin.pojo.vo;

import java.io.Serializable;

public class AgentApplyVO implements Serializable {


    /**
     * 用户ID
     */
    private Long memId;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 状态0 待审核 1 已通过
     */
    private Integer status;

}
