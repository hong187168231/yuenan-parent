package com.indo.game.controller.ob;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class ObCallBackTransferstatusReq {

    /**
     * 加扣款状态(0:失败,1:成功)
     */
    private String status;
    /**
     * 商户编码
     */
    private String merchantCode;
    /**
     * 账变都是唯一的(
     */
    private String transferId;

    /**
     * Long型时间戳(13位)
     */
    private String timestamp;

    private String msg;

}
