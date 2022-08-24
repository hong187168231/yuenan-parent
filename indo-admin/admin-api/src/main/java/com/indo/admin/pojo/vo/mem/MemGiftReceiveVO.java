package com.indo.admin.pojo.vo.mem;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@ApiModel(value = "memGiftReceiveVO对象", description = "会员礼金领取vo")
@Data
public class MemGiftReceiveVO {

    @ApiModelProperty(value = "礼金领取id")
    private Long receiveId;

    @ApiModelProperty(value = "会员id")
    private Long memId;

    @ApiModelProperty(value = "礼金类型")
    private String giftType;

    @ApiModelProperty(value = "礼金编码")
    private String giftCode;

    @ApiModelProperty(value = "礼金名称")
    private String giftName;

    @ApiModelProperty(value = "礼金金额")
    private BigDecimal giftAmount;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "用户账号")
    private String account;

}
