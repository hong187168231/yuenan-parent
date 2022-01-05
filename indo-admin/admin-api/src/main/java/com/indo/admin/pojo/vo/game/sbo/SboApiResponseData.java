package com.indo.admin.pojo.vo.game.sbo;

import lombok.Data;

@Data
public class SboApiResponseData {
    private String serverId;
    private SboApiResponseError error;
    private String Token;
    private String url;
    private String username;
}
