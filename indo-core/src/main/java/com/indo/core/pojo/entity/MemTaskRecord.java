package com.indo.core.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.indo.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author xxx
 * @since 2022-08-05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="MemTaskRecord对象", description="")
public class MemTaskRecord extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "任务ID")
    private Integer taskId;

    @ApiModelProperty(value = "任务类型ID")
    private Integer typeId;

    @ApiModelProperty(value = "会员ID")
    private Integer memId;

    @ApiModelProperty(value = "状态：0待领取，1已领取")
    private Integer states;

    @ApiModelProperty(value = "领取时间")
    private LocalDateTime pickTime;

    @ApiModelProperty(value = "备注")
    private String remark;


}
