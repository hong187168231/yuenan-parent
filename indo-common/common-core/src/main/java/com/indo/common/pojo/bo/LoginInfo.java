package com.indo.common.pojo.bo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class LoginInfo implements Serializable {

    private Long id;

    private String account;

    private Integer memLevel;

    private String nickName;

    private String mobile;

    private LocalDate birthday;

    private String headImage;

    private String openid;

    private Integer status;

    private String city;

    private String language;

    private BigDecimal balance;

}
