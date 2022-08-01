package com.indo.game.pojo.dto.manage;

import lombok.Data;

import java.util.List;

@Data
public class GameInfoPageImpReq extends GameInfoPageReq {

    //下级代理
    private List agentAcctList;

}
