package com.indo.game.pojo.vo.callback.ug;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class UgCallBackBalanceResp<T> extends UgCallBackParentResp {
    private List<T> data;
}
