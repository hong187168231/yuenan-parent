package com.indo.game.pojo.vo.callback.ug;


import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class UgCallBackGetBalanceResp extends UgCallBackParentResp {
    private BigDecimal data;
}
