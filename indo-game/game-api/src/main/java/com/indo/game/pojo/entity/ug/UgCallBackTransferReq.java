package com.indo.game.pojo.entity.ug;

import lombok.Data;

import java.util.List;

@Data
public class UgCallBackTransferReq<T> {
    private String Method;
    private List<T> Data;
}
