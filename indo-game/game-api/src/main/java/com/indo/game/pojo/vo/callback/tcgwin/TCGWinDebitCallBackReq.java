package com.indo.game.pojo.vo.callback.tcgwin;

import lombok.Data;

import java.util.List;

@Data
public class TCGWinDebitCallBackReq<T> {

    private String method;//	Y	String	parameter = sgb is a constant
//            数值sgb是一个字串常数
    private List<T> transactions;
    private Integer product_type;//	Y	Int
//    refer to Appendix Product Type-> Abbrev
//
//            请参阅附件产品代码

    private Long request_time;//	Y	Long
//    Timestamp of transaction(millisecond)
//
//    交易时间戳（毫秒）

}
