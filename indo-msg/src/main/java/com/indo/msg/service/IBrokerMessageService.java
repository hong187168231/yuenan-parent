package com.indo.msg.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.indo.common.enums.BrokerMessageStatus;
import com.indo.msg.pojo.entity.BrokerMessage;

import java.util.List;

/**
 * <p>
 * 消息记录表 服务类
 * </p>
 *
 * @author xxx
 * @since 2022-01-03
 */
public interface IBrokerMessageService extends IService<BrokerMessage> {


    boolean saveMessage(BrokerMessage message);

    void succuess(String messageId);

    void failure(String messageId);

    List<BrokerMessage> queryTimeoutMessage4Retry(BrokerMessageStatus brokerMessageStatus);

    BrokerMessage selectByMessageId(String messageId);

    void updateTryCount(String messageId);
}
