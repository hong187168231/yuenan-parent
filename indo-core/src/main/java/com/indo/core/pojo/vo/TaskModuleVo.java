package com.indo.core.pojo.vo;

import com.indo.core.pojo.entity.SysTask;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TaskModuleVo {
    @ApiModelProperty(value = "任务类型名称")
    private String taskTypeName;

    @ApiModelProperty(value = "任务信息集合")
    private List<SysTask> taskList;

}
