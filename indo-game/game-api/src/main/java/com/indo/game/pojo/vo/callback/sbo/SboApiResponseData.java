package com.indo.game.pojo.vo.callback.sbo;

import lombok.Data;

@Data
public class SboApiResponseData {
    private String serverId;
    private SboApiResponseError error;
    private String Token;
    private String url;
    private String username;
}
