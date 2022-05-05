package com.indo.game.pojo.dto.ug;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class UgCallBackTransferReq<T> extends UgCallBackParentReq {
    private List<T> data;

}
