package com.indo.common.rabbitmq.bo;

public class MessageType {


    /**
     * 迅速消息： 不需要保障消息的可靠性，也不需要做confirm确认
     */
    public final static String RAPID = "0";


    /**
     * 确认消息： 不需要保障消息的可靠性，但是会做消息confirm确认
     */
    public final static String CONFIRM = "1";


    /**
     * 可靠性消息： 一定消息保障消息100%投递，不允许有任何的消息丢失
     * PS: 保障数据库和所发的消息的原子性操作（最终一致的）
     */
    public final static String RELIANT = "2";
}
