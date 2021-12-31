package com.indo.common.rabbitmq.api;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MessageBuilder {
    /**
     * 消息的唯一ID
     */
    private String messageId;


    /**
     * 消息的主题
     */
    private String topic;

    /**
     * 消息的路由规则
     */
    private String routingKey = "";

    /**
     * 消息的附加属性
     */
    private Map<String,Object> attributes = new HashMap<>();

    /**
     * 延迟消息的参数配置
     */
    private int delayMills;

    /**
     * 消息类型.默认为Confirm消息
     */
    private String messageType = MessageType.CONFIRM;


    private MessageBuilder(){

    }

    public static MessageBuilder create(){
        return new MessageBuilder();
    }

    public MessageBuilder withMessageId(String messageId){
        this.messageId = messageId;
        return  this;
    }

    public MessageBuilder withTopic(String topic){
        this.topic = topic;
        return  this;
    }

    public MessageBuilder withRoutingKey(String routingKey){
        this.routingKey = routingKey;
        return  this;
    }

    public MessageBuilder withAttributes(Map<String,Object> attributes){
        this.attributes = attributes;
        return  this;
    }

    public MessageBuilder withAttribute(String key, Object object){
        this.attributes.put(key,object);
        return  this;
    }

    public MessageBuilder withMessageId(int delayMills){
        this.delayMills = delayMills;
        return  this;
    }

    public MessageBuilder withMessageType(String messageType){
        this.messageType = messageType;
        return  this;
    }

    public Message build(){
        if(messageId == null){
            messageId = UUID.randomUUID().toString();
        }
        if(topic == null){
            throw new MessageRunTimeException("this topic is null");
        }
        Message message = new Message(messageId,topic,routingKey,attributes,delayMills,messageType);
        return  message;
    }


}