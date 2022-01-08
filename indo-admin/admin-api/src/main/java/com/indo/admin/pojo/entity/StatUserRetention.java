package com.indo.admin.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.indo.common.pojo.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author kevin
 * @since 2021-11-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("stat_user_retention")
public class StatUserRetention extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 新增用户数
     */
    private Integer newUsers;

    /**
     * 次日留存
     */
    private Double nextDay;

    /**
     * 3日留存
     */
    private Double threeDay;

    /**
     * 7日留存
     */
    private Double sevevDay;

    /**
     * 30日留存
     */
    private Double thirtyDay;


}
