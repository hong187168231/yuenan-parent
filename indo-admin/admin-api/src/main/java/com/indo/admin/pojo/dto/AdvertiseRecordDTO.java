package com.indo.admin.pojo.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.indo.common.base.BaseDTO;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AdvertiseRecordDTO extends BaseDTO {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 标题
     */
    private String image;

    /**
     * 内容
     */
    private String skipUrl;

    /**
     * 发送人id
     */
    private String content;

    /**
     * 状态 0 下架1 上架
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;
}
