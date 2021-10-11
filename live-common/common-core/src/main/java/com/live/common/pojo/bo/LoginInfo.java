package com.live.common.pojo.bo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class LoginInfo implements Serializable {

    private Long id;

    private Integer gender;

    private String nickName;

    private String account;

    private String mobile;

    private LocalDate birthday;

    private String avatarUrl;

    private String openid;

    private String sessionKey;

    private Integer status;

    private Integer point;

    private Integer deleted;

    private Long balance;

    private String city;

    private String country;

    private String language;

    private String province;
}
