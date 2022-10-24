package com.indo.game.pojo.dto.sgwin;

import lombok.Data;


@Data
public class SgwinRequest {

    private String root;//	String	Reseller account, to be provided by Apivn
    private String agentID;//	String	Retailer account. Reseller can create Retailer from Backend
    private String username;//	String	Retailer account
    private int userType;//	int	2: Retailer
    private long timestamp;//	long	timestamp
    private String sign;//	String	Result string from merchant's private key sign, refer to <API Definition>

}
