package com.live.common.pojo.bo;

import lombok.Data;

/**
 * JWT载体
 *
 * @author 
 * @date 2021-03-10
 */
@Data
public class JwtTokenPayload {

    private String jti;

    private Long exp;
}
