package com.indo.auth.common.enums;

import lombok.Getter;


/**
 * @author puff
 * @description TODO
 * @createTime 2021/5/31 23:55
 */
public enum OAuthClientEnum {

    TEST("client", "测试客户端"),
    ADMIN("indo-admin", "系统管理端"),
    WEAPP("indo-weapp", "微信小程序端"),
    APP("indo-app", "app端登录");

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
