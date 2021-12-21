package com.indo.game.pojo.entity.ug;

import lombok.Data;


@Data
public class UgCallBackCancelReq extends UgCallBackParentReq{
    private String Method;//
    private String Account;//登录帐号
    private String TransactionNo;//交易号
}
