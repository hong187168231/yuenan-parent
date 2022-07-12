package com.indo.game.pojo.dto.wm;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WmCallBackReq {
    private String cmd;//	String	CallBalance
    private String signature;//	String	代理商标识符
    private String user;//	String	贵司在此申请的游戏帐号
    private String requestDate;//	String	我方请求时间 date("Y-m-d H:i:s")
    private BigDecimal money;//	decimal	加(扣)该会员的点数
    private String dealid;//	String	交易单号
    private String gtype;//	String	此游戏项目 101:百家乐,102:龙虎,103:轮盘,104:骰宝,105:牛牛,106:三公,107:番摊,108:色碟,110:鱼虾蟹,111:炸金花,112:温州牌九,113:二八杠,128:安達巴哈
    private String type;//	String	游戏项目_期数_局号_加扣点类型
    //    例:101_112139999_88_2
    //            0:电子游戏结算 1:加点 2:扣点 3:重对加点 4:重对扣点 5:重新派彩 cancel:取消
    private String betdetail;//	String	下注内容:金额 http://wmapi.a45.me/betdetail.html
    private String gameno;//	String	游戏项目_期数_局号
//    例:101_112139999_88
//    cancel:取消
//    例:101_112139999_88_cancel
    private String code;//	String	加扣点类型 0:电子游戏结算 1:加点 2:扣点 3:重对加点 4:重对扣点 5:重新派彩
    private String category;//	String	1:下注单 2:小费单
    private Integer betId;//	integer	注单编号(重对时發送)
    private BigDecimal payout;//	decimal	派彩金额(重对时發送)



}
