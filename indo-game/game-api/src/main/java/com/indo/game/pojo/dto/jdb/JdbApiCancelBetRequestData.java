package com.indo.game.pojo.dto.jdb;

import lombok.Data;

@Data
public class JdbApiCancelBetRequestData extends JdbApiRequestParentData{
    private Long transferId;// Long 交易序号


}
