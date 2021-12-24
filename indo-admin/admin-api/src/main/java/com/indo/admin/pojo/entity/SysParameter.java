package com.indo.admin.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.indo.common.pojo.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 系统参数
 * </p>
 *
 * @author puff
 * @since 2021-09-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysParameter extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 系统参数id
     */
    @TableId(value = "param_id", type = IdType.AUTO)
    private Long paramId;

    /**
     * 系统参数代码
     */
    private String paramCode;

    /**
     * 系统参数名称
     */
    private String paramName;

    /**
     * 参数说明
     */
    private String remark;

    /**
     * 系统参数值
     */
    private String paramValue;


    /**
     * 是否删除 0 未删除 1 删除
     */
    private Integer status;


    /**
     *
     */
    private Integer sortBy;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 最后修改人
     */
    private String updateUser;


}
