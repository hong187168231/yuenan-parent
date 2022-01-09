package com.indo.job.receiver;

import com.indo.common.constant.RabbitConstants;
import com.indo.common.constant.RedisKeys;
import com.indo.common.redis.utils.RedisUtils;
import com.indo.job.service.IMemBaseinfoService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class UserConsumer {

    @Autowired
    private IMemBaseinfoService iMemBaseinfoService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = RabbitConstants.Queue.UP_LEVEL, durable = "true", autoDelete = "false"),
            exchange = @Exchange(value = RabbitConstants.USER_LEVEL_TOPIC),
            key = {RabbitConstants.Key.UP_LEVEL}
    ))
    public void upLevelMessage(Message message, Channel channel) {
        try {
            String messageId = message.getMessageProperties().getMessageId();
            String payload = new String(message.getBody());
            String cacheMessageId = RedisUtils.get(RedisKeys.SYS_RABBITMQ_MSG_ID_KEY + messageId);
            if (messageId.equals(cacheMessageId)) {
                log.warn("该消息已经被消费  ====== {}", message);
                return;
            }
            iMemBaseinfoService.upLevel(payload);
            Long deliveryTag = message.getMessageProperties().getDeliveryTag();
            //手工ACK
            channel.basicAck(deliveryTag, false);
            RedisUtils.set(RedisKeys.SYS_RABBITMQ_MSG_ID_KEY + messageId, messageId, 60 * 60);
        } catch (IOException e) {
            log.error("====", e);
        }

    }


}
