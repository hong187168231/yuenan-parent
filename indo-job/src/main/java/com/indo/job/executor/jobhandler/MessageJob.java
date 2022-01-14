package com.indo.job.executor.jobhandler;

import com.alibaba.fastjson.JSONObject;
import com.indo.common.enums.BrokerMessageStatus;
import com.indo.common.rabbitmq.bo.Message;
import com.indo.core.brokery.RabbitBroker;
import com.indo.core.pojo.entity.BrokerMessage;
import com.indo.core.service.IBrokerMessageService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
public class MessageJob {

    private static final Integer max_retry_count = 3;

    @Autowired
    private IBrokerMessageService iMessageStoreService;

    @Autowired
    private RabbitBroker rabbitBroker;

    @XxlJob("messageJob")
    private void messageJob() {
        List<BrokerMessage> datList = iMessageStoreService.queryTimeoutMessage4Retry(BrokerMessageStatus.SENDING);
        datList.forEach(brokeryMessage -> {
            if (brokeryMessage.getTryCount() >= max_retry_count) {
                this.iMessageStoreService.failure(brokeryMessage.getMessageId());
                log.warn("消失设置为失败，消息id: {}  ====== ", brokeryMessage.getMessageId());
            } else {
                iMessageStoreService.updateTryCount(brokeryMessage.getMessageId());
                Message message = JSONObject.parseObject(brokeryMessage.getMessage(), Message.class);
                rabbitBroker.reliantSend(message);
            }
        });
    }

}
