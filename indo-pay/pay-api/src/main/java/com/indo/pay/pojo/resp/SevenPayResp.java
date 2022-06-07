package com.indo.pay.pojo.resp;


/*
  {
    "code": 1,
    "data": "{" +
    "\"amount\":\"50001.0000\"," +
    "\"bamount\":null," +
    "\"orderid\":\"HSBxpKBp4c2x1647505032703\",\
    "pageurl\":\"http://120.77.76.146:9080/t5/index.html?ticket=25a3c10ded4ddd7d5ab1430ad3a6d326\",\
    "sign\":\"f17a81b2cff61818fcb848070796e5c3\"," +
    "\"ticket\":\"25a3c10ded4ddd7d5ab1430ad3a6d326\",\
    "type\":\"Momo\"," +
    "\"userid\":\"4d492429445c4d1698647dcdab8d87c0\"}",
    "msg": "success"
   }
*/
public class SevenPayResp extends BasePayResp {

    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
