package com.live.auth.common.enums;

import lombok.Getter;


/**
 * @author puff
 * @description TODO
 * @createTime 2021/5/31 23:55
 */
public enum OAuthClientEnum {

    TEST("client", "测试客户端"),
    ADMIN("live-admin", "系统管理端"),
    WEAPP("live-weapp", "微信小程序端"),
    APP("live-app", "app端登录");

    @Getter
    private String clientId;

    @Getter
    private String desc;

    OAuthClientEnum(String clientId, String desc) {
        this.clientId = clientId;
        this.desc = desc;
    }

    public static OAuthClientEnum getByClientId(String clientId) {
        for (OAuthClientEnum client : OAuthClientEnum.values()) {
            if (client.getClientId().equals(clientId)) {
                return client;
            }
        }
        return null;
    }


}
