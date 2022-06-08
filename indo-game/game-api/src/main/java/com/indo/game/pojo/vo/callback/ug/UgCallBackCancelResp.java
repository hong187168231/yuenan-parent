package com.indo.game.pojo.vo.callback.ug;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

@Data
public class UgCallBackCancelResp<T> extends UgCallBackParentResp {
    private List<T> data;
}
