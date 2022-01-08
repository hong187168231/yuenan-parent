package com.indo.core.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.indo.common.pojo.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * <p>
 * 消息记录表
 * </p>
 *
 * @author xxx
 * @since 2022-01-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="BrokerMessage对象", description="消息记录表")
public class BrokerMessage extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId
    private String messageId;

    private String message;

    private Integer tryCount;

    private Integer status;

    private Date nextRetry;


}
