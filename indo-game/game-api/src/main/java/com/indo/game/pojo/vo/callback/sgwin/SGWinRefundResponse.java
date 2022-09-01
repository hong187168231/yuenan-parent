package com.indo.game.pojo.vo.callback.sgwin;


import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

@Data
public class SGWinRefundResponse {
    @JSONField(name = "Response")
    private List Response;
}
