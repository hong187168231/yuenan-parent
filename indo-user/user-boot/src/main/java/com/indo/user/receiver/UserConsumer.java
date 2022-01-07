package com.indo.user.receiver;

import com.indo.common.constant.RabbitConstants;
import com.indo.common.constant.RedisKeys;
import com.indo.user.common.util.UserBusinessRedisUtils;
import com.indo.user.service.MemBaseInfoService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
@Slf4j
public class UserConsumer {

    @Autowired
    private MemBaseInfoService memBaseInfoService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = RabbitConstants.Queue.UP_LEVEL, durable = "true", autoDelete = "false"),
            exchange = @Exchange(value = RabbitConstants.USER_LEVEL_TOPIC),
            key = {RabbitConstants.Key.UP_LEVEL}
    ))
    public void upLevelMessage(Message message, Channel channel) {
        try {
            String messageId = message.getMessageProperties().getMessageId();
            String payload = new String(message.getBody());
            String cacheMessageId = UserBusinessRedisUtils.get(RedisKeys.SYS_RABBITMQ_MSG_ID_KEY + messageId);
            if (messageId.equals(cacheMessageId)) {
                log.warn("该消息已经被消费  ====== {}", message);
                return;
            }
            memBaseInfoService.upLevel(payload);
            Long deliveryTag = message.getMessageProperties().getDeliveryTag();
            //手工ACK
            channel.basicAck(deliveryTag, false);
            UserBusinessRedisUtils.set(RedisKeys.SYS_RABBITMQ_MSG_ID_KEY + messageId, messageId, 60 * 60);
        } catch (IOException e) {
            log.error("====", e);
        }

    }



}
