package com.indo.user.pojo.dto;

import io.swagger.annotations.ApiModelProperty;

public class MemGrowthPointsConfigDTO {
    @ApiModelProperty(value = "参数键名")
    private String configKey;

    @ApiModelProperty(value = "参数键值")
    private String configValue;

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    MemGrowthPointsConfigDTO(){
    }
}
