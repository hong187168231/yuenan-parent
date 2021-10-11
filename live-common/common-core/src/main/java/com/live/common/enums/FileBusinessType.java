package com.live.common.enums;

/**
 * @Author: Mzh
 * @Date: 2021/5/8 10:30
 * @Version: 1.0.0
 * @Desc: 文件业务类型
 */
public enum FileBusinessType {
    /**
     * 银行信息管理相关
     */
    BANK_INFO("1", "bankInfo"),
    ;

    private final String code;
    private final String info;

    FileBusinessType(String code, String info)
    {
        this.code = code;
        this.info = info;
    }

    public String getCode()
    {
        return code;
    }

    public String getInfo()
    {
        return info;
    }
}
