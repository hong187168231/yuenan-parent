package com.indo.core.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.indo.common.pojo.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 成长积分配置表
 * </p>
 *
 * @author puff
 * @since 2021-08-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MemGrowthPointsConfig extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 参数名称
     */
    private String configName;

    /**
     * 参数键名
     */
    private String configKey;

    /**
     * 参数键值
     */
    private String configValue;

    /**
     * 是否删除 0 未删除 1 删除
     */
    private Boolean isDel;

    /**
     * 备注
     */
    private String remark;


}
