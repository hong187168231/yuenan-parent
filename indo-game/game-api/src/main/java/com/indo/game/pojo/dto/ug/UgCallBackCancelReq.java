package com.indo.game.pojo.dto.ug;


import lombok.Data;

import java.util.List;
@Data
public class UgCallBackCancelReq<T> extends UgCallBackParentReq{
    private List<T> data;
}
