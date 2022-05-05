package com.indo.game.pojo.vo.callback.ug;


import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class UgCallBackCheckLoginResp extends UgCallBackParentResp {
    private boolean data;//	boolean	是否合法

}
