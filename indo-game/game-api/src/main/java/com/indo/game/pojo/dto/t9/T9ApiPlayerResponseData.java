package com.indo.game.pojo.dto.t9;


import lombok.Data;

import java.math.BigDecimal;

/**
 * 玩家余额相关信息
 */
@Data
public class T9ApiPlayerResponseData {

    private BigDecimal balance;  // 玩家余额

}
