package com.indo.game.pojo.dto.ka;


import lombok.Data;

/**
 * 访问KA游戏返回
 */
@Data
public class KAApiCommonResp {

    private Integer statusCode;  // 0正常
    private String status;//状态结果集
    private Long balance; // 余额,需要乘以100
    private String walletTransactionId; // 交易序号

}
