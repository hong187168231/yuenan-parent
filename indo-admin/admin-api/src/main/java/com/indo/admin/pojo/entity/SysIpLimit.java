package com.indo.admin.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.indo.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 黑白名单IP限制表
 * </p>
 *
 * @author xxx
 * @since 2022-04-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="SysIpLimit对象", description="黑白名单IP限制表")
public class SysIpLimit extends BaseEntity {

    private static final long serialVersionUID = 1L;
    @TableId(type= IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "IP地址")
    private String ip;

    @ApiModelProperty(value = "类型：1移动端黑名单，2后管白名单")
    private Integer types;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "更新人")
    private String updateUser;


}
