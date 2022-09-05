package com.indo.game.pojo.vo.callback.tcgwin;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class TCGWinDebitCallBackResponse<T> extends TCGWinParentCallBackResponse {

    private List<T> balance_info;

}
