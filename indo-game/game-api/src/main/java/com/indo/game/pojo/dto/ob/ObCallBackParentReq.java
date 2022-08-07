package com.indo.game.pojo.dto.ob;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class ObCallBackParentReq {
    /**
     * 用户名
     */
    private String userName;
    /**
     * 业务类型,账变来源(1投注,2结算派彩,3撤单,4撤单回滚,5结算回滚,6拒单)
     */
    private String bizType;
    /**
     * 商户编码
     */
    private String merchantCode;
    /**
     * 账变都是唯一的(
     */
    private String transferId;
    /**
     * Double类型金额
     */
    private BigDecimal amount;

    /**
     * 账变类型(1加款,2扣款)
     */
    private String transferType;
    /**
     * 订单列表(json字符串)
     */
    private String orderStr;

    /**
     * Long型时间戳(13位)
     */
    private String timestamp;

    /**
     * 签名
     */
    private String siganature;
    /**
     * 商户方的用户会话
     */
    private String stoken;
    /**
     * 二次结算标识.1:是,0:否
     */
    private String secondSettleFlag;
    /**
     * 商户端注单ID. (
     */
    private String ticketId;

}
