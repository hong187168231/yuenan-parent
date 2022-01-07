package com.indo.msg.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.indo.common.pojo.entity.BaseEntity;
import com.indo.msg.pojo.entity.BrokerMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 消息记录表 Mapper 接口
 * </p>
 *
 * @author xxx
 * @since 2022-01-01
 */
@Mapper
public interface BrokerMessageMapper extends BaseMapper<BrokerMessage> {

    List<BrokerMessage> queryBrokeryMessageStatus4Timeout(@Param("status") Integer status);

    int updateForTryCount(@Param("messageId") String messageId, @Param("updateTime") Date updateTime);

    @Select("select * from broker_message")
    BrokerMessage selectById2();
}
