//package com.live.user.receiver;
//
//import com.live.common.constant.RabbitConstants;
//import com.rabbitmq.client.Channel;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.core.Message;
//import org.springframework.amqp.rabbit.annotation.Exchange;
//import org.springframework.amqp.rabbit.annotation.Queue;
//import org.springframework.amqp.rabbit.annotation.QueueBinding;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.amqp.support.AmqpHeaders;
//import org.springframework.messaging.handler.annotation.Headers;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.util.Map;
//
//@Component
//@Slf4j
//public class UserConsumer {
//
//
//    @RabbitListener(bindings = @QueueBinding(
//            value = @Queue(value = "queue.message", durable = "true"),
//            exchange = @Exchange(value = RabbitConstants.USER_EXCHANGE_TOPIC),
//            key = {"key.user"}
//    ))
//    public void getSmsMessage(Message message, Channel channel) {
//        System.err.println("------------------------------------");
//        System.err.println("消费端Payload:" +new String(message.getBody()));
//        Long deliverytag =  message.getMessageProperties().getDeliveryTag();
//        //手工ACK
//        try {
//            channel.basicAck(deliverytag, false);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    @RabbitListener(bindings = @QueueBinding(
//            value = @Queue(value = "queue.message2", durable = "true"),
//            exchange = @Exchange(value = RabbitConstants.USER_EXCHANGE_TOPIC2),
//            key = {"key.user2"}
//    ))
//    public void onOrderMessage(Message msg, Channel channel,
//                               @Headers Map<String, Object> headers) throws Exception {
//        System.err.println("------------------------------------");
//        System.err.println("消费端Order:" + msg);
//        Long deliverytag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
//        //手工ACK
//        channel.basicAck(deliverytag, false);
//    }
//
//
//
//    @RabbitListener(bindings = @QueueBinding(
//            value = @Queue(value = "queue.message3", durable = "true"),
//            exchange = @Exchange(value = RabbitConstants.USER_EXCHANGE_TOPIC3),
//            key = {"key.user3"}
//    ))
//    public void onOrderMessage3(org.springframework.messaging.Message message, Channel channel,
//                                @Headers Map<String, Object> headers) throws Exception {
//        System.err.println("------------------------------------");
//        System.err.println("消费端Order:" + message.getPayload());
//        Long deliverytag = (Long) message.getHeaders().get(AmqpHeaders.DELIVERY_TAG);
//        //手工ACK
//        channel.basicAck(deliverytag, false);
//    }
//
//
//
//}
