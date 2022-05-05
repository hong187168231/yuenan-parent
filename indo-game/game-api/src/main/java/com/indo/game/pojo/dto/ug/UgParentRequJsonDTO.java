package com.indo.game.pojo.dto.ug;


import com.indo.common.config.OpenAPIProperties;
import lombok.Data;

@Data
public class UgParentRequJsonDTO {

    private String apiKey = OpenAPIProperties.UG_COMPANY_KEY;

    private String operatorId = OpenAPIProperties.UG_API_KEY;

}
